package com.example.tripme;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//this class is used to record the launch time of the app
//CREATED in main, instances are gotten whenever we need to access appTime
public class SingletonAppTime {
    private String appTime;
    private static SingletonAppTime instance = null;
    protected SingletonAppTime(){
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        appTime = format.format(currentTime);
    }

    public static SingletonAppTime getInstance() {
        if(instance == null){
            instance = new SingletonAppTime();
        }
        return instance;
    }
    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }
}