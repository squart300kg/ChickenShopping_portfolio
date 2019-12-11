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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ReviewWrite extends AppCompatActivity {
    ImageView img;
    TextView name;
    int in_img;
    String in_name;
    String in_title;
    String in_contents;

    EditText title;
    EditText contents;

    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);
        title = findViewById(R.id.review_title);
        contents = findViewById(R.id.review_contents);

        Intent intent = getIntent();
        img = findViewById(R.id.review_img);
        name = findViewById(R.id.review_name);

        in_img = intent.getExtras().getInt("img");
        in_name = intent.getExtras().getString("name");
        if(intent.getExtras().getBoolean("update")) {
            in_title = intent.getExtras().getString("title");
            in_contents = intent.getExtras().getString("contents");

            Log.i("넘겨받은 제목 : ",in_title);

            Log.i("넘겨받은 내용 : ",in_contents);
            title.setText(in_title);
            contents.setText(in_contents);
        }
        img.setImageResource(in_img);
        name.setText(in_name);

    }
    public void reviewEnd(View view){
        ArrayList<Review> reviewServer = new ArrayList();

        Intent intent = new Intent(this, ReviewList.class);

        title = findViewById(R.id.review_title);
        contents = findViewById(R.id.review_contents);

        String title_1 = title.getText().toString();
        String contents_1 = contents.getText().toString();

        Log.i("리뷰 - 제목 : ",title_1);

         Log.i("리뷰 - 작성자 : ", contents_1);

         //버에 저장하고 리뷰 리스트 페이지로 이동!
        Review review = new Review();

        review.setTitle(title_1);
        review.setContents(contents_1);
        review.setImg(in_img);
        review.setName(in_name);
        review.setWritter(UserInfo.getUserId());//작성자 이름은 하드코딩

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
        reviewServer.add(review);

        strContact = gson.toJson(reviewServer, listType);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("revStr", strContact);
        editor.commit();

        //ReviewServer.reviewList.add(review);

        startActivity(intent);
      // TextView older_history_review = findViewById(R.id.older_history_review);
      // older_history_review.setVisibility(View.GONE);
        finish();
    }
}
