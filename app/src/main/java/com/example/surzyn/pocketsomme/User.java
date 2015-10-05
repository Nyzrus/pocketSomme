package com.example.surzyn.pocketsomme;

/**
 * Created by surzyn on 5/8/15.
 */
public class User {

    String username;
    String password;
    int userId;

    public User(String userName, String userPassword, int id){
        this.username = userName;
        this.password = userPassword;
        this.userId = id;
    }

    public String getPassword(){return password;}

    public int getId(){return userId;}

    public String getUsername(){return username;}

}
