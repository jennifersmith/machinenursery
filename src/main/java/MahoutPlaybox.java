package main.java;

import org.apache.mahout.classifier.df.DecisionForest;
import org.apache.mahout.classifier.df.builder.DefaultTreeBuilder;
import org.apache.mahout.classifier.df.data.*;
import org.apache.mahout.classifier.df.ref.SequentialBuilder;
import org.apache.mahout.common.RandomUtils;
import org.uncommons.maths.Maths;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import static main.java.KaggleInputReader.fileAsStringArray;

public class MahoutPlaybox {
    private static DecisionForest buildForest(int numberOfTrees, Data data) {
        int m = (int) Math.floor(Maths.log(2, data.getDataset().nbAttributes()) + 1);

        DefaultTreeBuilder treeBuilder = new DefaultTreeBuilder();
        SequentialBuilder forestBuilder = new SequentialBuilder(RandomUtils.getRandom(), treeBuilder, data.clone());
        treeBuilder.setM(m);

        return forestBuilder.build(numberOfTrees);
    }


    public static void main(String[] args) throws IOException, DescriptorException {
        String trainingSetFile = "data/train.csv";
        if (args.length < 3) {
            throw new RuntimeException("Usage: main.java.MahoutPlaybox [numberOfTrees] [ignorePixels(true|false)] [useThresholding(true|false)]");
        }

        int numberOfTrees = Integer.parseInt(args[0]);
        boolean ignorePixels = Boolean.parseBoolean(args[1]);
        boolean useThresholding = Boolean.parseBoolean(args[2]);

        System.out.println("Building " + numberOfTrees + " trees. Ignoring pixels? " + ignorePixels + ". Using thresholding? " + useThresholding);

        if(ignorePixels) {
            trainingSetFile = "data/huge.csv";
        }

        // might need to change the descriptor if we have less features

        String[] trainDataValues = fileAsStringArray(trainingSetFile, 42000, useThresholding);
        String[] testDataValues = testFileAsStringArray("data/test.csv");

        String descriptor = buildDescriptor(trainDataValues[0].split(",").length - 1);

        // take 90 percent to be the test data
        String[] part1 = new String[trainDataValues.length / 10 * 9];
        String[] part2 = new String[trainDataValues.length / 10];

        System.arraycopy(trainDataValues, 0, part1, 0, part1.length);
        System.arraycopy(trainDataValues, part1.length, part2, 0, part2.length);

        trainDataValues = part1;
        testDataValues = part2;

        //===================WOOOP

        long startTime = System.currentTimeMillis();
        runIteration(numberOfTrees, trainDataValues, testDataValues, descriptor);
        long endTime = System.currentTimeMillis();
        double duration = new BigDecimal(endTime - startTime).divide(new BigDecimal("1000")).doubleValue();
        System.out.println(numberOfTrees + " took " + duration + " seconds");

    }

    private static String buildDescriptor(int numberOfFeatures) {
        StringBuilder builder = new StringBuilder("L ");
        for (int i = 0; i < numberOfFeatures; i++) {
            builder.append("N ");
        }
        return builder.toString();
    }

    private static void saveTree(int numberOfTrees, DecisionForest forest) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("saved-trees/" + numberOfTrees + "-trees-" + System.currentTimeMillis() + "-" + RandomUtils.getRandom().nextInt() + ".txt"));
        forest.write(dos);
    }

    public static void runIteration(int numberOfTrees, String[] trainDataValues, String[] testDataValues, String descriptor) throws DescriptorException, IOException {
        Data data = loadData(trainDataValues, descriptor);
        Random rng = RandomUtils.getRandom();

//        List<Node> trees = new ArrayList<Node>();
//
//        File savedTrees = new File("saved-trees");
//        File[] files = savedTrees.listFiles();
//
//        int filesUsed = 0;
//        for (File file : files) {
//            if(file.getName().startsWith("10-trees-threshold")) {
//                filesUsed++;
//                try {
//                    MultiDecisionForest forest = MultiDecisionForest.load(new Configuration(), new Path(file.getPath()));
//                    trees.addAll(forest.getTrees());
//                } catch(Exception e) {
//                    System.out.println("file.getPath() = " + file.getPath());
//                }
//
//            }
//        }
//
//        numberOfTrees = filesUsed;
//
//        MultiDecisionForest forest = new MultiDecisionForest(trees);

        DecisionForest forest = buildForest(numberOfTrees, data);
        saveTree(numberOfTrees, forest);

        Data test = DataLoader.loadData(data.getDataset(), testDataValues);

        try {
            FileWriter fileWriter = new FileWriter("attempts/out-" + System.currentTimeMillis() + ".txt");
            PrintWriter out = new PrintWriter(fileWriter);

            int numberCorrect = 0;
            int numberOfValues = 0;

            for (int i = 0; i < test.size(); i++) {
                Instance oneSample = test.get(i);
                double actualIndex = oneSample.get(0);
                int actualLabel = data.getDataset().valueOf(0, String.valueOf((int) actualIndex));

                double classify = forest.classify(test.getDataset(), rng, oneSample);
                int label = data.getDataset().valueOf(0, String.valueOf((int) classify));

//                System.out.println("label = " + label + " actual = " + actualLabel);

                if (label == actualLabel) {
                    numberCorrect++;
                }
                numberOfValues++;
                out.println(label + ", " + actualLabel);
            }

            double percentageCorrect = numberCorrect * 100.0 / numberOfValues;
            System.out.println("Number of trees: " + numberOfTrees + " -> Number correct: " + numberCorrect + " of " + numberOfValues + " (" + percentageCorrect + ")");

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static Data loadData(String[] sData, String descriptor) throws DescriptorException {
        Dataset dataset = DataLoader.generateDataset(descriptor, false, sData);
        return DataLoader.loadData(dataset, sData);
    }

    private static String[] testFileAsStringArray(String file) {
        ArrayList<String> list = new ArrayList<String>();

        int readSoFar = 0;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            br.readLine(); // discard top one
            while ((strLine = br.readLine()) != null) {
                list.add("-," + strLine);
                readSoFar++;
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
        return list.toArray(new String[list.size()]);
    }


}
