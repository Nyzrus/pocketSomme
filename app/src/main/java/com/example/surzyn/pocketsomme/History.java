package com.example.surzyn.pocketsomme;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class History extends ActionBarActivity {

    ListView l;
    historyAdapter ha;
    int focus;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        l = (ListView) findViewById(R.id.favoritesList);
        ha = new historyAdapter(this);

        l.setAdapter(ha);
        i = new Intent(this, Search.class);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                focus = position;
            }
        });
    }

    public void addWine(View v){

        l.setAdapter(ha);

    }

    public void clearList(View v){
        ha.clearList();
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
        int groupId = 0;
        int order = 0;
        menu.add(groupId, 1, ++order, "Clear Favorites");
        menu.add(groupId, 2, ++order, "Back to Search");


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
            ha.clearList();
        }else if(itemid==2){
            startActivity(i);
        }

        return true;
    }
}
