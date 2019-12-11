package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Popup_goToMain2 extends AppCompatActivity {

    MySQLiteHelper helper;
    SQLiteDatabase database;
    Cursor cursor;

    Intent intent;
    String password_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup_go_to_main2);

        intent = getIntent();


    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    public void onBackPressed(){
        return;
    }

    public void goToChange(View view) {
        helper = new MySQLiteHelper(this);
        database = helper.getWritableDatabase();

        String currentPassword2 = intent.getExtras().getString("currentPassword2");
        String changePassword2 = intent.getExtras().getString("changePassword2");
        String changeRePassword2 = intent.getExtras().getString("changeRePassword2");
        String name2 = intent.getExtras().getString("name2");
        String phone2 = intent.getExtras().getString("phone2");
        String email2 = intent.getExtras().getString("email2");
        String address2 = intent.getExtras().getString("address2");

        UserInfo.setUserName(name2);
        UserInfo.setUserPhone(phone2);
        UserInfo.setUserAddress(address2);
        UserInfo.setUserEmail(email2);
        UserInfo.setUserPassword(changePassword2);



        database.execSQL("UPDATE UserInfo SET password = '"+changePassword2+"', name = '"+name2+"', email = '"+email2+"', address = '"+address2+"', phone = '"+phone2+"' WHERE id = '"+UserInfo.getUserId()+"'");
        Toast.makeText(this, "회원 정보 변경을 완료하였습니다!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));//OK를 눌러주면 메인으로 이동!
        ChangeUserInfo.changeUserInfo.finish();//회원 정보 변경페이지를 종료해준다.
        MyPage.myPage.finish();//마이페이지를 종료해준다.
        finish();
    }

    public void noChange(View view) {
        finish();//취소를 누르면 아무일도 안일어난다.
    }
}
