package main.java;

import org.apache.hadoop.util.StringUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KaggleInputReader {
    public static String[] fileAsStringArray(String file, int numberToRead) {
        ArrayList<String> list = new ArrayList<String>();

        int readSoFar = 0;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            br.readLine(); // discard top one
            while ((strLine = br.readLine()) != null && numberToRead > readSoFar) {
                list.add(strLine);
                readSoFar++;
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
        return list.toArray(new String[list.size()]);
    }


    public static String[] fileAsStringArray(String file, int numberToRead, List<Integer> pixelsToIgnore) {
        ArrayList<String> list = new ArrayList<String>();
        boolean threshold = false;
        int readSoFar = 0;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            br.readLine(); // discard top one
            while ((strLine = br.readLine()) != null && numberToRead > readSoFar) {
                List<String> rowBuilder = new ArrayList<String>();

                String[] row = strLine.split(",");
                rowBuilder.add(row[0]);

                for (int i = 1; i < row.length; i++) {
                    if (!pixelsToIgnore.contains(i)) {
                        int pixelValue = Integer.parseInt(row[i]);
                        if (threshold) {
                            if (pixelValue < 127) {
                                pixelValue = 0;
                            } else {
                                pixelValue = 1;
                            }
                        }
                        rowBuilder.add(String.valueOf(pixelValue));
                    }
                }

                list.add(StringUtils.join(",", rowBuilder));
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
