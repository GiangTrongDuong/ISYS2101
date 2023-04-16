package com.example.nvaphone;

public class User {
    private String name;
    private String email;

    public User(){

    }
    public User(String entered_name, String entered_email){
        name = entered_name;
        email = entered_email;
    }
    public String getName(){ return this.name;  }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail(){ return this.email;  }
    public void setEmail(String email) {
        this.name = email;
    }

}
