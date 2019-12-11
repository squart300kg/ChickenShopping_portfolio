package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView login_on;
    MySQLiteHelper helper;
    SQLiteDatabase database;

    ImageView main_banner;
    ArrayList<Drawable> img = new ArrayList();
    int index = 0;
    ArrayList<SosiziList> sosiziList;

    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //메인화면

        //메인 배너에 이미지를 넣는다.
        img.add(getResources().getDrawable((R.drawable.ball2)));
        img.add(getResources().getDrawable((R.drawable.stake2)));
        img.add(getResources().getDrawable((R.drawable.sosizi_list3)));
        img.add(getResources().getDrawable((R.drawable.steam2)));

        //배너화면을 알맞은 곳으로 보내주기 위해 상품 이름과, 이미지를 일시적으로만 하드코딩한다.
        sosiziList = new ArrayList();

        sosiziList.add(new SosiziList("(맛있닭)네가지맛의 사각볼 닭가슴살", R.drawable.ball2));
        sosiziList.add(new SosiziList("(맛있닭)6성급 레스토랑급의 스테이크 닭가슴살", R.drawable.stake2));
        sosiziList.add(new SosiziList("(또먹닭)네가지 맛의 소시지 닭가슴살", R.drawable.sosizi_list3));
        sosiziList.add(new SosiziList("(맛있닭)퍽퍽하지 않고 촉촉한 스팀 닭가슴살", R.drawable.steam2));
        //하드코딩

        helper = new MySQLiteHelper(this);
        login_on = findViewById(R.id.login_on);

        //메인화면의 배너 ImageView를 가져온다.
        main_banner = findViewById(R.id.main_banner);

        //메인화면의 배너 이미지를 주기적으로 바꿔주기 위해 스레드를 실행시킨다.
        bannerHandler.sendEmptyMessage(0);

        final Intent intent = new Intent(this, BuySosizi_1.class);

        main_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //스레드로 증감하는 변수인 index가 어떤 값이냐에 따라 이동하는 페이지에 다른 상품이름과 이미지를 넘겨준다.
                intent.putExtra("name",sosiziList.get(index).getName());
                intent.putExtra("img",sosiziList.get(index).getImg());

                startActivity(intent);
            }
        });

    }

    Handler bannerHandler = new Handler(){
        public void handleMessage(Message msg){

            index ++;
            if(index >= img.size())index = 0;

            main_banner.setImageDrawable(img.get(index));
            bannerHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    public void board(View view){
        Intent intent = new Intent(this, ReviewList.class);
        startActivity(intent);
    }
    public void goToSosiziList(View view){
        Intent intent = new Intent(MainActivity.this, Category.class);
        intent.putExtra("from","sosizi");
        startActivity(intent);
    }
    public void goToStakeList(View view) {
        Intent intent = new Intent(MainActivity.this, Category.class);
        intent.putExtra("from","stake");
        startActivity(intent);
    }

    public void goToSteamList(View view) {
        Intent intent = new Intent(MainActivity.this, Category.class);
        intent.putExtra("from","steam");
        startActivity(intent);
    }

    public void goToBallList(View view) {
        Intent intent = new Intent(MainActivity.this, Category.class);
        intent.putExtra("from","ball");
        startActivity(intent);
    }
    public void goToRawList(View view) {
        Intent intent = new Intent(MainActivity.this, Category.class);
        intent.putExtra("from","raw");
        startActivity(intent);
    }

    public void goToYukpoList(View view) {
        Intent intent = new Intent(MainActivity.this, Category.class);
        intent.putExtra("from","yukpo");
        startActivity(intent);
    }

    public void goTohunjeList(View view) {
        Intent intent = new Intent(MainActivity.this, Category.class);
        intent.putExtra("from","hunje");
        startActivity(intent);
    }

    public void goToSliceList(View view) {
        Intent intent = new Intent(MainActivity.this, Category.class);
        intent.putExtra("from","slice");
        startActivity(intent);
    }




    public void cart(View view){
        Intent intent = new Intent(this, Cart.class);
        intent.putExtra("main",true);
        startActivity(intent);
    }
    public void my(View view){
        //로그인을 안했다면 로그인 창을 띄워주고 그게 아니라면 로그아웃창과, 주문내역페이지를 띄워준다.

        if(!UserInfo.isLogin){//로그인을 안했다면 로그인 페이지로 보내주고
            startActivity(new Intent(MainActivity.this, Login.class));
        }else{//아니라면 마이페이지로 보내준다
            startActivity(new Intent(getApplicationContext(), MyPage.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //로그인창을 갔다가 다시 돌아왔는데 관리자 로그인이라면 다른 페이지로 안내한다.
        if(UserInfo.getUserId().equals("admin")){
            Intent intent = new Intent(this, Admin.class);
            startActivity(intent);
        }

        if(!PopupCheck.isPopupCheck()){
            Intent intent = new Intent(getApplicationContext(), Popup.class);
            startActivity(intent);
        }

        if(UserInfo.isLogin){//로그인 상태가 true = 로그인 중이다라는 뜻
            Log.i("메인액티비티",UserInfo.getUserId());
            login_on.setText(UserInfo.getUserName() + "님,\n 안녕하세요!");
        }else
            login_on.setText(" ");
    }


    public void game(View view) {
        if(!UserInfo.isLogin){
            Toast.makeText(this, "로그인을 해주세요",  Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MainActivity_Game.class);
        startActivity(intent);
    }


    public void goToAllList(View view) {
        Intent intent = new Intent(MainActivity.this, Category.class);
        intent.putExtra("from","all");
        startActivity(intent);
    }
}
