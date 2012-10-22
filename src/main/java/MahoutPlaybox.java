import org.apache.mahout.classifier.df.DecisionForest;
import org.apache.mahout.classifier.df.ErrorEstimate;
import org.apache.mahout.classifier.df.builder.DefaultTreeBuilder;
import org.apache.mahout.classifier.df.data.*;
import org.apache.mahout.classifier.df.ref.SequentialBuilder;
import org.apache.mahout.common.RandomUtils;
import org.uncommons.maths.Maths;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

public class MahoutPlaybox {
    private static final int NUM_ATTRIBUTES = 10;

    /**
     * sum test error
     */
    private static double sumTestErrM;

    private static double sumTestErrOne;

    /**
     * mean time to build a forest with m=log2(M)+1
     */
    private static long sumTimeM;

    /**
     * mean time to build a forest with m=1
     */
    private static long sumTimeOne;

    /**
     * mean number of nodes for all the trees grown with m=log2(M)+1
     */
    private static long numNodesM;

    /**
     * mean number of nodes for all the trees grown with m=1
     */
    private static long numNodesOne;

    private static DecisionForest buildTree(int nbTrees, String[] trainDataValues, Data data, String descriptor) {
        int m = (int) Math.floor(Maths.log(2, data.getDataset().nbAttributes()) + 1);

        Random rng = RandomUtils.getRandom();

        DefaultTreeBuilder treeBuilder = new DefaultTreeBuilder();
        SequentialBuilder forestBuilder = new SequentialBuilder(rng, treeBuilder, data.clone());
        treeBuilder.setM(m);

        return forestBuilder.build(nbTrees);
    }

    public static void main(String[] args) throws IOException, DescriptorException {
        String descriptor = "L N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N ";
        String[] trainDataValues = fileAsStringArray("data/train.csv");
        
        String[] part1 = new String[trainDataValues.length/2];
        String[] part2 = new String[trainDataValues.length/2];

        System.arraycopy(trainDataValues, 0, part1, 0, part1.length);
        System.arraycopy(trainDataValues, part1.length, part2, 0, part2.length);
        
        trainDataValues = part1;
        String[] testDataValues = testFileAsStringArray("data/test.csv");

        testDataValues = part2;

        //===================WOOOP

        List<Integer> potentialTrees = new ArrayList<Integer>();
        potentialTrees.add(1);
        potentialTrees.add(3);
        potentialTrees.add(5);
        potentialTrees.add(10);
        potentialTrees.add(20);

        for(int numberOfTrees : potentialTrees) {
            runIteration(numberOfTrees, trainDataValues, testDataValues, descriptor);
        }

    }

    private static void runIteration(int numberOfTrees, String[] trainDataValues, String[] testDataValues, String descriptor) throws DescriptorException {
        Data data = loadData(trainDataValues,descriptor);
        Random rng = RandomUtils.getRandom();
        DecisionForest tenForestTree = buildTree(numberOfTrees, trainDataValues, data, descriptor);
        Data test = DataLoader.loadData(data.getDataset(), testDataValues);

        try {
            FileWriter fstream = new FileWriter("attempts/out-" + System.currentTimeMillis()  + ".txt");
            PrintWriter out = new PrintWriter(fstream);

            int numberCorrect = 0;
            int numberOfValues = 0;
            
            for (int i = 0; i < test.size(); i++) {
                Instance oneSample = test.get(i);
                double actualIndex = oneSample.get(0);
                int actualLabel = data.getDataset().valueOf(0, String.valueOf((int) actualIndex));

                double classify = tenForestTree.classify(test.getDataset(), rng, oneSample);
                int label = data.getDataset().valueOf(0, String.valueOf((int) classify));
                
                if(label == actualLabel) {
                    numberCorrect++;
                }
                numberOfValues++;
                //out.println(label);
                out.println(label + ", " + actualLabel);
            }

            System.out.println("Number of trees: " + numberOfTrees + " -> Number correct: " + numberCorrect + " of " + numberOfValues + " " + (numberCorrect * 1.0/numberOfValues));

            out.close();
        } catch (Exception e) {//Catch exception if any
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }

    }

    private static Data loadData(String[] sData, String descriptor) throws DescriptorException {
        Dataset dataset = DataLoader.generateDataset(descriptor, false, sData);
        return DataLoader.loadData(dataset, sData);
    }

    private static void runIteration(Random rng, Data data, int m, int nbtrees) {
        Data train = data.clone();

        Data test = train.rsplit(rng, (int) (data.size() * 0.1));

        DefaultTreeBuilder treeBuilder = new DefaultTreeBuilder();
        SequentialBuilder forestBuilder = new SequentialBuilder(rng, treeBuilder, train);
        treeBuilder.setM(m);

        long time = System.currentTimeMillis();
        System.out.println("Growing a forest with m=" + m);
        DecisionForest forestM = forestBuilder.build(nbtrees);
        sumTimeM += System.currentTimeMillis() - time;
        numNodesM += forestM.nbNodes();

        // grow a forest with m=1
        treeBuilder.setM(1);

        time = System.currentTimeMillis();
        System.out.println("Growing a forest with m=1");
        DecisionForest forestOne = forestBuilder.build(nbtrees);
        sumTimeOne += System.currentTimeMillis() - time;
        numNodesOne += forestOne.nbNodes();

        // compute the test set error (Selection Error), and mean tree error (One Tree Error),
        double[] testLabels = test.extractLabels();
        double[] predictions = new double[test.size()];

        forestM.classify(test, predictions);

        double[] sumPredictions = new double[test.size()];
        Arrays.fill(sumPredictions, 0.0);
        for (int i = 0; i < predictions.length; i++) {
            sumPredictions[i] += predictions[i];
        }
        sumTestErrM += ErrorEstimate.errorRate(testLabels, sumPredictions);

        forestOne.classify(test, predictions);
        Arrays.fill(sumPredictions, 0.0);
        for (int i = 0; i < predictions.length; i++) {
            sumPredictions[i] += predictions[i];
        }
        sumTestErrOne += ErrorEstimate.errorRate(testLabels, sumPredictions);

//        DecisionForest tree = forestBuilder.build(5);
//
//        double[] predictions = new double[test.size()];
//        tree.classify(test, predictions);
//
//        for (double prediction : predictions) {
//            System.out.println("prediction = " + prediction);
//        }
    }

    private static String[] fileAsStringArray(String file) {
        ArrayList<String> list = new ArrayList<String>();

        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            br.readLine(); // discard top one
            while ((strLine = br.readLine()) != null) {
                list.add(strLine);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
        return list.toArray(new String[list.size()]);
    }

    private static String[] testFileAsStringArray(String file) {
        ArrayList<String> list = new ArrayList<String>();

        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            br.readLine(); // discard top one
            while ((strLine = br.readLine()) != null) {
                list.add("-," + strLine);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
        return list.toArray(new String[list.size()]);
    }


}
