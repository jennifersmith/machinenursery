package main.java;

import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodSingle;



public class SOMPlaybox {

    public static double SOM_INPUT[][] = {
            { 0, 255, 200, 0, 0 },
            { 200, 0, 0, 200, 200 },
            { 0, 200, 150, 0, 0 }  ,
            { 0, 200, 150, 0, 0 }
    };

    public static double SOM_OUTPUT[][] = {
            {1},
            {2},
            {1},
            {1}
    };

    public static void main(String args[])
    {
        // create the training set
        MLDataSet training = new BasicMLDataSet(SOM_INPUT, SOM_OUTPUT);

        // Create the neural network.
        SOM network = new SOM(5,2);
        network.reset();

        BasicTrainSOM train = new BasicTrainSOM(network, 0.7, training, new NeighborhoodSingle());

        for(int iteration = 0;iteration<=10;iteration++)
        {
            train.iteration();
            System.out.println("Iteration: " + iteration + ", Error:" + train.getError());
        }

        MLData data1 = new BasicMLData(SOM_INPUT[0]);
        MLData data2 = new BasicMLData(SOM_INPUT[1]);
        System.out.println("Pattern 1 winner: " + network.classify(data1));
        System.out.println("Pattern 2 winner: " + network.classify(data2));
        Encog.getInstance().shutdown();
    }
}
