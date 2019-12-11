package com.example.myapplication;

import java.util.ArrayList;

public class BuyDTO {
    String id;
    String name;
    String address;
    String date;
    String delivery;
    ArrayList<BuyData> buyList = new ArrayList();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getDelivery() {
        return delivery;
    }

    public ArrayList<BuyData> getBuyList() {
        return buyList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public void setBuyList(ArrayList<BuyData> buyList) {
        this.buyList = buyList;
    }
}
