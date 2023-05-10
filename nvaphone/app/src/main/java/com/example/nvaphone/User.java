package com.example.nvaphone;

public class User {
    private String email;
    private String name;
    private String phone;

    public User(){

    }
    public User(String entered_email,String entered_name, String entered_phone){
        email = entered_email;
        name = entered_name;
        phone = entered_phone;
    }
    public String getEmail(){ return this.email;  }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName(){ return this.name;  }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {  return phone;  }
    public void setPhone(String phone) { this.phone = phone;  }

}
