package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class Authentication_Number extends AppCompatActivity {

    TextView authentication_number_timer;
    int seconds = 0;
    int minute = 3;
    String t = "0";
    String clock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication__number);

        authentication_number_timer = findViewById(R.id.authentication_number_timer);
        mhandler.sendEmptyMessage(0);
    }


    Handler mhandler = new Handler(){
        public void handleMessage(Message msg){

            if(seconds/10 <= 0){
                t = "0";
               t = t + seconds + "";
               clock = minute+":"+t;
            }else{
                clock = minute+":"+seconds;
            }

            authentication_number_timer.setText(clock);
            seconds--;
            if(seconds < 0){
                seconds = 59;
                minute --;
            }
            mhandler.sendEmptyMessageDelayed(0,1000);
        }
    };

}
