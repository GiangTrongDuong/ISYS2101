package com.example.tripme;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//This class keeps track of the last notification that was alerted by the user's phone
//This record is entirely local, similar to that of the SingletonAppTime
public class SingletonLastNoti {
    private String notiTime;
    private int notiID;
    private static SingletonLastNoti notiInstance = null;

    protected SingletonLastNoti(){
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        notiTime = format.format(currentTime);

        notiID = 79;
    }

    public static SingletonLastNoti getInstance(){
        if (notiInstance == null){
            notiInstance = new SingletonLastNoti();
        }
        return notiInstance;
    }
    public String getNotiTime() {
        return notiTime;
    }

    public void setNotiTime(String notiTime) {
        this.notiTime = notiTime;
    }

    public int getNotiID() {
        return notiID;
    }

    public void setNotiID(int notiID) {
        this.notiID = notiID;
    }
}
