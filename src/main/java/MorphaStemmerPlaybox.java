package main.java;

import edu.washington.cs.knowitall.morpha.MorphaStemmer;

/**
 * Created with IntelliJ IDEA.
 * User: mneedham
 * Date: 04/11/2012
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class MorphaStemmerPlaybox {
    public static void main(String[] args) {
        System.out.println("smiley = " + new MorphaStemmer().morpha("smiley", false));
        System.out.println("smile = " + new MorphaStemmer().morpha("smile", false));
        System.out.println("smiled = " + new MorphaStemmer().morpha("smiled", false));

        System.out.println("positivity = " + new MorphaStemmer().morpha("positivity", false));
        System.out.println("positive = " + new MorphaStemmer().morpha("positive", false));
    }
}
