package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class Popup_goToMain extends AppCompatActivity {

    public static Activity popup_goToMain;
    SearchIdPassword searchIdPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup_go_to_main);
        popup_goToMain = Popup_goToMain.this;

        searchIdPassword = (SearchIdPassword)SearchIdPassword.searchIdPassword;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchIdPassword.finish();

    }

    public void goMain(View view) {
        finish();
        Login.login.finish();
        startActivity(new Intent(this, Login.class));
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
}
