package com.example.surzyn.pocketsomme;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class Favorites extends ActionBarActivity {

    ListView l;
    wineAdapter w;
    int focus;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        l = (ListView) findViewById(R.id.favoritesList);
        w = new wineAdapter(this);
        l.setAdapter(w);
        i = new Intent(this, WineMap.class);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                focus = position;
            }
        });
    }

    public void addWine(View v){

        l.setAdapter(w);

    }

    public void clearList(View v){
        w.clearList();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }*/

    /*@Override
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
    }*/

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
        int groupId = 0;
        int order = 0;
        menu.add(groupId, 1, ++order, "Clear Favorites");
        menu.add(groupId, 2, ++order, "Map Favorites");


    }

    public boolean onContextItemSelected(MenuItem item) {
        return (applyMenuOption(item));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return (applyMenuOption(item));
    }

    public boolean applyMenuOption(MenuItem item) {
        int itemid = item.getItemId();

        if (itemid == 1 ) {
            w.clearList();
        }else if(itemid==2){
            startActivity(i);
        }

        return true;
    }

    public void deleteEntry(View v){
        Wine a = (Wine) w.getItem(focus);
        a.getName();


    }
}
