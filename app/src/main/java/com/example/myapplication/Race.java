package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Race extends AppCompatActivity {

    public static Activity raceActivity;
    Intent intent;
    int input_point;
    LinearLayout yellow_runner;
    LinearLayout peanut_runner;
    LinearLayout black_runner;

    TextView yellow_text;
    TextView black_text;
    TextView peanut_text;

    TextView yellow_grade;
    TextView black_grade;
    TextView peanut_grade;

    Animation animation;
    Animation animation2;
    Animation animation3;

    int img;
    Drawable drawable;
    String runner;
    int translate;
    String gameEnd_text;

    SQLiteDatabase database;
    MySQLiteHelper helper;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        raceActivity = Race.this;

        intent = getIntent();
        //입력받은 배팅금액을 해당 액티비티로 다시 받아오는 작업이다.
        input_point = intent.getExtras().getInt("input_point");

        //달리는 각각의 캐릭터가 어떤 애니메이션 효과를 받을지를 지정해주는 랜덤 변수
        translate = (int)(Math.random() * 3 + 1);

        //경기가 시작되면 나의 runner가 누구인지 지정해준 후, 표시해준다.
        runner = UserInfo.getRunner();

        yellow_runner = findViewById(R.id.yellow_runner);
        peanut_runner = findViewById(R.id.peanut_runner);
        black_runner = findViewById(R.id.black_runner);

        yellow_grade = findViewById(R.id.yellow_grade);
        black_grade = findViewById(R.id.black_grade);
        peanut_grade = findViewById(R.id.peanut_grade);

        //이전페이지에서 내가 Radio Button으로 선택한 캐릭터를 식별할 수 있도록 해준다.
        yellow_text = findViewById(R.id.yellow_text);
        peanut_text = findViewById(R.id.peanut_text);
        black_text = findViewById(R.id.black_text);
        if(runner.equals("peanut_ball")){
            peanut_text.setVisibility(View.VISIBLE);
            black_text.setVisibility(View.GONE);
            yellow_text.setVisibility(View.GONE);
            img = R.drawable.run1;
        }else if(runner.equals("black_ball")){
            peanut_text.setVisibility(View.GONE);
            black_text.setVisibility(View.VISIBLE);
            yellow_text.setVisibility(View.GONE);
            img = R.drawable.run3;
        }else if(runner.equals("yellow_ball")){
            yellow_text.setVisibility(View.VISIBLE);
            black_text.setVisibility(View.GONE);
            peanut_text.setVisibility(View.GONE);
            img = R.drawable.run2;
        }

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate2);
        animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate3);

        //시간이 가장 긴 애니메이션3시간이 끝나면 등수를 표시해준다.
        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                //애니메이션이 끝났으면 등수가 몇등인지 보여준다.
                yellow_grade.setVisibility(View.VISIBLE);
                black_grade.setVisibility(View.VISIBLE);
                peanut_grade.setVisibility(View.VISIBLE);

                //여기서 애니메이션이 끝났으면 결과값을 보여준다.
                //1. Modal창을 띄워준다.
                //2.

                Intent intent = new Intent(getApplicationContext(), Popup_Game_End.class);
                intent.putExtra("img",img);
                intent.putExtra("gameEnd_text",gameEnd_text);
                startActivity(intent);

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //랜덤하게 다른 스레드가 발동된다.
        if(translate == 1){
            runner_Handler_1.sendEmptyMessage(0);
            //노란애 1등, 땅콩 2등, 검정애 3등
        }else if(translate == 2){
            runner_Handler_2.sendEmptyMessage(0);
            //땅콩1등, 검정애 2등, 노란애 3등
        }else if(translate == 3){
            runner_Handler_3.sendEmptyMessage(0);
            //노란애 2등, 땅콩 3등, 검정애 1등
        }
    }
    Handler runner_Handler_1 = new Handler(){
        public void handleMessage(Message msg){

            yellow_runner.startAnimation(animation);
            peanut_runner.startAnimation(animation2);
            black_runner.startAnimation(animation3);

            yellow_grade.setText("1등");
            peanut_grade.setText("2등");
            black_grade.setText("3등");

            //포인트 계산을 한 후, 메인 액티비티로 보내준다.
            //finish();

            //1등이면 2배 2등이면 본전 3등이면 포인트 차감이다.
            if(runner.equals("yellow_ball")){
                UserInfo.point += 3 * input_point;
                gameEnd_text = "1등입니다! 축하합니다 \n2배의 포인트를 획득하셨습니다!";
            }else if(runner.equals("peanut_ball")){
                UserInfo.point += input_point;
                gameEnd_text = "2등입니다! 다행입니다 \n 본전은 찾았네요 ^_^";
            }else{
                gameEnd_text = "3등입니다! 아쉽네요 \n 다음엔 꼭 따자구요! ^_^";
            }

            //결과가 나온 포인트를 DB에 저장한다.
            helper = new MySQLiteHelper(getApplicationContext());
            database = helper.getWritableDatabase();

            database.execSQL("UPDATE UserInfo SET point="+UserInfo.getPoint() + " WHERE id = '" + UserInfo.getUserId()+"'");

        }
    };

    Handler runner_Handler_2 = new Handler(){
        public void handleMessage(Message msg){
            yellow_runner.startAnimation(animation3);
            peanut_runner.startAnimation(animation);
            black_runner.startAnimation(animation2);

            yellow_grade.setText("3등");
            peanut_grade.setText("1등");
            black_grade.setText("2등");

            //1등이면 2배 2등이면 본전 3등이면 포인트 차감이다.
            if(runner.equals("peanut_ball")){
                gameEnd_text = "1등입니다! 축하합니다 \n2배의 포인트를 획득하셨습니다!";
                UserInfo.point += 3 * input_point;
            }else if(runner.equals("black_ball")){
                UserInfo.point += input_point;
                gameEnd_text = "2등입니다! 다행입니다 \n 본전은 찾았네요 ^_^";
            }else{
                gameEnd_text = "3등입니다! 아쉽네요 \n 다음엔 꼭 따자구요! ^_^";
            }
            //결과가 나온 포인트를 DB에 저장한다.
            helper = new MySQLiteHelper(getApplicationContext());
            database = helper.getWritableDatabase();

            database.execSQL("UPDATE UserInfo SET point="+UserInfo.getPoint() + " WHERE id = '" + UserInfo.getUserId()+"'");


        }
    };
    Handler runner_Handler_3 = new Handler(){
        public void handleMessage(Message msg){
            yellow_runner.startAnimation(animation2);
            peanut_runner.startAnimation(animation3);
            black_runner.startAnimation(animation);

            yellow_grade.setText("2등");
            peanut_grade.setText("3등");
            black_grade.setText("1등");
            //1등이면 2배 2등이면 본전 3등이면 포인트 차감이다.
            if(runner.equals("black_ball")){
                gameEnd_text = "1등입니다! 축하합니다 \n2배의 포인트를 획득하셨습니다!";
                UserInfo.point += 3 * input_point;
            }else if(runner.equals("yellow_ball")){
                UserInfo.point += input_point;
                gameEnd_text = "2등입니다! 다행입니다 \n 본전은 찾았네요 ^_^";
            }else{
                gameEnd_text = "3등입니다! 아쉽네요 \n 다음엔 꼭 따자구요! ^_^";
            }
            //결과가 나온 포인트를 DB에 저장한다.
            helper = new MySQLiteHelper(getApplicationContext());
            database = helper.getWritableDatabase();

            database.execSQL("UPDATE UserInfo SET point="+UserInfo.getPoint() + " WHERE id = '" + UserInfo.getUserId()+"'");

        }
    };
}
