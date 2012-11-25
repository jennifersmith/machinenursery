package main.java;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.clustercopy.SOMClusterCopyTraining;


public class EncogPlaybox {
    public static void main(String[] args) {
        // this is the total number of inputs
        final int inputNeuron = 28 * 28;

        // this is the number of values that we train on in the OCR example...but it should be the number of classes
        final int outputNeuron = 9;

        final MLDataSet trainingSet = new BasicMLDataSet();
        for (int t = 0; t < 2; t++) {
            final MLData item = new BasicMLData(inputNeuron);
            item.setData(0, 255);
            trainingSet.add(new BasicMLDataPair(item, null));
        }

        SOM neuralNetwork = new SOM(inputNeuron, outputNeuron);
        neuralNetwork.reset();

        SOMClusterCopyTraining train = new SOMClusterCopyTraining(neuralNetwork, trainingSet);

        train.iteration();

        final MLData input = new BasicMLData(inputNeuron);
        // set the pixel brightness values for the new thing
        input.setData(0, 10);

        final int best = neuralNetwork.classify(input);
//        final char map[] = mapNeurons();

    }

//    char[] mapNeurons() {
//        final char map[] = new char[this.letterListModel.size()];
//
//        for (int i = 0; i < map.length; i++) {
//            map[i] = '?';
//        }
//        for (int i = 0; i < this.letterListModel.size(); i++) {
//            final MLData input = new BasicMLData(5 * 7);
//            int idx = 0;
//            final SampleData ds = (SampleData) this.letterListModel.getElementAt(i);
//            for (int y = 0; y < ds.getHeight(); y++) {
//                for (int x = 0; x < ds.getWidth(); x++) {
//                    input.setData(idx++, ds.getData(x, y) ? .5 : -.5);
//                }
//            }
//
//            final int best = this.net.classify(input);
//            map[best] = ds.getLetter();
//        }
//        return map;
//    }
}
