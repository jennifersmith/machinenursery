package main.java;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.folded.FoldedDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.cross.CrossValidationKFold;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import java.math.BigDecimal;


public class ManualEncogPlaybox {
    public final static double MAX_ERROR = 0.10;

    public static void main(String[] args) {
        BasicNetwork network = createNetwork();

        String[] trainingData = KaggleInputReader.fileAsStringArray("data/train_head.csv");

        final MLDataSet training = new BasicMLDataSet();
        for (String row : trainingData) {
            double[] actualValue = asOnePerClassCoding(new Double(row.split(",")[0]));
            double[] inputs = inputs(row.split(","));
            training.add(new BasicMLDataPair(new BasicMLData(inputs), new BasicMLData(actualValue)));
        }

        train(network, training);

        String[] testData = KaggleInputReader.fileAsStringArray("data/train_tail.csv");

        int total = testData.length;
        int correct = 0;
        for (String row : testData) {
            Iteration iterate = iterate(row, network);

            if(iterate.correct()) {
                correct++;
            }

            System.out.println("Actual: " + iterate.getActual() + ", Prediction: " + iterate.getPrediction());
        }

        System.out.println("accuracy: " + new BigDecimal(correct).divide(new BigDecimal(total)));
    }

    private static Iteration iterate(String testData, BasicNetwork network) {
        double[] inputs = inputs(testData.split(","));
        int prediction = prediction(network, inputs);
        int actual = new Integer(testData.split(",")[0]);
        return new Iteration(new Double(actual), prediction);
    }

    private static double[] inputs(String[] columns) {
        double[] inputs = new double[columns.length-1];
        for (int i = 1; i < columns.length; i++) {
            inputs[i-1] = new Double(columns[i]);
        }

        return inputs;
    }

    private static double[] asOnePerClassCoding(Double label) {
        double[] classes = new double[] { 0,0,0,0,0,0,0,0,0,0 };
        for (int i = 0; i < 10; i++) {
            if(label.intValue() == i) {
                classes[label.intValue()] = 1;
            }

        }
        return classes;
    }

    private static int prediction(BasicNetwork network, double[] input) {
        MLData output = network.compute(new BasicMLData(input));

        double winningOutput = Double.NEGATIVE_INFINITY;
        int winningClass = Integer.MIN_VALUE;

        for(int i=0;i<output.size();i++)
        {
            // evaluate if this is a "winning" direction
            double thisOutput = output.getData(i);
            if( thisOutput>winningOutput)
            {
                winningOutput = thisOutput;
                winningClass = i;
            }
        }
        return winningClass;
    }

    public static void train(BasicNetwork network, MLDataSet training) {
        final FoldedDataSet folded = new FoldedDataSet(training);
        final MLTrain train = new ResilientPropagation(network, folded);
        final CrossValidationKFold trainFolded = new CrossValidationKFold(train, 4);

        int epoch = 1;

        do {
            trainFolded.iteration();
            System.out
                    .println("Epoch #" + epoch + " Error:" + trainFolded.getError());
            epoch++;
        } while (trainFolded.getError() > MAX_ERROR);
    }

    private static BasicNetwork createNetwork() {
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(3));
        network.addLayer(new BasicLayer(10));
        network.addLayer(new BasicLayer(10));
        network.getStructure().finalizeStructure();
        network.reset();
        return network;
    }
}