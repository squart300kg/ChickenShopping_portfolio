package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Buy extends AppCompatActivity {
    String name_1;
    int image_1;
    String option;
    int price;
    int count;
    Adapter_Buy adapter_buy;

    Gson gson;
    ArrayList<HashMap<String, CartData>> user_cart;
    HashMap<String, CartData> one_cart;

    TextView buy_total_price;
    EditText point;
    int totalPrice;
    int point_1 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.buy);

        //회원이 기재한 포인트
        point = findViewById(R.id.point);


        //회원 이름 텍스트 뷰 불러옴
        TextView name = findViewById(R.id.name);
        //회원 주소 텍스트 뷰 불러옴
        TextView address = findViewById(R.id.address);
        //회원 이메일 텍스트 뷰 불러옴
        TextView email = findViewById(R.id.email);

        //텍스트 란에 회원 이름 설정
        name.setText("이름 : " + UserInfo.getUserName());
        address.setText("주소 : " + UserInfo.getUserAddress());//텍스트 란에 회원 주소 설정
        email.setText("이메일 : " + UserInfo.getUserEmail());//텍스트 란에 회원 이메일 설정

//        //=================이제부터 회원 장바구니 리스트를 불러와서 UserInfo.cartList에 저장할 것이다.================


    }

    @Override
    protected  void onStart(){
        super.onStart();

        //장바구니 페이지로부터 왔느냐 구매하기 페이지로부터 왔느냐에 따라서 분기가 된다
        boolean from_buy = getIntent().getBooleanExtra("from_buy",false);

        if(from_buy){//만약 구매하기 페이지로부터 왔다면
            CartData cartData = new CartData();
            ArrayList<CartData> cartList = new ArrayList();
            cartData.setName(getIntent().getExtras().getString("sosizi_list1_name"));
            cartData.setImg(getIntent().getExtras().getInt("sosizi_list1_img"));
            cartData.setOption(getIntent().getExtras().getString("option"));
            cartData.setPrice(getIntent().getExtras().getString("price"));
            cartData.setCount(getIntent().getExtras().getString("count"));
            Log.i("Buy - count - ",cartData.getPrice());
            cartList.add(cartData);

            //여기 데이터 하나를 쉐어드 프리퍼런스를 사용하여 cart저장소에 넣어주면 된다!
            //Cart 저장소에서 데이터를 가져온다.
            SharedPreferences sp = getSharedPreferences("Cart", MODE_PRIVATE);
            String strContact = sp.getString("cartStr","");
            Type listType = new TypeToken<ArrayList<HashMap<String, CartData>>>() {}.getType();
            gson = new GsonBuilder().create();
            if(!strContact.equals("")){
                //Array참조변수에 장바구니에 대한 모든 데이터를 가져온다.
                user_cart = gson.fromJson(strContact, listType);
            }else{
                user_cart = new ArrayList();
            }
            one_cart = new HashMap();
            one_cart.put(UserInfo.getUserId(), cartData);
            user_cart.add(one_cart);

            //리스트 형태의 데이터를 가져오기 위한 사전준비 작업이다 - Type클래스를 정의한다.

            String cartStr = gson.toJson(user_cart, listType);

            SharedPreferences.Editor editor = sp.edit();
            Log.i("BuySosizi - 중복 저장 : ",cartStr);
            editor.putString("cartStr",cartStr);
            editor.commit();

            adapter_buy = new Adapter_Buy(cartList);
            RecyclerView recyclerView = findViewById(R.id.recyclerView_buy);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter_buy);

            int count = 0;
            int lastPrice = 0;
            totalPrice = 0;
            for(int i = 1 ; i <= cartList.size() ; i ++){
                price = Integer.parseInt(cartList.get(i - 1).getPrice());
                count = Integer.parseInt(cartList.get(i - 1).getCount());
                lastPrice = price * count;
                totalPrice += lastPrice;
            }

            if(totalPrice < 50000){
                totalPrice += 2500;
            }
            buy_total_price = findViewById(R.id.buy_total_price);
            buy_total_price.setText("총 주문 금액 : " + totalPrice + "원");


            //포인트를 표시해준다.
            Cursor cursor;
            SQLiteDatabase database;
            MySQLiteHelper helper;

            helper = new MySQLiteHelper(this);
            database = helper.getReadableDatabase();

            cursor = database.rawQuery("SELECT point FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});

            cursor.moveToNext();

            int db_point = cursor.getInt(0);//DB로부터 가져온 회원의 포인트
            point.setText(db_point+"");
        }else{//그게 아니라 장바구니 페이지로부터 왔다면
            //=================이제부터 회원 장바구니 리스트를 불러와서 UserInfo.cartList에 저장할 것이다.================
            //Cart 저장소에서 데이터를 가져온다.
            SharedPreferences sp = getSharedPreferences("Cart", MODE_PRIVATE);
            //Cart 저장소 안에 있는 데이터 중, cartStr 데이터를 가져온다.
            String strContact = sp.getString("cartStr", "");
            //리스트 형태의 데이터를 가져오기 위한 사전준비 작업이다 - Type클래스를 정의한다.
            Type listType = new TypeToken<ArrayList<HashMap<String, CartData>>>() {}.getType();
            gson = new Gson();

            user_cart = gson.fromJson(strContact,listType);

            Log.i("Buy.java-장바구니 DB갯수 : ",user_cart.size()+" ");
            Log.i("Buy.java-장바구니 User갯수 : ",UserInfo.cartList.size()+" ");
            for(int i = 1 ; i <= user_cart.size() ; i ++){
                //유저의 아이디가 UserInfo.getUserId()와 일치한다면
                //UserInfo.cartList에 장바구니 목록을 추가한다.
                Log.i("Cart : ","물품 데이터를 Buy에 뿌려주기");


                //저장소에 있는 장바구니에 대한 데이터 하나를 one_cart에 저장한다.
                one_cart = user_cart.get(i - 1);

                //아이디가 로그인한 아이디와 일치한다면 그 정보를 UserInfo.cartList에 넣는다.
                if(one_cart.containsKey(UserInfo.getUserId())){
                    UserInfo.cartList.add(one_cart.get(UserInfo.getUserId()));
                }
            }
            Log.i("Buy.java- 물품 갯수 : ",UserInfo.cartList.size()+"");

            //구매하기 페이지로 건너온 물품들이 장바구니에서부터 왔는지, 구매하기에서부터 왔는지 검사하기 위함이다.

            //그게 아니라 장바구니 페이지로부터 왔다면 장바구니 페이지에 담긴 물품 모두를 리스트에 표시해준다.
            adapter_buy = new Adapter_Buy(UserInfo.cartList);
            RecyclerView recyclerView = findViewById(R.id.recyclerView_buy);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter_buy);


            int count = 0;
            int lastPrice = 0;
            totalPrice = 0;
            for(int i = 1 ; i <= UserInfo.cartList.size() ; i ++){
                price = Integer.parseInt(UserInfo.cartList.get(i - 1).getPrice());
                count = Integer.parseInt(UserInfo.cartList.get(i - 1).getCount());
                lastPrice = price * count;
                totalPrice += lastPrice;
            }

            if(totalPrice < 50000){
                totalPrice += 2500;
            }
            buy_total_price = findViewById(R.id.buy_total_price);
            buy_total_price.setText("총 주문 금액 : " + totalPrice + "원");


            //포인트를 표시해준다.
            Cursor cursor;
            SQLiteDatabase database;
            MySQLiteHelper helper;

            helper = new MySQLiteHelper(this);
            database = helper.getReadableDatabase();

            cursor = database.rawQuery("SELECT point FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});

            cursor.moveToNext();

            int db_point = cursor.getInt(0);//DB로부터 가져온 회원의 포인트
            point.setText(db_point+"");
        }


    }


    @Override
    protected void onStop() {
        super.onStop();
        UserInfo.cartList.clear();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserInfo.cartList.clear();
    }


    public void buyStep_1(View view){
        RadioButton radio_account = (RadioButton)findViewById(R.id.buytype_account);
        RadioButton radio_phone = (RadioButton)findViewById(R.id.buytype_phone);
        RadioButton radio_credit = (RadioButton)findViewById(R.id.buytype_credit);

        OlderProduct.older_Name = UserInfo.getUserName();
        OlderProduct.older_Address = UserInfo.getUserAddress();
        OlderProduct.product_Price = (totalPrice - point_1) + "원";
        OlderProduct.total_price = totalPrice - point_1;
        OlderProduct.point = point_1;

        Intent intent;


        if(radio_account.isChecked()){
            intent = new Intent(this, PayAccount_API.class);
            UserInfo.cartList.clear();
            //intent = new Intent(Buy.this, PayAccount.class);
            startActivity(intent);
        }else if(radio_credit.isChecked()){
            intent = new Intent(Buy.this, PayCredit_API.class);
            UserInfo.cartList.clear();
            startActivity(intent);
        }else if(radio_phone.isChecked()){
            intent = new Intent(this, PayPhone_API.class);
            UserInfo.cartList.clear();
//            intent = new Intent(Buy.this, PayPhone.class);
            startActivity(intent);
        }


    }

    public void point_sub(View view) {
        point_1 = Integer.parseInt(point.getText().toString());//사용하기 버튼을 누르면 포인트 사용을 위한 준비를 한다.
        Cursor cursor;
        SQLiteDatabase database;
        MySQLiteHelper helper;

        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();

        cursor = database.rawQuery("SELECT point FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});

        cursor.moveToNext();

        int db_point = cursor.getInt(0);//DB로부터 가져온 회원의 포인트
        Log.i("Buy - point : ",db_point+"");
        if(db_point < point_1){
            //만약 회원이 포인트 비용을 높게 책정했다면
            Toast.makeText(this, "회원님의 보유 포인트를 확인해 주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        //포인트 사용버튼을 클릭하면 전체 가격을 뺴준다.
        buy_total_price.setText("총 주문 금액 : " + (totalPrice - Integer.parseInt(point.getText().toString()))+"원");



    }

    public void point_no_sub(View view) {
        //포인트 사용안함을 누르면 포인트 금액이 0원으로 바뀐다.
        //포인트 사용액을 0원으로 초기화한다.
        point.setText(0+"");
        point_1 = 0;
        buy_total_price.setText("총 주문 금액 : " + (totalPrice - Integer.parseInt(point.getText().toString()))+"원");
    }
}
