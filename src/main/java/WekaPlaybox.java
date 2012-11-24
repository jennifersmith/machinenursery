package main.java;

import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


public class WekaPlaybox {

    public static void main(String[] args) throws Exception {
        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute("pixel1"));
        attributes.addElement(new Attribute("pixel2"));
        attributes.addElement(new Attribute("pixel3"));
        attributes.addElement(digit());


        Instances instances = new Instances("digit recognizer", attributes, 10);
        instances.setClassIndex(instances.numAttributes() - 1);

        instances.add(createInstance(99, 0, 0, "1"));
        instances.add(createInstance(0, 99, 0, "2"));

        RandomForest randomForest = new RandomForest();
        randomForest.buildClassifier(instances);


        System.out.println("prediction = " + randomForest.classifyInstance(createInstanceToPredict(0, 98, 0, instances)));
        System.out.println("prediction = " + randomForest.classifyInstance(createInstanceToPredict(99, 0, 0, instances)));
        System.out.println("prediction = " + randomForest.classifyInstance(createInstanceToPredict(0, 0, 99, instances)));


    }

    private static Attribute digit() {
        FastVector possibleClasses = new FastVector(3);
        possibleClasses.addElement("1");
        possibleClasses.addElement("2");
        possibleClasses.addElement("3");
        return new Attribute("digit", possibleClasses, 3);

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
