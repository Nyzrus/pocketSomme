package com.example.surzyn.pocketsomme;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;


public class userHome extends ActionBarActivity {

    Button searchButton, favoriteButton, historyButton;
    Intent sActivity, fActivity, hActivity;
    TextView exp, varietalTV;
    historyAdapter ha;
    wineAdapter wa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        ha = new historyAdapter(this);
        wa = new wineAdapter(this);

        searchButton = (Button) findViewById(R.id.searchWinesButton);
        favoriteButton = (Button) findViewById(R.id.favoriteButton);
        historyButton = (Button) findViewById(R.id.historyButton);
        sActivity = new Intent(this, Search.class);
        fActivity = new Intent(this, Favorites.class);

        exp = (TextView) findViewById(R.id.historyTV);
        exp.setText("Wines: " + ha.getCount());

        varietalTV = (TextView) findViewById(R.id.varietalTV);
        if(getFavoriteVarietal()==null){
            varietalTV.setText("Varietal : None");
        }else{
            varietalTV.setText("Varietal : " + getFavoriteVarietal());
        }


        hActivity = new Intent(this, History.class);
    }

    public String aggregateTaste(){

        for(int i = 0; i < ha.getCount(); i++){

        }
        return null;
    }

    public String getFavoriteVarietal(){
        return wa.alternateMethod();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home, menu);
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

    public void toSearch(View v){
        startActivity(sActivity);
    }
    public void toFavorites(View v){
        startActivity(fActivity);
    }
    public void toHistory(View v){
        startActivity(hActivity);
    }
}
