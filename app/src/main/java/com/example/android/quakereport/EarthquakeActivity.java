/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks <ArrayList<Earthquakes>>{
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        TextView  emptyView= findViewById(R.id.empty_view);
        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        emptyView.setVisibility(View.GONE);


        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,R.color.magnitude9);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TextView emptyView = findViewById(R.id.empty_view);
                emptyView.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(true);
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(0,null,EarthquakeActivity.this);
            }
        });

    }
public void UpdateUi (final ArrayList<Earthquakes> earthquakes){
        // Find a reference to the {@link ListView} in the layout

        ListView earthquakeListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdapter adapter = new EarthquakeAdapter(EarthquakeActivity.this ,earthquakes);
 //SETS CLICK LISTENER TO LISTITEMS
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquakes current = earthquakes.get(position);
                double lat = current.getmLat();
                double lng = current.getmLng();

                Intent i = new Intent(EarthquakeActivity.this , EarthquakeDetails.class);
                i.putExtra("lat" , lat);
                i.putExtra("lng" , lng);
                i.putExtra("title" , current.getPrimaryPlace());
                startActivity(i);
            }
        });
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
    if(earthquakes == null)
        earthquakeListView.setAdapter(null);
    else
        earthquakeListView.setAdapter(adapter);
    }
   /* public class EarthquakeAsync extends AsyncTask<Void , Void , ArrayList<Earthquakes>>{
        @Override
        protected ArrayList<Earthquakes> doInBackground(Void... voids) {
                return QueryUtils.getEarthquake();
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquakes> earthquakes) {
            UpdateUi(earthquakes);
        }
    }*/

   //SETS THE USER PREFERENCES IN THIS CASE FILTER EARTHQUAKES
    @Override
    public Loader<ArrayList<Earthquakes>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String limit = sharedPrefs.getString(getString(R.string.settings_limit_key),
                getString(R.string.settings_limit_default));
      //  Integer l = Integer.parseInt(limit);
/*if(l>50){
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString( "num earthquake" , "50");
            editor.apply();
            limit = ""+50;
        }*/
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", limit);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");
        uriBuilder.appendQueryParameter("starttime" , "2016-01-01");

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquakes>> loader, ArrayList<Earthquakes> data) {
        TextView emptyView = findViewById(R.id.empty_view);
        ProgressBar load = findViewById(R.id.load);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);
        if(!data.isEmpty()) {
            UpdateUi(data);
            load.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

        }
        else{
            boolean netState = isOnline();
            emptyView.setVisibility(View.VISIBLE);
            if(netState){
                emptyView.setText("No Earthquake data found");

            }
            else{
                emptyView.setText("No Internet Connection");

            }
            swipeRefreshLayout.setRefreshing(false);
            load.setVisibility(View.GONE);
            UpdateUi(null);
        }}

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquakes>> loader) {
        UpdateUi(null);
    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if(id==R.id.Sync){
            TextView emptyView = findViewById(R.id.empty_view);
            emptyView.setVisibility(View.GONE);
            ProgressBar load = findViewById(R.id.load);
            load.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(0,null,this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
