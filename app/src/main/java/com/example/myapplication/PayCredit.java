package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PayCredit extends AppCompatActivity {
    Intent intent;
    int point = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.paycredit);
        intent = getIntent();

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void goToBuy(View view){
        Intent intent = new Intent(PayCredit.this, Popup_PayCredit.class);
        startActivity(intent);

    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void goToBuy_Test(View view) {
        Intent intent = new Intent(this, PayCredit_API.class);
        startActivity(intent);
    }
}
