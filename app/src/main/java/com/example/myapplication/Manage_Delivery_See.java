package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Manage_Delivery_See extends AppCompatActivity {
    TextView id;
    TextView name;
    TextView address;
    TextView date;
    TextView delivery_state;
    Spinner spinner;
    Intent intent;
    int position;

    ArrayList<BuyDTO> buyDTOArray = new ArrayList();
    BuyDTO buyDTO = new BuyDTO();

    Gson gson;
    Adapter_ManageDeliverySee adapterManageDeliverySee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__delivery__see);

        id = findViewById(R.id.manage_delivery_see_id);
        name = findViewById(R.id.manage_delivery_see_name);
        address = findViewById(R.id.manage_delivery_see_address);
        date = findViewById(R.id.manage_delivery_see_date);

        intent = getIntent();
        position = intent.getExtras().getInt("position");
        //BuyDTO 저장소에서 데이터를 가져온다.
        SharedPreferences sp = getSharedPreferences("BuyDTO", MODE_PRIVATE);
        //BuyDTO 저장소 안에 있는 데이터 중, buyStr 데이터를 가져온다.
        String strContact = sp.getString("buyDTOStr", "");

        Type listType = new TypeToken<ArrayList<BuyDTO>>(){}.getType();
        gson = new GsonBuilder().create();

        //DB로부터 불러온 Review 데이터가 있다면  그 데이터를 reviewServer로 가리킨다.
        if(!strContact.equals("")){
            //Array참조변수에 장바구니에 대한 모든 데이터를 가져온다.
            buyDTOArray = gson.fromJson(strContact, listType);
        }
        buyDTO.buyList = buyDTOArray.get(position).getBuyList();
        buyDTO.delivery = buyDTOArray.get(position).getDelivery();

        adapterManageDeliverySee = new Adapter_ManageDeliverySee(buyDTO.getBuyList());
        RecyclerView recyclerView = findViewById(R.id.recyclerView_manage_olderProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterManageDeliverySee);
//===============================================DB에서 가져온 데이터를 UI에 뿌려주는 작업이다.======================

        id.setText("아이디 : "+buyDTOArray.get(position).getId());
        name.setText("이름 : "+buyDTOArray.get(position).getName());
        address.setText("주소 : "+buyDTOArray.get(position).getAddress());
        date.setText("주문일 : "+buyDTOArray.get(position).getDate());

//=============================================================spinner를 위한 선언이다=======================
        position = intent.getExtras().getInt("position");
        delivery_state = findViewById(R.id.manage_delivery_see_state);
        spinner = findViewById(R.id.spinner);
        if(buyDTOArray.get(position).getDelivery().equals(" - 입금 전")){
            spinner.setSelection(0);
        }else if(buyDTOArray.get(position).getDelivery().equals(" - 입금 완료")){
            spinner.setSelection(1);
        }else if(buyDTOArray.get(position).getDelivery().equals(" - 배송 전")){
            spinner.setSelection(2);
        }else if(buyDTOArray.get(position).getDelivery().equals(" - 배송 중")){
            spinner.setSelection(3);
        }else if(buyDTOArray.get(position).getDelivery().equals(" - 배송 완료")){
            spinner.setSelection(4);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                delivery_state.setText(" - " +
                        parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
    }

    public void delivery_Update(View view){
        buyDTOArray.get(position).setDelivery(delivery_state.getText().toString());
        //ArrayList<BuyData> buyDTOArray;
        Type listType = new TypeToken<ArrayList<BuyDTO>>(){}.getType();

        String strContact = gson.toJson(buyDTOArray, listType);

        SharedPreferences sp = getSharedPreferences("BuyDTO", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Log.i("BuyDTO.java : ",strContact);
        editor.putString("buyDTOStr", strContact);
        editor.commit();

        Intent intent = new Intent(this, Manage_Delivery.class);
        startActivity(intent);
        finish();
    }
}
