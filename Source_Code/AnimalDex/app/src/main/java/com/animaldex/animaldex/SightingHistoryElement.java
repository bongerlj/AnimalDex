package com.animaldex.animaldex;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Josh Voskamp on 3/31/2016.
 */
public class SightingHistoryElement implements Parcelable {
    private final int month;
    private final int day;
    private final int year;
    private final String animal;
    private final LatLng coordinate;

    private final String FILEPATH;

    public SightingHistoryElement(int month, int day, int year, String animal, LatLng coordinate,String filepath) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.animal = animal;
        this.coordinate = coordinate;
        this.FILEPATH = filepath;
    }

    protected SightingHistoryElement(Parcel in) {
        month = in.readInt();
        day = in.readInt();
        year = in.readInt();
        animal = in.readString();
        coordinate = in.readParcelable(LatLng.class.getClassLoader());
        FILEPATH = in.readString();
    }

    public static final Creator<SightingHistoryElement> CREATOR = new Creator<SightingHistoryElement>() {
        @Override
        public SightingHistoryElement createFromParcel(Parcel in) {
            return new SightingHistoryElement(in);
        }

        @Override
        public SightingHistoryElement[] newArray(int size) {
            return new SightingHistoryElement[size];
        }
    };

    @Override
    public String toString(){
        return "Spotted: " + animal + " On: "+ month + "/" + day + "/" + year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getAnimal() {
        return animal;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public String getFILEPATH() {
        return FILEPATH;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(year);
        dest.writeString(animal);
        dest.writeParcelable(coordinate, flags);
        dest.writeString(FILEPATH);
    }
}
