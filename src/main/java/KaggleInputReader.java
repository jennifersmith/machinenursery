package main.java;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class KaggleInputReader {
    public static String[] fileAsStringArray(String file, int numberToRead, List<Integer> pixelsToIgnore) {
        ArrayList<String> list = new ArrayList<String>();

        int readSoFar = 0;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            br.readLine(); // discard top one
            while ((strLine = br.readLine()) != null && numberToRead > readSoFar) {

                if(!pixelsToIgnore.contains(readSoFar)) {
                    list.add(strLine);
                }

                readSoFar++;
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
        return list.toArray(new String[list.size()]);
    }

}
