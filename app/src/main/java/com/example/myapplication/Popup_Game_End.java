package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Popup_Game_End extends AppCompatActivity {
    ImageView myRunner;
    TextView gameEnd_text;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup__game__end);

        intent = getIntent();

        int img = intent.getExtras().getInt("img");
        String text = intent.getExtras().getString("gameEnd_text");

        myRunner = findViewById(R.id.myRunner);
        gameEnd_text = findViewById(R.id.gameEnd_text);

        myRunner.setImageResource(img);
        gameEnd_text.setText(text);
    }

    public void gameEnd(View view) {
        startActivity(new Intent(this, MainActivity_Game.class));
        Race race = (Race)Race.raceActivity;
        race.finish();
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
