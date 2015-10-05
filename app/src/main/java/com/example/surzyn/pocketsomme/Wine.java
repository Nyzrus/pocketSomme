package com.example.surzyn.pocketsomme;

/**
 * Created by surzyn on 5/8/15.
 */
public class Wine {

    String name, vineyard, varietal, rating, link;

    Wine(String name, String vineyard, String varietal, String rating, String link){
        this.name = name;
        this.varietal = varietal;
        this.vineyard = vineyard;
        this.rating = rating;
        this.link = link;
    }

    public String getName(){
        return name;
    }

    public String getVineyard(){
        return vineyard;
    }

    public String getVarietal(){
        return varietal;
    }

    public String getRating(){ return rating;}

    public String getLink(){ return link;}
}
