package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PayPhone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.payphone);
    }

    public void goToBuy(View view){
        Toast.makeText(this, "결제를 완료하였습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PayPhone.this, Authentication_Number.class);
        startActivity(intent);
    }
}
