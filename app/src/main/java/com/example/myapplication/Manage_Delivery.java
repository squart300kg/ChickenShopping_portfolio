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

public class Manage_Delivery extends AppCompatActivity implements Adapter_ManageDelivery.ClickListener{

    //회원의 구매내역을 보여주기 위한 리사이클러뷰 어댑터이다.
    Adapter_ManageDelivery adapter_manageDelivery;
    Gson gson;
    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__delivery);

        //DB로부터 구매내역에 관한 데이터를 받아와 저장하기 위한 변수이다.
        ArrayList<BuyDTO> buyDtoArray = new ArrayList();
        //구매내역 하나를 저장할 일회용 변수이다.
        BuyDTO buyDTO = new BuyDTO();
        //BuyDTO 저장소에서 데이터를 가져온다.
        SharedPreferences sp = getSharedPreferences("BuyDTO", MODE_PRIVATE);
        //Review 저장소 안에 있는 데이터 중, buyDTOStr 데이터를 가져온다.
        String strContact = sp.getString("buyDTOStr", "");

        Type listType = new TypeToken<ArrayList<BuyDTO>>(){}.getType();
        gson = new GsonBuilder().create();

        //DB로부터 불러온 BuyDTO 데이터가 있다면  그 데이터를 reviewServer로 가리킨다.
        if(!strContact.equals("")){
            //Array참조변수에 장바구니에 대한 모든 데이터를 가져온다.
            buyDtoArray = gson.fromJson(strContact, listType);
        }
        //회원들의 구매내역을 보여주는 리싸이클러뷰 어댑터에다가 구매내역디비 리스트 데이터들을 넣어준다.
        adapter_manageDelivery = new Adapter_ManageDelivery(buyDtoArray);
        adapter_manageDelivery.setOnItemClick(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_manage_delivery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_manageDelivery);
    }


    @Override
    public void onManageDeliveryClick(int position) {
        Intent intent = new Intent(this, Manage_Delivery_See.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }
}
