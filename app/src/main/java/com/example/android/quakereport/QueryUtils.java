package com.example.android.quakereport;

import android.provider.Telephony;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Created by mufeez on 4/7/2018.
 */

public final class  QueryUtils {
    private static  String JSONString ="";
    private static Earthquakes e;
    private QueryUtils(){}
    public static ArrayList<Earthquakes> getEarthquake (String stringUrl) {
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquakes> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            String sURL = stringUrl;
            URL url = createUrl(sURL);
            try{
            JSONString = makeHTTPRequest(url);}
            catch (IOException e){
                Log.e(LOG_TAG , "error HTTP request" , e);
            }
            if(JSONString != null){
            JSONObject root = new JSONObject(JSONString);

            JSONArray features = root.optJSONArray("features");
            for(int i =0 ; i<features.length() ; i++){
                JSONObject currentEarthquake = features.getJSONObject(i);
                JSONObject geo = currentEarthquake.getJSONObject("geometry");
                JSONArray cords = geo.getJSONArray("coordinates");
                 double lat = cords.getDouble(0);
                 double lng = cords.getDouble(1);

                JSONObject properties = currentEarthquake.getJSONObject("properties");
                earthquakes.add(new Earthquakes(properties.getDouble("mag") , properties.getString("place"), properties.getLong("time") , lng , lat , properties.getString("title")));


            }}

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;

    }
    private static URL createUrl(String sUrl){
        URL url =null;
        try {
            url = new URL(sUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }

            return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException{
        String JsonResponse="";
        if(url==null){
            return null;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode() != 200){
                Log.e(LOG_TAG , "Error response code : " + urlConnection.getResponseCode());
            }
            else{
                inputStream = urlConnection.getInputStream();
                JsonResponse = ReadStream(inputStream);
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG , "Error retrieving JSON" , e);
        }
        finally {
            if(inputStream!=null){
                inputStream=null;
            }
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
        }
        return JsonResponse;
    }

    private static String ReadStream(InputStream inputStream){
        StringBuilder Response = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader ISR = new InputStreamReader(inputStream , Charset.forName("UTF-8"));
            BufferedReader br =new BufferedReader(ISR);
            try{
            String line = br.readLine();
            while (line!=null){
                Response.append(line);
                line= br.readLine();
            }
            }
            catch (IOException e){
                Log.e(LOG_TAG, "Error reading stream " , e);
            }
        }
        return Response.toString();
    }
}
