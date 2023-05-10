package com.example.nvaphone;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Notification {
    private String content;
    private String time;

    public Notification(){

    }

    public Notification (String new_content, String new_time){
        this.content = new_content;
        this.time = new_time;
    }

    public Notification (String new_content){
        this.content = new_content;
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        this.time = format.format(currentTime);
    }
    public String getContent(){
        return content;
    }
    public String getTime(){
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setTime(String time) {
        this.time = time;
    }
    @NonNull
    public String toString(){
        return time + "\n" + content;
    }
}
