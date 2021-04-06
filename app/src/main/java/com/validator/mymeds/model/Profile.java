package com.validator.mymeds.model;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * The following class is used to store user data.
 */
public class Profile implements Serializable {

    //Attributes
    public String authId;
    public String name;
    public String username;
    public String email;
    public ArrayList<String> allergies;

    //Constructor
    public Profile(){

    }

    public Profile(String n, String user, String email, String auth) {
        name = n;
        username = user;
        this.email = email;
        authId = auth;
        allergies = new ArrayList<String>();
    }

    //Get Method
    public String getAuthId() {
        return authId;
    }
}


