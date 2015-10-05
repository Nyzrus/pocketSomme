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

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by surzyn on 5/8/15.
 */
public class wineAdapter extends BaseAdapter {

    List<Wine> wineList;
    private int count = 1;
    private SQLiteDatabase db;
    private String sdcardPath;
    private final String dbName = "dbfavorite";
    Context context;

    wineAdapter (Context context){
        wineList = new ArrayList<Wine>();
        this.context = context;
        try{
            db = context.openOrCreateDatabase(dbName, 0, null);
            //Toast.makeText(context, "success1", Toast.LENGTH_SHORT).show();
            createTable();

            performQuery();
            //Toast.makeText(context, "success2", Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
            Toast.makeText(context, "failed to make favorite database", Toast.LENGTH_SHORT).show();
        }
    }

    public void createTable(){
        String query = "SELECT name FROM sqlite_master WHERE type='table'";
        Cursor cs = db.rawQuery(query, null);
        cs.moveToFirst();
        int nameIdx = cs.getColumnIndex("name");
        while(cs.isAfterLast() == false){
            String name = cs.getString(nameIdx);
            if(name.equals("favorites"))
                return;
            cs.moveToNext();
        }
        db.beginTransaction();
        try{
            db.execSQL("create table favorites (" +
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

        Cursor cs = db.rawQuery("select * from favorites order by name COLLATE NOCASE",null);
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

            String submit = " insert into favorites(name,varietal,vineyard, rating, link) "+
                    "values ( '"+ item.getName() + "','" + item.getVarietal() +"','"+ item.getVineyard()+"','"+item.getRating()+"','"+item.getLink()+"');";
            db.execSQL( submit );
            db.setTransactionSuccessful();
            //Toast.makeText(context, "successfully entered data", Toast.LENGTH_LONG).show();
        }
        catch(Exception e) {
            Toast.makeText(context, "failed to enter data", Toast.LENGTH_LONG).show();
        }
        finally {
            db.endTransaction();
        }
        return true;
    }

    public boolean checkDuplicates(String wineName){
        Cursor cs = db.rawQuery("select * from favorites order by name",null);
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
                db.execSQL( "drop table favorites; ");
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

    public String getFavoriteVarietal(){
        try{
            String query = " select vineyard" +
                    " from favorites order by name";
            //String query = " select vineyard" +
            //        " from favorites order by name";
            Cursor cs = db.rawQuery(query, null);
            int n = cs.getColumnIndex("vineyard");
            cs.moveToFirst();
            String s = "";
            while(cs.isAfterLast() == false) {
                s = cs.getString(n);
                cs.moveToNext();
            }
            return s;
            //Toast.makeText(context, n, Toast.LENGTH_LONG).show();
            /*String s = cs.getString(n);
            /*db.setTransactionSuccessful();
            return s;*/
        }catch(Exception e){
            Toast.makeText(context, "varietal query fail", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public String alternateMethod(){
        try{
            String query = " select vineyard, COUNT(vineyard) as cnt" +
                    " from favorites order by cnt";
            Cursor cs = db.rawQuery(query, null);
            int n = cs.getColumnIndex("vineyard");
            cs.moveToFirst();
            String s = "";
            s = cs.getString(n);
            while(cs.isAfterLast() == false) {
                s = cs.getString(n);
                cs.moveToNext();
            }
            return s;
        }catch(Exception x){
            Toast.makeText(context, "varietal query fail", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
