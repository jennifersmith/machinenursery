package main.java;

import org.apache.mahout.classifier.df.DecisionForest;
import org.apache.mahout.classifier.df.builder.DefaultTreeBuilder;
import org.apache.mahout.classifier.df.data.*;
import org.apache.mahout.classifier.df.ref.SequentialBuilder;
import org.apache.mahout.common.RandomUtils;
import org.uncommons.maths.Maths;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MahoutPlaybox {
    private static DecisionForest buildForest(int numberOfTrees, Data data) {
        int m = (int) Math.floor(Maths.log(2, data.getDataset().nbAttributes()) + 1);

        DefaultTreeBuilder treeBuilder = new DefaultTreeBuilder();
        SequentialBuilder forestBuilder = new SequentialBuilder(RandomUtils.getRandom(), treeBuilder, data.clone());
        treeBuilder.setM(m);

        return forestBuilder.build(numberOfTrees);
    }

    public static void foo (String[] args)throws IOException, DescriptorException {
        main(args);
    }
    public static void main(String[] args) throws IOException, DescriptorException {
        String descriptor = "L N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N N ";
        String[] trainDataValues = fileAsStringArray("data/train.csv");
        String[] testDataValues = testFileAsStringArray("data/test.csv");

        // take 90 percent to be the test data
//        String[] part1 = new String[trainDataValues.length / 10 * 9];
//        String[] part2 = new String[trainDataValues.length / 10];
//
//        System.arraycopy(trainDataValues, 0, part1, 0, part1.length);
//        System.arraycopy(trainDataValues, part1.length, part2, 0, part2.length);
//
//        trainDataValues = part1;
//        testDataValues = part2;

        //===================WOOOP

        List<Integer> potentialTrees = new ArrayList<Integer>();
//        potentialTrees.add(1);
//        potentialTrees.add(10);
        potentialTrees.add(200);
//        potentialTrees.add(1000);
//        potentialTrees.add(10000);


        for (int numberOfTrees : potentialTrees) {
            long startTime = System.nanoTime();
            runIteration(numberOfTrees, trainDataValues, testDataValues, descriptor);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            System.out.println(numberOfTrees + " took " + duration + " nano seconds");
        }

    }

    private static void saveTree(int numberOfTrees, DecisionForest forest) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("saved-trees/" + numberOfTrees + "-trees.txt"));
        forest.write(dos);
    }

    public static void runIteration(int numberOfTrees, String[] trainDataValues, String[] testDataValues, String descriptor) throws DescriptorException, IOException {
        Data data = loadData(trainDataValues, descriptor);
        Random rng = RandomUtils.getRandom();

//        DecisionForest forest = DecisionForest.load(new Configuration(), new Path("saved-trees/" + numberOfTrees + "-trees.txt"));
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

                if (label == actualLabel) {
                    numberCorrect++;
                }
                numberOfValues++;
                out.println(label + ", " + actualLabel);
            }

            System.out.println("Number of trees: " + numberOfTrees + " -> Number correct: " + numberCorrect + " of " + numberOfValues + " " + (numberCorrect * 1.0 / numberOfValues));

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


    public static String[] fileAsStringArray(String file) {
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
