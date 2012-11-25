package main.java;


public class Iteration {

    private double actual;
    private double prediction;

    public Iteration(double actual, double prediction) {
        this.actual = actual;
        this.prediction = prediction;
    }

    public boolean correct() {
        return actual == prediction;
    }

    public double getActual() {
        return actual;
    }

    public double getPrediction() {
        return prediction;
    }
}
