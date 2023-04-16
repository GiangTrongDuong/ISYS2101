package com.example.myapplication;

public class Item {
    String itemName;
    String itemTime;

    public Item(String itemName, String itemTime) {
        this.itemName = itemName;
        this.itemTime = itemTime;
    }

    public String getitemName() {
        return itemName;
    }

    public String getitemTime() {
        return itemTime;
    }
}
