package com.example.myapplication;

public class SoldOut {
   static boolean isSoldOut = false;


    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }
}
