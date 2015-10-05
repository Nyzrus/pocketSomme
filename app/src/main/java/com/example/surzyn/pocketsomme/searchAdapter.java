package com.example.surzyn.pocketsomme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by surzyn on 5/9/15.
 */
public class searchAdapter extends BaseAdapter {

    ArrayList<Wine> wineList;
    private int count = 1;
    Context context;

    searchAdapter (Context context){
        wineList = new ArrayList<Wine>();
        this.context = context;

    }

    @Override
    public int getCount() {
        return wineList.size();
    }

    public void clearList(){
        if (wineList.size() > 0 )
        {
            count = 1;
            wineList.clear();
            notifyDataSetChanged();
        }
    }

    public void insertEntry(Wine w){

        wineList.add(w);
    }

    public void insertEntry (String name, String vineyard, String price, String rating, String link) {
        insertEntry(new Wine(name, vineyard, price, rating, link));
    }

    @Override
    public Object getItem(int position) {
        return wineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Wine entry = wineList.get(position);

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.favoritelist, null);
        }

        TextView txtviewName = (TextView) convertView.findViewById(R.id.favoriteName);
        txtviewName.setText(entry.getName());
        TextView txtviewVarietal = (TextView) convertView.findViewById(R.id.favoriteVarietal);
        txtviewVarietal.setText(entry.getVineyard());
        TextView txtviewVineyard = (TextView) convertView.findViewById(R.id.favoriteVineyard);
        txtviewVineyard.setText(entry.getVarietal());
        TextView txtviewRating = (TextView) convertView.findViewById(R.id.favoriteRating);
        txtviewVineyard.setText("Rating: " + entry.getRating());

        return convertView;

    }

}
