package com.example.tripme;

public class Notification {
    String notiNo;
    String notiTime;
    String notiText;

    public Notification(){

    }
    public Notification(String no, String time, String text) {
        this.notiNo = no;
        this.notiTime = time;
        this.notiText = text;
    }

    public String getNotiNo() {
        return notiNo;
    }

    public String getNotiTime() {
        return notiTime;
    }

    public String getNotiText() {
        return notiText;
    }
    public String toString(){
        return "[" + notiTime + "]" + " \n" + notiText;
    }
}
