package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Popup_ReviewDelete extends AppCompatActivity {
    int position;

    Gson gson;
    ArrayList<Review> reviewServer = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup__review_delete);
    }

    public void delete(View view){
        //Review 저장소에서 데이터를 가져온다.
        SharedPreferences sp = getSharedPreferences("Review", MODE_PRIVATE);
        //Review 저장소 안에 있는 데이터 중, revStr 데이터를 가져온다.
        String strContact = sp.getString("revStr", "");

        Type listType = new TypeToken<ArrayList<Review>>(){}.getType();
        gson = new GsonBuilder().create();

        //DB로부터 불러온 Review 데이터가 있다면  그 데이터를 reviewServer로 가리킨다.
        if(!strContact.equals("")){
            //Array참조변수에 장바구니에 대한 모든 데이터를 가져온다.
            reviewServer = gson.fromJson(strContact, listType);
        }
        ReviewServer.reviewList.addAll(reviewServer);
        //회원이 선택한 인덱스 번호의 후기를 지원다.
        reviewServer.remove(position);

        String revStr  = gson.toJson(reviewServer, listType);

        SharedPreferences.Editor editor = sp.edit();
        Log.i("ReviewDelete.java : ",revStr);
        editor.putString("revStr",revStr);
        editor.commit();

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        ReviewServer.reviewList.remove(position);
        startActivity(new Intent(this, ReviewList.class));
        finish();


    }

    public void write(View view){
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
