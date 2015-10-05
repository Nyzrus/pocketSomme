package com.example.surzyn.pocketsomme;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WineMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback{

    private GoogleMap mMap;
    Geocoder gc = null;
    List<Address> addressList;
    StringBuffer locationName;
    ArrayList<String> locs;
    ArrayList<String> names;
    private LatLng latLng;
    Intent i;


    private LatLng myLocation;
    double lat = 42.6556, lng = -70.6208;
    MapFragment mf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_map);

        locationName = new StringBuffer();

        addressList = new ArrayList<Address>();
        gc = new Geocoder(this);
        myLocation = new LatLng(47.05,4.38);
        //Bundle ex = getIntent().getExtras();
        //i = getIntent();






        mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mf.getMapAsync(this);

        setUpMapIfNeeded();

        //new GeocoderTask().execute();

        retrieveCities("london");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onMapLoaded() {
        // code to run when the map has loaded

        // read user's current location, if possible
        // try to get location three ways: GPS, cell/wifi network, and 'passive' mode
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 5));
        mMap.setMyLocationEnabled(true);

        if (loc == null) {
            // fall back to network if GPS is not available
            loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (loc == null) {
            // fall back to "passive" location if GPS and network are not available
            loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        if (loc == null) {
            myLocation = new LatLng(lat, lng);
            Toast.makeText(this, "Unable to access your location. Consider enabling Location in your device's Settings.", Toast.LENGTH_LONG).show();
        } else {
            double myLat = loc.getLatitude();
            double myLng = loc.getLongitude();
            myLocation = new LatLng(myLat, myLng);
        }
        //theMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 5));
        //theMap.setMyLocationEnabled(true);

    }

    @Override
    public void onMapReady(GoogleMap map) {    // map is loaded but not laid out yet
        this.mMap = map;

        mMap.setOnMapLoadedCallback(this);      // calls onMapLoaded when layout done
        UiSettings mapSettings;
        mapSettings = mMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = mf.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                //Zooming Buttons
                UiSettings mapSettings;
                mapSettings = mMap.getUiSettings();
                mapSettings.setZoomControlsEnabled(true);
            }
        }

    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        List<Address> addresses;

        @Override
        protected List<Address> doInBackground(String... params) {
            Geocoder geocoder = new Geocoder(getBaseContext());
            //List<Address> addresses = null;

            try{
                addresses = geocoder.getFromLocationName("London", 3);
            }catch(IOException e){
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses){

            double lat = addresses.get(0).getLatitude();
            double lng = addresses.get(0).getLongitude();
            //mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("test"));
        }
    }


    private void retrieveCities(String string){
        try {
            addressList = gc.getFromLocationName(string, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addressList != null && addressList.size() > 0){
            double lat = addressList.get(0).getLatitude();
            double lng = addressList.get(0).getLongitude();
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("test"));
        }

        mMap.addMarker(new MarkerOptions().position(new LatLng(47.05,4.38)).title("Burgundy"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(44.83,-0.57)).title("Bordeaux"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(38.42,-122.39)).title("Napa Valley"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(49.925992, 7.96339)).title("Rhine Valley"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(43.771051, 11.248621)).title("Tuscany"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.026628, 0.333235)).title("Champagne"));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(14.524307, -60.864951)).title("Beaujolais"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(45.984648, 4.052545)).title("Loire Valley"));




    }

    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 1));

        try {
            addressList = gc.getFromLocationName("London", 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addressList != null && addressList.size() > 0){
            double lat = addressList.get(0).getLatitude();
            double lng = addressList.get(0).getLongitude();
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("test"));
        }

        //mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("test"));
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wine_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
