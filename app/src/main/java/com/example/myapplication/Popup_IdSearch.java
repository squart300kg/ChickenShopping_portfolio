package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Popup_IdSearch extends AppCompatActivity {
    Button ok;
    TextView idSearchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.popup_idsearch);

        Intent intent = getIntent();
        idSearchText = findViewById(R.id.idSearchText);
        ok = findViewById(R.id.ok);

        idSearchText.setText("회원님의 아이디는 " + intent.getExtras().getString("id") + "입니다. ");
    }
    public void ok(View view){
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
