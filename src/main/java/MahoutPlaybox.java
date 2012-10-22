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

    public static void main(String[] args) throws IOException, DescriptorException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        DataOutput out = new DataOutputStream(byteArrayOutputStream);
//        out.writeInt(2); // nb attributes
//
//        WritableUtils.writeString(out, Dataset.Attribute.NUMERICAL.name());
//        WritableUtils.writeString(out, Dataset.Attribute.LABEL.name());
//        String[] vals = new String[] { "1", "1" };
//        WritableUtils.writeStringArray(out, vals);
//
//        out.writeInt(1);
//        out.writeInt(1);
//
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//
//        FileOutputStream fos = new FileOutputStream("/Users/mneedham/github/mahout/mahout_test.data");
//        fos.write(bytes);
//        fos.close();
//
//        System.out.println("bytes = " + bytes);
//
//        Dataset realDataSet = Dataset.read(new DataInputStream(new FileInputStream(new File("/Users/mneedham/github/mahout/mahout_test.data"))));

//        System.out.println("realDataSet = " + realDataSet);

//        Data data = Utils.randomData(rng, NUM_ATTRIBUTES, false, 100);

//        String[] sData = new String[]{
//                "7,6,2,1,",
//                "5,7,4,1,",
//                "1,7,3,2,",
//                "4,1,8,3,",
//                "5,5,5,1,",
//                "4,4,6,3,",
//                "4,5,2,2,",
//                "2,2,7,3,",
//                "6,6,6,2,",
//                "2,9,2,2,",
//                "3,3,8,3,",
//                "3,3,7,3,",
//                "3,3,6,3,",
//                "3,3,7,3,",
//                "3,3,7,3,"
//        };


        String descriptor = "L N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N ";
        Data data = loadData(fileAsStringArray("/Users/mneedham/github/machinenursery/data/train.csv"), descriptor);

        int m = (int) Math.floor(Maths.log(2, data.getDataset().nbAttributes()) + 1);

        Random rng = RandomUtils.getRandom();
        int nbTrees = 3;

        DefaultTreeBuilder treeBuilder = new DefaultTreeBuilder();
        SequentialBuilder forestBuilder = new SequentialBuilder(rng, treeBuilder, data.clone());
        treeBuilder.setM(m);

        DecisionForest forestM = forestBuilder.build(nbTrees);

        String[] testDataValues = testFileAsStringArray("/Users/mneedham/github/machinenursery/data/test.csv");
        Data test = DataLoader.loadData(data.getDataset(), testDataValues);

        System.out.println("test.size() = " + test.size());

        try {
            FileWriter fstream = new FileWriter("/Users/mneedham/github/machinenursery/attempts/out-" + System.currentTimeMillis()  + ".txt");
            PrintWriter out = new PrintWriter(fstream);

            for (int i = 0; i < test.size(); i++) {
                Instance oneSample = test.get(i);

                double classify = forestM.classify(test.getDataset(), rng, oneSample);
                int label = data.getDataset().valueOf(0, String.valueOf((int) classify));
                out.println(label);

                System.out.println("label = " + label);
            }

            out.close();
        } catch (Exception e) {//Catch exception if any
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }


        //        Data test = data.clone().rsplit(rng, (int) (data.size() * 0.1));
//        int nbIterations = 1;
//        for (int iteration = 0; iteration < nbIterations; iteration++) {
//            System.out.println("Iteration " + iteration);
//            runIteration(rng, data, m, nbTrees);
//        }
//
//        System.out.println("********************************************");
//        System.out.println("Random Input Test Error : {}" + sumTestErrM / nbIterations);
//        System.out.println("Single Input Test Error : {}" + sumTestErrOne / nbIterations);
//        System.out.println("Mean Random Input Time : {}" + DFUtils.elapsedTime(sumTimeM / nbIterations));
//        System.out.println("Mean Single Input Time : {}" + DFUtils.elapsedTime(sumTimeOne / nbIterations));
//        System.out.println("Mean Random Input Num Nodes : {}" + numNodesM / nbIterations);
//        System.out.println("Mean Single Input Num Nodes : {}" + numNodesOne / nbIterations);

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

