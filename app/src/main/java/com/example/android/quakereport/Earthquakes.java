package com.example.android.quakereport;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mufeez on 4/7/2018.
 */

public class Earthquakes  {
    private double mag;
    private String primaryPlace;
    private String loc;
    private String place;
    private Date date;
    private String displayDate;
    private String displayTime;
    private double mLat;
    private double mLng;
    private String mTitle;
    public Earthquakes  (double magnitude , String city , long dateOccured , double lat , double lng , String title) {
        String country[] = new String[] {"check","made"};
        mag = magnitude;
        mLat=lat;
        mLng=lng;
        mTitle = title;
        primaryPlace = city;
        int x = primaryPlace.indexOf("of");
        if(x!=-1){
          country  =primaryPlace.split("of ");
               place = country[1];
                loc = country[0] + "of ";
        }
        else {
            loc = "Near the";
            place = city;
        }
        date =  new Date(dateOccured);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d,yyyy");
         displayDate = dateFormat.format(date);
         dateFormat = new SimpleDateFormat("h:mm a");
         displayTime = dateFormat.format(date);
    }
    public String getMagAsString(){
        return Double.toString(mag);
    }
    public Double getMag(){return mag;}
    public String getPlace(){return primaryPlace = place;}
    public String getDate () {return displayDate;}
    public String getTime(){return displayTime;}
    public String getLoc () {return loc;}
    public String getPrimaryPlace(){return primaryPlace;}
    public double getmLat() {
        return mLat;
    }

    public double getmLng() {
        return mLng;
    }
    public String getmTitle(){
        return mTitle;
    }
}
