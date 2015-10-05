package com.example.surzyn.pocketsomme;

/*

    The SearchResults activity displays the information from the Search Activity in ListView form

 */


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SearchResults extends ActionBarActivity {

    TextView header;                            //header textview to hold the userQuery
    String queryReceive;                        //will hold the user's query from the last activity

    ArrayList<String> wineName;                 //ArrayLists to store the information passed to this activity from the last
    ArrayList<String> vineyardName;             //activity, Search
    ArrayList<String> varietals;
    ArrayList<String> links;
    ArrayList<String> ratings;
    ArrayList<Wine> wineList;
    ListView l;                                 //listview element will hold Wine elements in scrollable list format
    TextView t;
    searchAdapter sa;                           //searchAdapter formats the results to Listview
    historyAdapter ha;                          //historyAdapter and wineAdapter are used to allow for user insertions to
    wineAdapter wa;                             //history and favorite lists, respectively
    Intent i,b;                                 //i will be used to get the info from the last activity, b to allow for backwards movement to the Search page

    int focus;                                  //focus will provide the index of the listview elements to be used for other operations
                                                //such as insert to history/favorite lists, or travel to respective url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);       //initialize elements
        t = (TextView) findViewById(R.id.noResultLabel);
        header = (TextView) findViewById(R.id.header);



        b = new Intent(this, Search.class);
        focus = 404;

        i = getIntent();
        queryReceive = i.getStringExtra("query");
        wineName = i.getStringArrayListExtra("wines");
        vineyardName = i.getStringArrayListExtra("vineyards");
        varietals = i.getStringArrayListExtra("varietals");
        ratings = i.getStringArrayListExtra("ratings");
        links = i.getStringArrayListExtra("links");
        sa = new searchAdapter(this);
        wa = new wineAdapter(this);
        ha = new historyAdapter(this);
        l = (ListView) findViewById(R.id.searchList);

        header.setText("Query: " + queryReceive);

        try {
            for (int j = 0; j < wineName.size(); j++) {
                Wine newWine = new Wine(wineName.get(j), varietals.get(j), vineyardName.get(j), ratings.get(j), links.get(j));
                sa.insertEntry(newWine);        //insert wine into search adapter for display on listview
            }
            if(wineName.size()==0){
                t.setText("No results");
            }
        }catch(Exception ex){
            sa.insertEntry("No results", "","","", "");
        }

        l.setAdapter(sa);


        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                focus = position;
            }
        });



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sa.clearList();             //go back to Search and reset certain elements
            focus = 404;
            startActivity(b);
            return true;
        }

        return super.onKeyDown(keyCode, event);
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
        menu.add(groupId, 1, ++order, "Add to Favorites");
        menu.add(groupId, 2, ++order, "Add to History");
        menu.add(groupId, 3, ++order, "Add to Both");

    }

    public boolean onContextItemSelected(MenuItem item) {
        return (applyMenuOption(item));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return (applyMenuOption(item));
    }

    public boolean applyMenuOption(MenuItem item) {
        int itemid = item.getItemId();

        Wine w = (Wine) sa.getItem(focus);
        if (itemid == 1) {
            wa.insertEntry(w);
        } else if (itemid == 2) {
            ha.insertEntry(w);
        } else if (itemid == 3) {
            wa.insertEntry(w);
            ha.insertEntry(w);
        }

        return true;
    }

    public void addToFavorites(View v){     //adds specified wine to favorites list found in favorites activity
        int itemNumber = focus;
        if(itemNumber != 404){
            Wine w = (Wine) sa.getItem(focus);
            if(wa.insertEntry(w)){
                ha.insertEntry(w);
                Toast.makeText(this, "added to favorites", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "already in favorites", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "you must select a wine to insert", Toast.LENGTH_SHORT).show();
        }

    }

    public void addToHistory(View v){           //adds wine to history list, found in history activity
        int itemNumber = focus;
        if(itemNumber != 404){
            Wine w = (Wine) sa.getItem(focus);
            if(ha.insertEntry(w)){
                Toast.makeText(this, "added to history", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "already in history", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "you must select a wine to insert", Toast.LENGTH_SHORT).show();
        }

    }

    public void goToWebsite(View v){        //goes to the specified wine's website
        int itemNumber = focus;
        if(itemNumber != 404){
            Intent website = new Intent(Intent.ACTION_VIEW, Uri.parse(links.get(focus)));
            startActivity(website);
        }else{
            Toast.makeText(this, "you must select a wine", Toast.LENGTH_SHORT).show();
        }
    }
}
