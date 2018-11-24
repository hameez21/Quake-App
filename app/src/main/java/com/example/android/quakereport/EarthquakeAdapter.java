package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mufeez on 4/7/2018.
 */
// THIS ADAPTER IS USED FOR OPTIMIZED LIST VIEW
public class EarthquakeAdapter extends ArrayAdapter<Earthquakes> {
    public EarthquakeAdapter (Activity context, ArrayList<Earthquakes> Earthquake ){
        super(context , 0 , Earthquake);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.layout_adapter,parent,false);}
        Earthquakes currentEarthquake = getItem(position);
        TextView mag = listView.findViewById(R.id.mag);
        TextView place = listView.findViewById(R.id.place);
        TextView date = listView.findViewById(R.id.date);
        TextView time = listView.findViewById(R.id.time);
        TextView loc = listView.findViewById(R.id.loc);
        mag.setText(currentEarthquake.getMagAsString());
        place.setText(currentEarthquake.getPlace());
        date.setText(currentEarthquake.getDate());
        time.setText(currentEarthquake.getTime());
        loc.setText(currentEarthquake.getLoc());
        GradientDrawable magColor = (GradientDrawable) mag.getBackground();
        magColor.setColor(getMagColor(currentEarthquake.getMag()));
        return listView;  //RETURNS LIST VIEW TO THE ACTIVTY
    }
    //SELECTS COLOR DEPENDING ON MAGNITUDE
    public int getMagColor(Double mag){
        int magi = mag.intValue();
        int magnitudeColorResourceId;
        switch(magi){
              case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }



}
