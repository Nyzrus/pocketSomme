package com.example.surzyn.pocketsomme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Search extends ActionBarActivity {


    String url;                 //url to access API
    String jsonString;          //String that will hold JSON encoding
    String s;                   //s will be used during the JSON task to hold info during the ASYNC process
    EditText wineryET;          //the serch bar EditText
    Intent i, h;                //i will hold the intent to get back to the searchResults Activity, h the landing page
    Intent historyIntent, favoritesIntent;
    ArrayList<String> wineList;             //wineList will hold the names
    ArrayList<String> regionList;           //regionList will hold the location
    ArrayList<String> vineyardList;         //vineyardList will hold the vineyard name
    ArrayList<String> varietalList;         //varietalList will hold the varietal name
    ArrayList<String> ratingList;           //ratingList will hold the API-ordained wine rating
    ArrayList<String> linkList;             //linkList will hold the url to each wine on the API-provider's main site

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        s = "";                                                 //Initialize all elements
        wineList = new ArrayList<String>();
        regionList = new ArrayList<String>();
        vineyardList = new ArrayList<String>();
        varietalList = new ArrayList<String>();
        ratingList = new ArrayList<String>();
        linkList = new ArrayList<String>();
        wineryET = (EditText) findViewById(R.id.wineryET);
        i = new Intent(this, SearchResults.class);
        h = new Intent(this, userHome.class);
        historyIntent = new Intent(this, History.class);
        favoritesIntent = new Intent(this, Favorites.class);

        url = "";

        //the url will be constructed by urlMaker where a base url is concatenated with an individual API-key
        //allowing access to the contents of the API


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {     //if the user hits the back button, return to the home screen
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(h);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class jsonTask extends AsyncTask<String, Void, String> {

        String q;           //The scraping of data from the API site is run asynchronously so as not to slow down
                            // the entire application or GUI operations
        @Override
        protected String doInBackground(String... params) {
            jsonString = getData();                             //runs getData() which returns the raw JSON-formatted string
            try{                                                //from the API
                JSONObject obj = new JSONObject(jsonString);
                JSONArray wines = obj.getJSONArray("wines");
                for(int i = 0; i < wines.length();i++){                     //goes through and parses useful data into corresponding ArrayLists
                    wineList.add(wines.getJSONObject(i).getString("name"));
                    String tempVineyard = wines.getJSONObject(i).getString("winery");
                    String tempRegion = wines.getJSONObject(i).getString("region");
                    linkList.add(wines.getJSONObject(i).getString("link"));
                    varietalList.add(wines.getJSONObject(i).getString("varietal"));
                    ratingList.add(wines.getJSONObject(i).getString("snoothrank"));
                    vineyardList.add(tempVineyard +", " + tempRegion);
                }

                for(int k = 0; k < wineList.size(); k++){
                    s += wineList.get(k) +": " + varietalList.get(k)+",\n";
                }
            }catch(JSONException e){

                //t.setText("error");
            }

            return s;
        }

        @Override
        protected void onPostExecute(String params){
            setResults(s);
            s = "";
            //return s;
        }
    }

    public void setResults(String thewines){
        s = "";                                                 //this method packages all data for use in the next activity
        i.putExtra("query", wineryET.getText().toString());
        i.putStringArrayListExtra("wines", wineList);
        i.putStringArrayListExtra("vineyards", vineyardList);
        i.putStringArrayListExtra("varietals", varietalList);
        i.putStringArrayListExtra("ratings", ratingList);
        i.putStringArrayListExtra("links", linkList);
        startActivity(i);                                       //starts searchResults.java
    }

    public String getData(){

        StringBuilder builder = new StringBuilder();        //StringBuilder instead of String because it will be modified
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try{
            HttpResponse response = client.execute(httpGet);    //Verifies network connectivity via HTTP response call
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader((new InputStreamReader(content)));       //Reads in data through
                String line;                                                                        //buffered stream
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }

            }else{
                Log.d("Surzyn", "Status : " + statusCode);
            }

        } catch (ClientProtocolException e) {
            Log.d("sada", e.toString());
        } catch (IOException e) {
            Log.d("sada", e.toString());
        }
        return builder.toString();
    }

    public void check(View v) {

        //urlMaker is a custom method that constructs a url with the API-key and the user's query

        urlMaker u = new urlMaker(wineryET.getText().toString());
        url = u.getUrl();
        new jsonTask().execute();           //starts json task


    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        populateMenu(menu);

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        populateMenu(menu);
        return super.onCreateOptionsMenu(menu);

    }
    public void populateMenu(Menu menu)
    {
        int groupId = 0;            //create menu options for favorite/history insertion
        int order = 0;
        menu.add(groupId, 1, ++order, "History");
        menu.add(groupId, 2, ++order, "Favorites");

    }

    public boolean onContextItemSelected(MenuItem item) {
        return (applyMenuOption(item));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return (applyMenuOption(item));
    }

    public boolean applyMenuOption(MenuItem item) {
        int itemid = item.getItemId();

        if (itemid == 1) {
            startActivity(historyIntent);
        } else if (itemid == 2) {
            startActivity(favoritesIntent);
        }

        return true;
    }

}
