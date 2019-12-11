package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void manage_delivery(View view) {
        Intent intent = new Intent(this, Manage_Delivery.class);
        startActivity(intent);
    }

    public void manage_review(View view) {
        Intent intent = new Intent(this, ReviewList.class);
        startActivity(intent);
    }

    public void manage_logout(View view){
        finish();
        UserInfo.setUserId("");
        startActivity(new Intent(this, MainActivity.class));
    }
}
