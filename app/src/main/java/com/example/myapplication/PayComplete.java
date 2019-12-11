package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class PayComplete extends AppCompatActivity {

    Gson gson;
    //[회원의 아이디와 (날짜, 물품정보들)]을 저장하는 해쉬맵이다.
    ArrayList<HashMap<String, HashMap<String, ArrayList<BuyData>>>> user_buy= new ArrayList();

    //회원의 아이디와 (날짜, 물품정보들)을 일회용으로 저장하는 해쉬맵이다.
    HashMap<String, HashMap<String, ArrayList<BuyData>>> user_buy_one = new HashMap();

    //날짜와 (물품 정보들)을 일회용으로 저장하는 해쉬맵이다.
    HashMap<String, ArrayList<BuyData>> user_buy_one_one = new HashMap();

    //구매내역 날짜를 내림차순으로 정렬하기 위한 구매내역 정보들을 누적 저장할 해쉬맵을 선언한다.
    HashMap<String, ArrayList<BuyData>> user_buy_one_one_sort = new HashMap();

    //구매내역의 날짜를 보여주기위한 리스트뷰 어댑터이다.
    Adapter_OlderHistory adapter_olderHistory;

    TextView userpoint;
    static TextView title;
    Intent intent;

    Cursor cursor;
    SQLiteDatabase database;
    MySQLiteHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_complete);

        intent = getIntent();


        title = findViewById(R.id.title);

        //회원이 보유하고 있는 포인트를 보여주기 위해, 해당하는 뷰위젯을 가져온다.
        userpoint = findViewById(R.id.userpoint);

        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();

        cursor = database.rawQuery("SELECT point FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});

        cursor.moveToNext();

        userpoint.setText("보유 포인트 : "+cursor.getInt(0)+"point");

        //Buy 저장소에서 데이터를 가져온다.
        SharedPreferences sp = getSharedPreferences("Buy", MODE_PRIVATE);
        //Buy 저장소 안에 있는 데이터 중, buyStr 데이터를 가져온다.(구매내역 데이터)
        String strContact = sp.getString("buyStr", "");
        UserInfo.setStrContact(strContact);//후에 구매 목록 데이터를 사용하기 위해 저장한다.
        //리스트 형태의 데이터를 가져오기 위한 사전준비 작업이다 - Type클래스를 정의한다.
        Type listType = new TypeToken<ArrayList<HashMap<String, HashMap<String, ArrayList<BuyData>>>>>() {}.getType();
        gson = new GsonBuilder().create();

        if(!strContact.equals("")){
            user_buy = gson.fromJson(strContact, listType);
        }

        //1.구매내역을 가지고 있는 DB를 size반큼 반복을 돌린 후에, 해당하는 Key값이 로그인중인 아이디인지 검사한다.
        for(int i = 1 ; i <= user_buy.size() ; i++){
            //해당하는 key값이 로그인값과 같은지 검사한다.
            if(user_buy.get(i - 1).containsKey(UserInfo.getUserId())){ //만약 로그인중인 아이디와 같다면! 다음 작업을 수행한다.

                //하나의 해당 Key값(로그인중인 회원 아이디)에 해당하는 Value값(날짜와 구매내역 정보들)을 할당한다.
                user_buy_one = user_buy.get(i - 1);

                //하나의 Key값(물품 구매 날짜)와 Value값(해당 시각에 구매한 물품DTO들)을 할당한다.
                user_buy_one_one = user_buy_one.get(UserInfo.getUserId());

                //구매 날짜를 기준으로 물품 리스트 뷰를 내림차순으로 정렬하기 위해 구매내역 데이터들을 누적 저장한다.
                user_buy_one_one_sort.putAll(user_buy_one_one);
            }//이로써 user_buy_one_one_sort데이터에는 구매날짜를 Key값으로 하고 구매물품들을 Value값으로 하는
            //데이터들이 누적으로 저장되어 있다.
        }

        //반복문이 끝나면 구매날짜를 오름차순으로 재정렬 한다.
        TreeMap<String, ArrayList<BuyData>> tm = new TreeMap(user_buy_one_one_sort);

        Iterator<String> iteratorKey = tm.descendingKeySet().iterator();

        //user_buy_one_one데이터를 재활용 하기 위해 비운다.
        user_buy_one_one.clear();

        while(iteratorKey.hasNext()){
            String key = iteratorKey.next();
            Log.i("내림차순 정렬 날짜 : ",key);
            ArrayList<BuyData> value = user_buy_one_one_sort.get(key);
            user_buy_one_one.put(key, value);
            UserInfo.buy.putAll(user_buy_one_one);
        }//여기 내림차순은 문제없다
        //user_buy_one_one에 쌓이는 정보들을 하나하나 누적해서 UserInfo.buy에 넣어주면 된다.


        adapter_olderHistory = new Adapter_OlderHistory(UserInfo.buy,this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_olderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_olderHistory);

    }

    public void goToMain(View view) {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
