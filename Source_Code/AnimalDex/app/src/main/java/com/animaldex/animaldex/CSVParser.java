package com.animaldex.animaldex;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Alex on 2016-03-29.
 */
public class CSVParser {

    //Parses a CSV file into an ArrayList of String arrays, where each array contains a line of the CSV file.
    public static ArrayList<String[]> parse(String filePath){

        //Create empty ArrayList to hold results
        ArrayList<String[]> outputList = new ArrayList<String[]>();

        try {
            //Create BufferedReader to read CSV
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            //Crate String to temporarily hold each line read from the file, and get the first line of the file
            String inputLine = br.readLine();

            //Keep going until the end of the file is reached
            while(inputLine != null) {

                //Split the line by comma into an array, and add the array into the ArrayList
                outputList.add(inputLine.split(","));

                //Get the next line of the file
                inputLine = br.readLine();
            }
            br.close();

        }catch(Exception ex){
                Log.e("CSVParser", "File " + filePath + "could not be parsed." + ex.getMessage());
            }

        return outputList;
    }
}
