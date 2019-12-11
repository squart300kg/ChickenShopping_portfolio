package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Popup extends AppCompatActivity {

    Button cancel,go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.popup);

        cancel = findViewById(R.id.cancel);
        go = findViewById(R.id.go);
    }

    public void cancel(View view){
        PopupCheck.setPopupCheck(true);
        finish();
    }
    public void go(View view){

        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","sosizi");
        startActivity(intent);
        PopupCheck.setPopupCheck(true);
        finish();
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
