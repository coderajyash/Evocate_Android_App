package util;

import android.app.Application;

public class EvocateApi extends Application {
    private String username,userID;
    private static EvocateApi instance;

    public static  EvocateApi getInstance(){
        if(instance==null)
            instance = new EvocateApi();
        return instance;
    }

    public EvocateApi(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
