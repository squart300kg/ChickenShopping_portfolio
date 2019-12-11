package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class UserInsert extends AppCompatActivity {
    SQLiteDatabase database;
    MySQLiteHelper helper;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinsert);

    }

    public void userInsertStep(View view) {

        EditText userphone = findViewById(R.id.userphone);
        EditText userid = findViewById(R.id.userid);
        EditText userpassword = findViewById(R.id.userpassword);
        EditText userrepassword = findViewById(R.id.userrepassword);
        EditText username = findViewById(R.id.username);
        EditText useremail = findViewById(R.id.useremail);
        EditText useraddress = findViewById(R.id.useraddress);


        if (!userpassword.getText().toString().equals(userrepassword.getText().toString())) {
            Toast.makeText(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        //======================================================아이디 중복확인을 한다. Admin이라는 아이디로 가입을 못하게 한다.
        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();
        //UI에서 입력된 회원 아이디가 DB에 있는지 확인한다.
        cursor = database.rawQuery("SELECT count(id) FROM UserInfo WHERE id = ?", new String[]{userid.getText().toString()});

        //회원이 UI에서 입력한 아이디가 이미 존재하거나, 관리자의 아이디와 일치하면 다음 조건문을 실행한다.
        cursor.moveToNext();
        if(cursor.getInt(0) != 0 || userid.getText().toString().equals("admin")) {//만약 아이디가 존재한다면!
            Toast.makeText(this, "이미 존재하는 아이디 입니다!", Toast.LENGTH_SHORT).show();
            return;
        }

        //=====================================================================================================================
        //회원가입 모든 절차가 끝났다. 회원 정보를 DB에 저장시킨다.

        UserInfo.setUserId(userid.getText().toString());
        UserInfo.setUserPassword(userpassword.getText().toString());
        UserInfo.setUserName(username.getText().toString());
        UserInfo.setUserEmail(useremail.getText().toString());
        UserInfo.setUserAddress(useraddress.getText().toString());
        UserInfo.setPoint(2000);//처음회원가입을 하면 2000포인트를 줌
        UserInfo.setUserPhone(userphone.getText().toString());

        helper = new MySQLiteHelper(this);
        database = helper.getWritableDatabase();
       // String sql = "insert into " + "UserInfo values('" + UserInfo.getUserId() + "', '"+UserInfo.getUserPassword()+"', '"+UserInfo.getUserName()+"','"+UserInfo.getUserEmail()+"', '"+UserInfo.getUserAddress()+"')";
        database.execSQL("insert into " + "UserInfo values('" + UserInfo.getUserId() + "', '"+UserInfo.getUserPassword()+"', '"+UserInfo.getUserName()+"','"+UserInfo.getUserEmail()+"', '"+UserInfo.getUserAddress()+"', '"+UserInfo.getPoint()+"', '"+UserInfo.getUserPhone()+"')");
        Log.i("DB에 회원정보 넣기 : ","성공");

        //DB에 회원정보를 저장한다.
        UserInfo.isLogin = true;

        Toast.makeText(this, "회원가입을 완료하였습니다!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
