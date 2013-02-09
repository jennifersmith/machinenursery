package main.java;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class WekaPlaybox {

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        FastVector attributes = attributes();

        Instances instances = new Instances("digit recognizer", attributes, 40000);
        instances.setClassIndex(0);

        String[] trainingDataValues = KaggleInputReader.fileAsStringArray("data/train.csv", 2000, false);

        long end = System.currentTimeMillis();
        System.out.println("data parsed: " + (end - start));

        for (String trainingDataValue : trainingDataValues) {
            Instance instance = createInstance(trainingDataValue);
            instances.add(instance);
        }

        Classifier classifier = buildClassifier(instances);

        end = System.currentTimeMillis();
        System.out.println("classifier built: " + (end - start));

        String[] testDataValues = KaggleInputReader.fileAsStringArray("data/train_tail.csv", 1000, false);

        int total = testDataValues.length;
        int numberCorrect = 0;
        FileWriter fileWriter = new FileWriter("weka-attempts/out-" + System.currentTimeMillis() + ".txt");
        PrintWriter out = new PrintWriter(fileWriter);
        for (String testDataValue : testDataValues) {
            Iteration iterate = iterate(testDataValue, classifier, instances);
            if(iterate.correct()) {
                numberCorrect++;
            }
            out.println((int) iterate.getPrediction());
        }
        out.close();
        System.out.println("Number correct: " + numberCorrect);
        System.out.println("Total: " + total);
        System.out.println("Accuracy: " + new BigDecimal(numberCorrect).divide(new BigDecimal(total), BigDecimal.ROUND_HALF_DOWN, 3).doubleValue());

        end = System.currentTimeMillis();
        System.out.println("time: " + (end - start));
    }

    private static Classifier buildClassifier(Instances instances) throws Exception {
        RandomForest randomForest = new RandomForest();
        randomForest.setNumTrees(500);
        randomForest.buildClassifier(instances);
        return randomForest;

//        MultiBoostAB multiBoostAB = new MultiBoostAB();
//        multiBoostAB.setClassifier(randomForest);
//        multiBoostAB.buildClassifier(instances);
//        return multiBoostAB;

//        AdaBoostM1 adaBoostM1 = new AdaBoostM1();
//        adaBoostM1.setClassifier(randomForest);
//        adaBoostM1.buildClassifier(instances);
//        return adaBoostM1;
    }


    private static Iteration iterate(String testDataValue, Classifier classifier, Instances instances) throws Exception {
//        Instance predictMe = createTestDataBasedInstanceToPredict(testDataValue, instances);
        Instance predictMe = createTrainingDataBasedInstanceToPredict(testDataValue, instances);
        double prediction = classifier.classifyInstance(predictMe);

        return new Iteration(new Double(testDataValue.split(",")[0]), prediction);
    }

    private static Instance createTrainingDataBasedInstanceToPredict(String testDataValue, Instances instances) {
        String[] columns = testDataValue.split(",");
        Instance instance = new Instance(785);

        for (int i = 1; i < columns.length; i++) {
            instance.setValue(new Attribute("pixel" + (i-1), i), new Double(columns[i]));
        }

        instance.setDataset(instances);
        return instance;
    }

    private static Instance createTestDataBasedInstanceToPredict(String testDataValue, Instances instances) {
        String[] columns = testDataValue.split(",");
        Instance instance = new Instance(785);

        for (int i = 0; i < columns.length; i++) {
            instance.setValue(new Attribute("pixel" + i, i+1), new Double(columns[i]));
        }

        instance.setDataset(instances);
        return instance;
    }



    private static Instance createInstance(String trainingDataValue) {
        String[] columns = trainingDataValue.split(",");

        Instance instance = new Instance(785);
        instance.setValue(digit(), columns[0]);

        for (int i = 1; i < columns.length; i++) {
            instance.setValue(new Attribute("pixel" + (i-1), i), new Double(columns[i]));
        }

        return instance;
    }

    private static FastVector attributes() {
        FastVector attributes = new FastVector();
        attributes.addElement(digit());

        for (int i = 0; i <= 783; i++) {
            attributes.addElement(new Attribute("pixel" + i));
        }

        return attributes;
    }


//    public static void main(String[] args) throws Exception {
//        FastVector attributes = new FastVector();
//        attributes.addElement(new Attribute("pixel1"));
//        attributes.addElement(new Attribute("pixel2"));
//        attributes.addElement(new Attribute("pixel3"));
//        attributes.addElement(digit());
//
//
//        Instances instances = new Instances("digit recognizer", attributes, 10);
//        instances.setClassIndex(instances.numAttributes() - 1);
//
//        instances.add(createInstance(99, 0, 0, "1"));
//        instances.add(createInstance(0, 99, 0, "2"));
//
//        RandomForest randomForest = new RandomForest();
//        randomForest.buildClassifier(instances);
//
//
//        System.out.println("prediction = " + randomForest.classifyInstance(createTrainingDataBasedInstanceToPredict(0, 98, 0, instances)));
//        System.out.println("prediction = " + randomForest.classifyInstance(createTrainingDataBasedInstanceToPredict(99, 0, 0, instances)));
//        System.out.println("prediction = " + randomForest.classifyInstance(createTrainingDataBasedInstanceToPredict(0, 0, 99, instances)));
//    }

    private static Attribute digit() {
        FastVector possibleClasses = new FastVector(10);
        possibleClasses.addElement("0");
        possibleClasses.addElement("1");
        possibleClasses.addElement("2");
        possibleClasses.addElement("3");
        possibleClasses.addElement("4");
        possibleClasses.addElement("5");
        possibleClasses.addElement("6");
        possibleClasses.addElement("7");
        possibleClasses.addElement("8");
        possibleClasses.addElement("9");
        return new Attribute("label", possibleClasses, 0);

    }

    private static Instance createInstanceToPredict(int pixel1, int pixel2, int pixel3, Instances instances) {
        Instance instance = new Instance(3);
        instance.setValue(new Attribute("pixel1", 0), pixel1);
        instance.setValue(new Attribute("pixel2", 1), pixel2);
        instance.setValue(new Attribute("pixel3", 2), pixel3);
        instance.setDataset(instances);
        return instance;
    }

    private static Instance createInstance(int pixel1, int pixel2, int pixel3, String klass) {
        Instance instance = new Instance(4);
        instance.setValue(new Attribute("pixel1", 0), pixel1);
        instance.setValue(new Attribute("pixel2", 1), pixel2);
        instance.setValue(new Attribute("pixel3", 2), pixel3);
        instance.setValue(digit(), klass);
        return instance;
    }
}
