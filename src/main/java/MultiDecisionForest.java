package main.java;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.classifier.df.DFUtils;
import org.apache.mahout.classifier.df.DecisionForest;
import org.apache.mahout.classifier.df.data.Data;
import org.apache.mahout.classifier.df.data.DataUtils;
import org.apache.mahout.classifier.df.data.Dataset;
import org.apache.mahout.classifier.df.data.Instance;
import org.apache.mahout.classifier.df.node.Node;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MultiDecisionForest {
    private final List<Node> trees;

    public MultiDecisionForest() {
        trees = Lists.newArrayList();
    }

    public MultiDecisionForest(List<Node> trees) {
        this.trees = trees;
    }

    public List<Node> getTrees() {
        return trees;
    }

    /**
     * Classifies the data and calls callback for each classification
     */
    public void classify(Data data, double[] predictions) {
        Preconditions.checkArgument(data.size() == predictions.length, "predictions.length must be equal to data.size()");

        if (data.isEmpty()) {
            return; // nothing to classify
        }

        for (Node tree : trees) {
            for (int index = 0; index < data.size(); index++) {
                predictions[index] = tree.classify(data.get(index));
            }
        }
    }

    /**
     * predicts the label for the instance
     *
     * @param rng
     *          Random number generator, used to break ties randomly
     * @return -1 if the label cannot be predicted
     */
    public double classify(Dataset dataset, Random rng, Instance instance) {
        if (dataset.isNumerical(dataset.getLabelId())) {
            double sum = 0;
            int cnt = 0;
            for (Node tree : trees) {
                double prediction = tree.classify(instance);
                if (prediction != -1) {
                    sum += prediction;
                    cnt++;
                }
            }
            return sum / cnt;
        } else {
            int[] predictions = new int[dataset.nblabels()];
            for (Node tree : trees) {
                double prediction = tree.classify(instance);
                if (prediction != -1) {
                    predictions[(int) prediction]++;
                }
            }

            if (DataUtils.sum(predictions) == 0) {
                return -1; // no prediction available
            }

            return DataUtils.maxindex(rng, predictions);
        }
    }

    /**
     * @return Mean number of nodes per tree
     */
    public long meanNbNodes() {
        long sum = 0;

        for (Node tree : trees) {
            sum += tree.nbNodes();
        }

        return sum / trees.size();
    }

    /**
     * @return Total number of nodes in all the trees
     */
    public long nbNodes() {
        long sum = 0;

        for (Node tree : trees) {
            sum += tree.nbNodes();
        }

        return sum;
    }

    /**
     * @return Mean maximum depth per tree
     */
    public long meanMaxDepth() {
        long sum = 0;

        for (Node tree : trees) {
            sum += tree.maxDepth();
        }

        return sum / trees.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MultiDecisionForest)) {
            return false;
        }

        MultiDecisionForest rf = (MultiDecisionForest) obj;

        return trees.size() == rf.getTrees().size() && trees.containsAll(rf.getTrees());
    }

    @Override
    public int hashCode() {
        return trees.hashCode();
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(trees.size());
        for (Node tree : trees) {
            tree.write(dataOutput);
        }
    }

    /**
     * Reads the trees from the input and adds them to the existing trees
     */
    public void readFields(DataInput dataInput) throws IOException {
        int size = dataInput.readInt();
        for (int i = 0; i < size; i++) {
            trees.add(Node.read(dataInput));
        }
    }

    private static MultiDecisionForest read(DataInput dataInput) throws IOException {
        MultiDecisionForest forest = new MultiDecisionForest();
        forest.readFields(dataInput);
        return forest;
    }

    /**
     * Load the forest from a single file or a directory of files
     * @throws java.io.IOException
     */
    public static MultiDecisionForest load(Configuration conf, Path forestPath) throws IOException {
        FileSystem fs = forestPath.getFileSystem(conf);
        Path[] files;
        if (fs.getFileStatus(forestPath).isDir()) {
            files = DFUtils.listOutputFiles(fs, forestPath);
        } else {
            files = new Path[]{forestPath};
        }

        MultiDecisionForest forest = null;
        for (Path path : files) {
            FSDataInputStream dataInput = new FSDataInputStream(fs.open(path));
            try {
                if (forest == null) {
                    forest = read(dataInput);
                } else {
                    forest.readFields(dataInput);
                }
            } finally {
                Closeables.closeQuietly(dataInput);
            }
        }

        return forest;

    }
}
