package com.example.surzyn.pocketsomme;

/**
 * Created by surzyn on 5/8/15.
 *
 * urlMaker is used to construct a url that queries the Snooth API for specified query contents
 *
 */
public class urlMaker {

    String base = "http://api.snooth.com/wines/?akey=arra37jo2ggrjtza95o4rtalafuplipjpb7q9xasa065uqn9";
    String s;
    String convert;
    String limit ="&xp=10";

    urlMaker(String simple){
        convert = simple.replaceAll(" ", "+");
        s = base + "&q=" + convert + limit;
    }

    public String getUrl(){
        return s;
    }
}
