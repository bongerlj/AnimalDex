package com.animaldex.animaldex;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Josh Voskamp on 3/30/2016.
 */
public class SightingHistoryParser {

    public static ArrayList<SightingHistoryElement> parseHistory(InputStream in) {
        Scanner input = new Scanner(in);

        ArrayList<SightingHistoryElement> history = new ArrayList<>();
        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] splits = line.split(",");
            //Not great efficiency but want to view the list backwards
            history.add(new SightingHistoryElement(Integer.parseInt(splits[3]),
                    Integer.parseInt(splits[4]),
                    Integer.parseInt(splits[5]),
                    splits[0],
                    new LatLng(Double.parseDouble(splits[1]),
                            Double.parseDouble(splits[2])),splits[6]));
                    Log.v("History Parser","File Path: "+splits[6]);
        }
        input.close();

        return history;
    }
}
