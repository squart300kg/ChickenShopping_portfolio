package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity_Game extends AppCompatActivity {
    SQLiteDatabase database;
    MySQLiteHelper helper;
    Cursor cursor;

    RadioButton peanut_ball;
    RadioButton yellow_ball;
    RadioButton black_ball;
    EditText input_point;

    TextView main_point;
    TextView main_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__game);

        peanut_ball = findViewById(R.id.peanut_ball);
        yellow_ball = findViewById(R.id.yellow_ball);
        black_ball = findViewById(R.id.black_ball);
        input_point = findViewById(R.id.input_point);

        main_point = findViewById(R.id.main_point);
        main_id = findViewById(R.id.main_id);

        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();

        cursor = database.rawQuery("SELECT point FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});

        cursor.moveToNext();

        int point = cursor.getInt(0);

        main_point.setText("포인트 : "+point+"point");
        main_id.setText(UserInfo.getUserId()+"님, 반갑습니다!");
    }

    public void startRace(View view) {
        //만약 배팅금액을 입력하지 않았다면 배팅금을 입력하라는 메시지를 띄워준다.
        if(input_point.getText().toString().equals("") || input_point.getText().toString().equals(null)){
            Toast.makeText(this, "배팅포인트를 입력해 주세요",  Toast.LENGTH_SHORT).show();
            return;
        }
        int money = Integer.parseInt(input_point.getText().toString());
        //사용자가 입력한 배팅금액이 자신이 보유한 배팅금액보다 많을 경우
        if(money > UserInfo.getPoint()){
            Toast.makeText(this, "배팅하신 포인트가 너무 많습니다.\n 보유 포인트를 확인해 주세요!",  Toast.LENGTH_SHORT).show();
            return;
        }

        //모든 과정을 다 마쳤다면 이제 경기 시작 전에, 회원의 보유 배팅포인트를 차감한다.
        UserInfo.setPoint(UserInfo.getPoint() - Integer.parseInt(input_point.getText().toString()));
        //차감된 배팅 포인트를 DB에 갱신한다.
        database = helper.getWritableDatabase();
        database.execSQL("UPDATE UserInfo SET point = "+UserInfo.getPoint() + " WHERE id = '"+UserInfo.getUserId()+"'");

        //사용자가 고른 캐릭터를 식별한다.
        if(yellow_ball.isChecked()){
            //사용자가 만약 노란공 캐릭터를 선택했다면
            UserInfo.setRunner("yellow_ball");
        }else if(black_ball.isChecked()){
            //사용자가 만약 검은공 캐릭터를 선택했다면
            UserInfo.setRunner("black_ball");
        }else if(peanut_ball.isChecked()){
            //사용자가 만약 땅콩 캐릭터를 선택했다면
            UserInfo.setRunner("peanut_ball");
        }

        Intent intent = new Intent(this, Race.class);
        intent.putExtra("input_point",money);
        startActivity(intent);

        //포인트를 차감하고 경기를 시작하면 해당 화면을 지운다.
        finish();

    }

}
