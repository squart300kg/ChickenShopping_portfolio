package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    Cursor cursor;
    public final static int DATABASE_VERSION = 10;
    public final static String DATABASE_NAME = "chicken.db";
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS UserInfo (id primary key not null, password text not null, name text not null, email not null, address text not null, point not null, phone not null)";
        db.execSQL(sql);
        /*
        String sql2 = "CREATE TABLE IF NOT EXISTS Product (name, option, count, price)";
        db.execSQL(sql2);
        String sql3 = "CREATE TABLE IF NOT EXISTS Cart (id ,name,img , option, count, price)";
        db.execSQL(sql3);
        String sql4 = "CREATE TABLE IF NOT EXISTS Buy(id, name, img, option, count, price)";
        db.execSQL(sql4);
        String sql5 = "CREATE TABLE IF NOT EXISTS Review(id, name, img, title, contents)";
        db.execSQL(sql5);*/
        Log.i("OpenHelper - onCreate","테이블 생성 완료");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table UserInfo");
//        db.execSQL("drop table Product");
//        db.execSQL("drop table Cart");
//        db.execSQL("drop table Buy");
//        db.execSQL("drop table Review");

        onCreate(db);
        Log.i("OpenHelper - onUpgrade","테이블 삭제, 생성 완료");
    }


    public String login_check(SQLiteDatabase db, String id){
        String sql = "SELECT count(password) FROM UserInfo WHERE "+id;
        String password_db;
        cursor = UserInfo.getDatabase().rawQuery(sql,null);
        if(cursor != null){
            cursor.moveToNext();
            password_db = cursor.getString(0);
        }else{
            return "아이디가 없음";
        }
        return password_db;
    }
}
