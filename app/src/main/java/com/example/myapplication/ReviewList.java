package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewList extends AppCompatActivity implements Adapter_Review.ClickListener {

    Adapter_Review adapter_review;
    Gson gson;
    ArrayList<Review> reviewServer = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

    }

    @Override
    protected void onPause() {
        super.onPause();
       // ReviewServer.reviewList.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();

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
        ReviewServer.reviewList.clear();
        ReviewServer.reviewList.addAll(reviewServer);

        adapter_review = new Adapter_Review(ReviewServer.reviewList);
        adapter_review.setOnItemClick(ReviewList.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_review);
    }

    @Override
    public void OnReviewClick(int posistion) {
        Log.i("ReviewList-position:",posistion+"");
        Log.i("ReviewList-size:",ReviewServer.reviewList.size()+"");
        Intent intent = new Intent(this, ReviewSee.class);
        intent.putExtra("position", posistion);
        startActivity(intent);
       // finish();
    }
}
