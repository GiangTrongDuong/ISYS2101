package com.example.nvaphone;

public class User {
    private String email;
    private String name;

    public User(){

    }
    public User(String entered_email,String entered_name){
        email = entered_email;
        name = entered_name;
    }
    public String getEmail(){ return this.email;  }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName(){ return this.name;  }
    public void setName(String name) {
        this.name = name;
    }
}
