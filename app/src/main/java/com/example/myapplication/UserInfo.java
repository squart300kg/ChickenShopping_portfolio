package com.example.myapplication;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class UserInfo {
    static String strContact = null;

    public static void setStrContact(String strContact) {
        UserInfo.strContact = strContact;
    }

    public static String getStrContact() {
        return strContact;
    }

    static String runner = " ";
    static String userId = " ";
    static String userPassword = " ";
    static  String userName = " ";
    static  String userEmail = " ";
    static  String userAddress = " ";
    static String userPhone = " ";
    static int point = 0;

    public static void setPoint(int point) {
        UserInfo.point = point;
    }

    public static int getPoint() {
        return point;
    }

    static boolean isLogin = false;
    static MySQLiteHelper helper;
    static SQLiteDatabase database;

    static ArrayList<CartData> cartList = new ArrayList();
    static ArrayList<BuyData> buyList = new ArrayList();
    static HashMap<String, ArrayList<BuyData>> buy = new HashMap();
    public static MySQLiteHelper getHelper() {
        return helper;
    }

    public static SQLiteDatabase getDatabase() {
        return database;
    }


    public static String getUserId() {
        return userId;
    }

    public static String getUserPassword() {
        return userPassword;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static String getUserAddress() {
        return userAddress;
    }

    public static void setUserId(String userId) {
        UserInfo.userId = userId;
    }

    public static void setUserPassword(String userPassword) {
        UserInfo.userPassword = userPassword;
    }

    public static void setUserName(String userName) {
        UserInfo.userName = userName;
    }

    public static void setUserEmail(String userEmail) {
        UserInfo.userEmail = userEmail;
    }

    public static void setUserAddress(String userAddress) {
        UserInfo.userAddress = userAddress;
    }

    public static void setRunner(String runner) {
        UserInfo.runner = runner;
    }

    public static String getRunner() {
        return runner;
    }

    public static void setUserPhone(String userPhone) {
        UserInfo.userPhone = userPhone;
    }

    public static String getUserPhone() {
        return userPhone;
    }
}
