package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchPassword extends AppCompatActivity {

    EditText userNewPassword;
    EditText userReNewPassword;

    Intent intent;

    MySQLiteHelper helper;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_password);

        userNewPassword = findViewById(R.id.userrepassword);
        userReNewPassword = findViewById(R.id.userReNewPassword);


    }

    public void changePassword(View view) {
        //1.비밀번호와 비밀번호 확인이 일치한지 확인한다.
        //2.일치하면 DB에 비밀번호를 바꾸어 준다.

        intent = getIntent();

        String password = userNewPassword.getText().toString();
        String repassword = userReNewPassword.getText().toString();

        if(password.equals(repassword)){
           //비밀번호와 비밀번호 확인이 일치한다면!
            helper = new MySQLiteHelper(this);
            database = helper.getWritableDatabase();

            database.execSQL("UPDATE UserInfo SET password = '"+password+"' WHERE id = '"+intent.getExtras().getString("id")+"'");
            Toast.makeText(this, "비밀번호가 성공적으로 변경되었습니다!", Toast.LENGTH_SHORT).show();

            finish();
            startActivity(new Intent(this, MainActivity.class));
        }else{
            Toast.makeText(this, "비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
