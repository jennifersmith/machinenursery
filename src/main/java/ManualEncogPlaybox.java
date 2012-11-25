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


public class ManualEncogPlaybox {
    public final static double MAX_ERROR = 0.70;

    public static void main(String[] args) {
        BasicNetwork network = createNetwork();

        final MLDataSet training = new BasicMLDataSet();
        training.add(new BasicMLDataPair(new BasicMLData(new double[] { 99, 0, 0 }), new BasicMLData(new double[] {1,0,0,0,0,0,0,0,0,0}))); // 0
        training.add(new BasicMLDataPair(new BasicMLData(new double[] { 0, 99, 0 }), new BasicMLData(new double[] {0,1,0,0,0,0,0,0,0,0}))); // 1
        training.add(new BasicMLDataPair(new BasicMLData(new double[] { 0, 0, 99 }), new BasicMLData(new double[] {0,0,1,0,0,0,0,0,0,0}))); // 2

        train(network, training);

        int winningOutput = prediction(network, new double[]{0, 0, 99});

        System.out.println("prediction: " + winningOutput);

        System.out.println("network = " + network);
    }

    private static int prediction(BasicNetwork network, double[] input) {
        MLData output = network.compute(new BasicMLData(input));

        double winningOutput = Double.NEGATIVE_INFINITY;

        for(int i=0;i<output.size();i++)
        {
            // evaluate if this is a "winning" direction
            double thisOutput = output.getData(i);
            if( thisOutput>winningOutput)
            {
                winningOutput = i;
            }
        }
        return (int)winningOutput;
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
        network.addLayer(new BasicLayer(60));
        network.addLayer(new BasicLayer(10));
        network.getStructure().finalizeStructure();
        network.reset();
        return network;
    }

//    public void predict(BasicNetwork network) {
//        NumberFormat f = NumberFormat.getNumberInstance();
//        f.setMaximumFractionDigits(4);
//        f.setMinimumFractionDigits(4);
//
//        System.out.println("Year\tActual\tPredict\tClosed Loop Predict");
//
//        for (int year = EVALUATE_START; year < EVALUATE_END; year++) {
//            // calculate based on actual data
//            MLData input = new BasicMLData(WINDOW_SIZE);
//            for (int i = 0; i < input.size(); i++) {
//                input.setData(i, this.normalizedSunspots[(year - WINDOW_SIZE)
//                        + i]);
//            }
//            MLData output = network.compute(input);
//            double prediction = output.getData(0);
//            this.closedLoopSunspots[year] = prediction;
//
//            // calculate "closed loop", based on predicted data
//            for (int i = 0; i < input.size(); i++) {
//                input.setData(i, this.closedLoopSunspots[(year - WINDOW_SIZE)
//                        + i]);
//            }
//            output = network.compute(input);
//            double closedLoopPrediction = output.getData(0);
//
//            // display
//            System.out.println((STARTING_YEAR + year) + "\t"
//                    + f.format(this.normalizedSunspots[year]) + "\t"
//                    + f.format(prediction) + "\t"
//                    + f.format(closedLoopPrediction));
//
//        }
//    }

}
