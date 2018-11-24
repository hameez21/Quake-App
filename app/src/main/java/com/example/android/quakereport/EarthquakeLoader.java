package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mufeez on 4/17/2018.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquakes>> {
    private String mUrl;

    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param Url to load data from
     */
    public EarthquakeLoader(Context context , String Url) {
        super(context);
        mUrl=Url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public ArrayList<Earthquakes> loadInBackground() {


        // Perform the network request, parse the response, and extract a list of earthquakes.
        ArrayList<Earthquakes> earthquakes = QueryUtils.getEarthquake(mUrl);
        return earthquakes;
    }
}
