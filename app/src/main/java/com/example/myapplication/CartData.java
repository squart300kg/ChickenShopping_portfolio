package com.example.myapplication;

public class CartData {
    int img;
    String name;
    String option;
    String count;
    String price;

    public void setImg(int img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setPrice(String total_price) {
        this.price = total_price;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getOption() {
        return option;
    }

    public String getCount() {
        return count;
    }

    public String getPrice() {
        return price;
    }
}
