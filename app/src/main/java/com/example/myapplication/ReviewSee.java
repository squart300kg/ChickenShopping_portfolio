package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReviewSee extends AppCompatActivity {

    int position;

    ImageView img;
    TextView title;
    TextView contents;
    TextView writter;
    TextView btn_update;
    TextView btn_delete;

    Gson gson;
    ArrayList<Review> reviewServer = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_see);
        Log.i("ReviewSee - 1 : ","들어옴");
        //Review 저장소에서 데이터를 가져온다.
        SharedPreferences sp = getSharedPreferences("Review", MODE_PRIVATE);
        //Review 저장소 안에 있는 데이터 중, revStr 데이터를 가져온다.
        String strContact = sp.getString("revStr", "");

        Type listType = new TypeToken<ArrayList<Review>>(){}.getType();
        gson = new GsonBuilder().create();
        Log.i("ReviewSee - 2 : ","들어옴");
        //DB로부터 불러온 Review 데이터가 있다면  그 데이터를 reviewServer로 가리킨다.
        if(!strContact.equals("")){
            //Array참조변수에 장바구니에 대한 모든 데이터를 가져온다.
            reviewServer = gson.fromJson(strContact, listType);
        }
        Log.i("ReviewSee - 3 : ","들어옴");
        ReviewServer.reviewList.addAll(reviewServer);


        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        Log.i("ReviewSee-position-:",position+"");
        Log.i("ReviewSee-size-:",ReviewServer.reviewList.size()+"");
        img = findViewById(R.id.review_see_img);
        title = findViewById(R.id.review_see_title);
        contents = findViewById(R.id.review_see_contents);
        writter = findViewById(R.id.review_see_writter);
        btn_update = findViewById(R.id.review_see_update);
        btn_delete = findViewById(R.id.review_see_delete);

        img.setImageResource(ReviewServer.reviewList.get(position).getImg());
        title.setText(ReviewServer.reviewList.get(position).getTitle());
        contents.setText(ReviewServer.reviewList.get(position).getContents());
        writter.setText(ReviewServer.reviewList.get(position).getWritter());

        if(!UserInfo.getUserId().equals(ReviewServer.reviewList.get(position).getWritter())){
            btn_update.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);
        }
        if(UserInfo.getUserId().equals("admin")){
            //만약 관리자가 로그인 한것이라면 삭제버튼을 보여준다.
            btn_delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ReviewServer.reviewList.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void review_see_update(View view){
        Intent intent = new Intent(this, ReviewUpdate.class);

        intent.putExtra("img",ReviewServer.reviewList.get(position).getImg());
        intent.putExtra("name",ReviewServer.reviewList.get(position).getName());
        intent.putExtra("title",ReviewServer.reviewList.get(position).getTitle());
        intent.putExtra("contents",ReviewServer.reviewList.get(position).getContents());
        intent.putExtra("position",position);
        intent.putExtra("update",true);

        startActivity(intent);
    }
    public void review_see_delete(View view){
        //정말 삭제하시겠습니까? Y -> 삭제 N -> 삭제안함
        Intent intent = new Intent(this, Popup_ReviewDelete.class);
        intent.putExtra("position",position);
        startActivity(intent);

    }
}
