package com.example.surzyn.pocketsomme;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surzyn on 5/9/15.
 */
public class historyAdapter extends BaseAdapter {
    List<Wine> wineList;
    private int count = 1;
    private SQLiteDatabase db;
    private String sdcardPath;
    private final String dbName = "historyDB";
    Context context;

    historyAdapter (Context context){
        wineList = new ArrayList<Wine>();
        this.context = context;
        try{
            db = context.openOrCreateDatabase(dbName, 0, null);
            createTable();

            performQuery();
        }catch(Exception ex){
            Toast.makeText(context, "failed to make history database", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTable(){
        String query = "SELECT name FROM sqlite_master WHERE type='table'";
        Cursor cs = db.rawQuery(query, null);
        cs.moveToFirst();
        int nameIdx = cs.getColumnIndex("name");
        while(cs.isAfterLast() == false){
            String name = cs.getString(nameIdx);
            if(name.equals("history"))
                return;
            cs.moveToNext();
        }
        db.beginTransaction();
        try{
            db.execSQL("create table history (" +
                    " recID integer PRIMARY KEY autoincrement," +
                    " name text," +
                    " varietal text,"+
                    " vineyard text,"+
                    " rating text,"+
                    " link text);  ");
            db.setTransactionSuccessful();
        }catch(Exception ex){
            Toast.makeText(context, "Failed to create table" , Toast.LENGTH_LONG).show();
        }
        finally {
            db.endTransaction();
        }
    }

    private void performQuery(){

        Cursor cs = db.rawQuery("select * from history order by name COLLATE NOCASE",null);
        int nameId = cs.getColumnIndex("name");
        int vineyardId = cs.getColumnIndex("vineyard");
        int varietalId = cs.getColumnIndex("varietal");
        int ratingId = cs.getColumnIndex("rating");
        int linkId = cs.getColumnIndex("link");
        cs.moveToFirst();
        while(cs.isAfterLast() == false) {
            String name = cs.getString(nameId);
            String vineyard = cs.getString(vineyardId);
            String price = cs.getString(varietalId);
            String rating = cs.getString(ratingId);
            String link = cs.getString(linkId);
            wineList.add(new Wine(name, vineyard, price, rating, link));
            //wineList.add(new Wine(name, null, null, null, null));
            cs.moveToNext();
        }

    }

    @Override
    public int getCount() {
        return wineList.size();
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

        return convertView;
    }

    public boolean saveContents(Wine item){
        db.beginTransaction();
        try{

            if(checkDuplicates(item.getName())==true){
                return false;
            }
            String submit = " insert into history(name,varietal,vineyard,rating,link) "+
                    "values ( '"+ item.getName() + "','" + item.getVarietal() +"','"+ item.getVineyard()+"','"+item.getRating()+"','"+item.getLink()+"');";
            db.execSQL( submit );
            db.setTransactionSuccessful();
            //Toast.makeText(context, "successfully entered data", Toast.LENGTH_LONG).show();
        }
        catch(Exception e) {
            Toast.makeText(context, "failed to enter data2", Toast.LENGTH_LONG).show();
        }
        finally {
            db.endTransaction();
        }
        return true;
    }

    public boolean checkDuplicates(String wineName){
        Cursor cs = db.rawQuery("select * from history order by name",null);
        int nameId = cs.getColumnIndex("name");
        cs.moveToFirst();
        while(cs.isAfterLast() == false) {
            String name = cs.getString(nameId);
            if(name.equals(wineName)){
                return true;
            }
            cs.moveToNext();
        }
        return false;
    }

    public void insertEntry (String name, String vineyard, String price, String rating, String link) {
        insertEntry(new Wine(name, vineyard, price, rating, link));
    }

    public boolean insertEntry(Wine item) {
        notifyDataSetChanged();
        wineList.add(item);
        if(saveContents(item)){

            return true;
        }else{
            return false;
        }
    }

    public void clearList() {
        try {
            if (wineList.size() > 0 )
            {
                count = 1;
                wineList.clear();
                db.execSQL( "drop table history; ");
                notifyDataSetChanged();
            }
        }
        catch (Exception ex)
        {

        }
        createTable();

    }

    public void close(){
        db.close();
    }
}
