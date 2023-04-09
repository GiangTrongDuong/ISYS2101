package com.example.tripme;

public class User {

    private String email;
    private String name;
    private String phone;
    private String password;
    private String role;
    private String tripID;

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, String role, String tripID, String phone) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.tripID = tripID;
        this.phone = phone;
    }



    public void setpassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getTripID() {
        return tripID;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getpassword() {
        return password;
    }
}
