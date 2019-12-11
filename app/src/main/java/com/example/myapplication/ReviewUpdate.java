package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewUpdate extends AppCompatActivity {

    ImageView img;
    TextView name;
    EditText title;
    EditText contents;
    int position;

    String in_title;
    String in_contents;

    Gson gson;
    ArrayList<Review> reviewServer = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_update);

        img = findViewById(R.id.review_update_img);
        name = findViewById(R.id.review_update_name);
        title = findViewById(R.id.review_update_title);
        contents = findViewById(R.id.review_update_contents);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        img.setImageResource(intent.getExtras().getInt("img"));
        name.setText(intent.getExtras().getString("name"));
        title.setText(intent.getExtras().getString("title"));
        contents.setText(intent.getExtras().getString("contents"));
    }

    public void review_update_End(View view){
        in_title = title.getText().toString();
        in_contents = contents.getText().toString();

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

        //새로운 데이터를 다시 넣어준다.
        reviewServer.get(position).setTitle(in_title);
        reviewServer.get(position).setContents(in_contents);
        //내용을 새롭게 갱신하기 때문에 기존의 데이터는 지워버린다.
        String revStr  = gson.toJson(reviewServer, listType);

        SharedPreferences.Editor editor = sp.edit();
        Log.i("ReviewUpdata.java : ",revStr);
        editor.putString("revStr",revStr);
        editor.commit();
        //ReviewServer.reviewList.get(position).setTitle(in_title);
        //ReviewServer.reviewList.get(position).setContents(in_contents);

        finish();
    }
}
