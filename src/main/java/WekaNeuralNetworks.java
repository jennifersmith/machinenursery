package main.java;


import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;

public class WekaNeuralNetworks {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        int numberToRead = 1000;
        String[] trainingDataValues = KaggleInputReader.fileAsStringArray("data/train_head.csv", numberToRead, new ArrayList<Integer>());
        FastVector attributes = attributes();

        Instances instances = new Instances("digit recognizer", attributes, numberToRead);
        instances.setClassIndex(0);

        for (String trainingDataValue : trainingDataValues) {
            Instance instance = createInstance(trainingDataValue);
            instances.add(instance);
        }

        Classifier multilayerPerceptron = buildClassifier(instances, "weka-attempts/neural-networks", "17");

        String[] testDataValues = KaggleInputReader.fileAsStringArray("data/train_tail.csv", 1000, new ArrayList<Integer>());

        int total = testDataValues.length;
        int numberCorrect = 0;
        FileWriter fileWriter = new FileWriter("weka-attempts/out-" + System.currentTimeMillis() + ".txt");
        PrintWriter out = new PrintWriter(fileWriter);
        for (String testDataValue : testDataValues) {
            Iteration iterate = iterate(testDataValue, multilayerPerceptron, instances);
            if(iterate.correct()) {
                numberCorrect++;
            }
            out.println((int) iterate.getPrediction());
            System.out.println("Actual: " + iterate.getActual() + ", Prediction: " + iterate.getPrediction());
        }
        out.close();
        System.out.println("Number correct: " + numberCorrect);
        System.out.println("Total: " + total);
        System.out.println("Accuracy: " + new BigDecimal(numberCorrect).divide(new BigDecimal(total), BigDecimal.ROUND_HALF_DOWN, 3).doubleValue());

        long end = System.currentTimeMillis();

        System.out.println("time: " + (end - start));
    }

    private static Classifier buildClassifier(Instances instances, String path, String layers) throws Exception {
        MultilayerPerceptron classifier = new MultilayerPerceptron();
//        classifier.setDecay(true);
//        classifier.setLearningRate(0.5);
//        classifier.setGUI(true);
        classifier.setHiddenLayers(layers);
        classifier.buildClassifier(instances);
        Debug.saveToFile(path, classifier);

//        SerializedClassifier classifier = new SerializedClassifier();
//        classifier.setModelFile(new File(path));

        return classifier;
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
            Double val = new Double(columns[i]);
            if(val >= 128){
                val = Double.valueOf(1);
            } else{
                val = Double.valueOf(0);
            }
            instance.setValue(new Attribute("pixel" + (i-1), i), val);
        }

        instance.setDataset(instances);
        return instance;
    }

    private static Instance createInstance(String trainingDataValue) {
        String[] columns = trainingDataValue.split(",");

        Instance instance = new Instance(785);
        instance.setValue(digit(), columns[0]);

        for (int i = 1; i < columns.length; i++) {
            Double val = new Double(columns[i]);
            if(val >= 128){
                val = Double.valueOf(1);
            } else{
                val = Double.valueOf(0);
            }
            instance.setValue(new Attribute("pixel" + (i-1), i), val);
        }

        return instance;
    }

    private static FastVector attributes() {
        FastVector attributes = new FastVector();
        attributes.addElement(
                digit());

        for (int i = 0; i <= 783; i++) {
            attributes.addElement(new Attribute("pixel" + i));
        }

        return attributes;
    }

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
}
