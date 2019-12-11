package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Popup_PayCredit extends AppCompatActivity {
    Gson gson;
    Intent intent;
    int point = 0;
    ArrayList<HashMap<String, CartData>> user_cart;
    HashMap<String, CartData> user_cart_one;



    Button cancel, ok;
    TextView name,address,price;
    //String nowDate = new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());

    //String nowDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
    Calendar calendar = Calendar.getInstance();
    java.util.Date date = calendar.getTime();
    String nowDate = (new SimpleDateFormat("yyyyMMddHHmmss").format(date));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.popup_paycredit);

        intent = getIntent();

        cancel = findViewById(R.id.cancel);
        ok = findViewById(R.id.ok);

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        price = findViewById(R.id.price);

        name.setText("이름 : " + OlderProduct.older_Name);
        address.setText("주소 : " + OlderProduct.older_Address);
        price.setText(OlderProduct.product_Price);


    }

    public void cancel(View view){
        Toast.makeText(this, "결제가 취소되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
    public void ok(View view){
        ArrayList<HashMap<String, HashMap<String, ArrayList<BuyData>>>> user_buy= new ArrayList();
        HashMap<String, HashMap<String, ArrayList<BuyData>>> user_buy_one = new HashMap();
        HashMap<String, ArrayList<BuyData>> user_buy_one_one = new HashMap();

        //Buy 저장소에서 데이터를 가져온다.
        SharedPreferences sp = getSharedPreferences("Buy", MODE_PRIVATE);
        //Buy 저장소 안에 있는 데이터 중, buyStr 데이터를 가져온다.
        String strContact = sp.getString("buyStr", "");
        //리스트 형태의 데이터를 가져오기 위한 사전준비 작업이다 - Type클래스를 정의한다.
        Type listType = new TypeToken<ArrayList<HashMap<String, HashMap<String, ArrayList<BuyData>>>>>() {}.getType();
        gson = new GsonBuilder().create();

        //사용자가 두번쨰 이상으로 구매내역을 갱신하려고 할 때, Buy 저장소에 있는 모든 정보를 가져온 후, 갱신하고 다시 넣기 위함이다.
        if(!strContact.equals("")){
            Log.i("user_buy : ","초기화 됐다리");
            //Array참조변수인 Buy에 대한 모든 데이터를 가져온다.
            user_buy = gson.fromJson(strContact, listType);
        }

        SoldOut.isSoldOut = true;
        Toast.makeText(this, "결제를 완료하였습니다.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

        ArrayList<BuyData> buyList = new ArrayList();
        //구매완료를 진행하면 구매목록을 추가하고, 장바구니 목록은 비운다.

        //최종적으로 추려진 장바구니 리스트를 가져온다.
        //최종적으로 DB에 존재하는 회원의 장바구니 리스트를 가져온다.
        //Cart 저장소에서 데이터를 가져온다.
        SharedPreferences sp_1 = getSharedPreferences("Cart", MODE_PRIVATE);
        //Cart 저장소 안에 있는 데이터 중, cartStr 데이터를 가져온다.
        strContact = sp_1.getString("cartStr", "");
        //리스트 형태의 데이터를 가져오기 위한 사전준비 작업이다 - Type클래스를 정의한다.
        listType = new TypeToken<ArrayList<HashMap<String, CartData>>>() {}.getType();
        gson = new Gson();
        //리스트값을 가져온다.
        user_cart = gson.fromJson(strContact,listType);

        for(int i = 1 ; i <= user_cart.size() ; i ++){
            //유저의 아이디가 UserInfo.getUserId()와 일치한다면
            //UserInfo.cartList에 장바구니 목록을 추가한다.

            //저장소에 있는 장바구니에 대한 모든 데이터를 one_cart에 저장한다.
            user_cart_one = user_cart.get(i - 1);

            //아이디가 로그인한 아이디와 일치한다면 그 정보를 UserInfo.cartList에 넣는다.
            if(user_cart_one.containsKey(UserInfo.getUserId())){
                UserInfo.cartList.add(user_cart_one.get(UserInfo.getUserId()));
            }
        }
        for(int i = 1 ; i <= UserInfo.cartList.size() ; i ++){
            BuyData buyData = new BuyData();

            buyData.setName(UserInfo.cartList.get(i - 1).getName());
            Log.i("구매목록 이름 : ", buyData.getName());

            buyData.setImg(UserInfo.cartList.get(i - 1).getImg());
            Log.i("구매목록 이미지 : ", Integer.toString(buyData.getImg()));

            buyData.setCount(UserInfo.cartList.get(i - 1).getCount());
            Log.i("구매목록 갯수 : ", buyData.getCount());

            buyData.setOption(UserInfo.cartList.get(i - 1).getOption());
            Log.i("구매목록 옵션 : ", buyData.getOption());

            buyData.setPrice(UserInfo.cartList.get(i - 1).getPrice());
            Log.i("구매목록 가격 : ", buyData.getPrice());

            buyList.add(buyData);


        }
        Log.i("현재 시간 : ", nowDate);

        //이로써 한 시각에 구매한 물품들을 키와 값으로 저장했다. (HashMap<String, ArrayList<BuyData>>)
        user_buy_one_one.put(nowDate, buyList);


        for(int i = 1 ; i <= buyList.size() ; i ++){
            Log.i("노가다 로그 : ",buyList.get(i - 1).getName());
        }//로그가 잘 찍히는 걸로 봐선  user_buy_one_one.put(nowDate, buyList);에 값이 잘 들어갔다.


        //이제 이 정보를 Value값으로 하고, Key값은 아이디로 해서 또다시 HashMap에 저장한다.
        user_buy_one.put(UserInfo.getUserId(), user_buy_one_one);

        //이제 하나의 유저가 특정한 시간에 어떠한 물품들을 구매했는지 하나의 데이터를 만들었다.
        //이것을 이제 DB에 저장해야 한다. 저장을 위한 배열변수에 넣자.

        user_buy.add(user_buy_one);

        //리스트 자료형 형식을 정해준다.
        listType = new TypeToken<ArrayList>() {}.getType();

        //리스트 형태를 쉐어드에 저장하기 위해서 Gson을 사용해서 문자열로 변환한다.
        String buyStr = gson.toJson(user_buy, listType);
        Log.i("buyStr",buyStr);//buyStr이 제대로 저장되지 않았다.
        //쉐어드를 수행하기 위한 객체를 얻어온다.
        SharedPreferences.Editor editor = sp.edit();

        //쉐어드에 구매 내역을 최종적으로 저장한다.
        editor.putString("buyStr", buyStr);
        editor.commit();
        //====================================================================================
        //관리자 프로그램과의 소통을 위해 BuyDTO에 값을 넣고 DB에 저장한다.
        //=====================================================================================
        //DB로부터 구매내역에 관한 데이터를 받아와 저장하기 위한 변수이다.
        ArrayList<BuyDTO> buyDtoArray = new ArrayList();
        //구매내역 하나를 저장할 일회용 변수이다.
        BuyDTO buyDTO = new BuyDTO();
        //BuyDTO 저장소에서 데이터를 가져온다.
        sp = getSharedPreferences("BuyDTO", MODE_PRIVATE);
        //BuyDTO 저장소 안에 있는 데이터 중, buyDTOStr 데이터를 가져온다.
        strContact = sp.getString("buyDTOStr", "");

        listType = new TypeToken<ArrayList<BuyDTO>>(){}.getType();
        gson = new GsonBuilder().create();

        //DB로부터 불러온 BuyDTO 데이터가 있다면  그 데이터를 buyDtoArray로 가리킨다.
        if(!strContact.equals("")){
            //Array참조변수에 장바구니에 대한 모든 데이터를 가져온다.
            buyDtoArray = gson.fromJson(strContact, listType);
        }

        //한 고객이 한 시각에 구매한 구매내역 정보들을 담았다.
        buyDTO.setId(UserInfo.getUserId());
        buyDTO.setName(UserInfo.getUserName());
        buyDTO.setAddress(UserInfo.getUserAddress());
        buyDTO.setDelivery(" - 배송전");
        buyDTO.setBuyList(buyList);
        String year = nowDate.substring(0,4);
        String month = nowDate.substring(4,6);
        String day = nowDate.substring(6,8);
        String hour = nowDate.substring(8,10);
        String minute = nowDate.substring(10,12);
        String second = nowDate.substring(12,14);

        nowDate = year + "/" + month +"/" + day + " - " + hour + ":" + minute + ":" + second;
        Log.i("구매당시 시각 : ",nowDate);
        buyDTO.setDate(nowDate);
        buyDtoArray.add(buyDTO);

        strContact = gson.toJson(buyDtoArray, listType);

        editor = sp.edit();
        Log.i("BuyDTO.java : ",strContact);
        editor.putString("buyDTOStr", strContact);
        editor.commit();
        //=====================================================================================
        //구매 내역을 설정했으니까 이제 회원이 구매했던 장바구니를 비울 차례다//
        //=====================================================================================

        //Cart 저장소에서 데이터를 가져온다.
        sp = getSharedPreferences("Cart", MODE_PRIVATE);
        //Cart 저장소 안에 있는 데이터 중, cartStr 데이터를 가져온다.
        strContact = sp.getString("cartStr", "");
        Log.i("cartStr(삭제전) : ",strContact);
        //리스트 형태의 데이터를 가져오기 위한 사전준비 작업이다 - Type클래스를 정의한다.
        listType = new TypeToken<ArrayList<HashMap<String, CartData>>>() {}.getType();
        gson = new Gson();

        user_cart = gson.fromJson(strContact,listType);

        for(int i = user_cart.size() ; i >= 1 ; i --){
            //유저의 아이디가 UserInfo.getUserId()와 일치한다면
            //UserInfo.cartList에 장바구니 목록을 추가한다.

            //저장소에 있는 장바구니에 대한 모든 데이터를 one_cart에 저장한다.
            user_cart_one = user_cart.get(i - 1);

            //아이디가 로그인한 아이디와 일치한다면 그 정보를 UserInfo.cartList에 넣는다.
            if(user_cart_one.containsKey(UserInfo.getUserId())){
                user_cart.remove(i - 1);
            }
        }
        listType = new TypeToken<ArrayList<HashMap<String, CartData>>>(){}.getType();
        String cartStr = gson.toJson(user_cart,listType);
        Log.i("cartStr(삭제후) : ",cartStr);
        sp = getSharedPreferences("Cart",MODE_PRIVATE);
        editor = sp.edit();

        editor.putString("cartStr",cartStr);
        editor.commit();

        for(int i = 1 ; i <= UserInfo.cartList.size(); i ++){
            UserInfo.cartList.clear();
        }
        finish();
        //만약 회원이 포인트를 함꼐써서 물건을 구매했다면 포인트를 그만큼 차감시켜준다.
        SQLiteDatabase database;
        MySQLiteHelper helper;
        Cursor cursor;

        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();

        cursor = database.rawQuery("SELECT point FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});

        cursor.moveToNext();
        //물건을 구매한 회원의 포인트를 가져온다.
        int point = cursor.getInt(0);

        point -= OlderProduct.point;//포인트를 사용한 만큼 포인트를 차감시켜준다.

        database = helper.getWritableDatabase();//이제 회원의 포인트 내역을 갱신한다.

        database.execSQL("UPDATE UserInfo SET point="+point + " WHERE id = '" + UserInfo.getUserId()+"'");

        //상품구매를 다 했으면 회원에게 포인트를 적립해 주어야 한다. 5%를 적립해 준다.


        helper = new MySQLiteHelper(this);
        database = helper.getReadableDatabase();

        int accumPoint = (int)Math.ceil(OlderProduct.total_price * 0.05);//구매금액의 5%이다. = 포인트

        //우선적으로 해당 회원의 포인트를 조회해와서 해당 금액에 누적시켜 다시 저장시킨다.

        //물품 구매 회원의 포인트를 조회한다.
        cursor = database.rawQuery("SELECT point FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});

        cursor.moveToNext();

        accumPoint += cursor.getInt(0);//구매한 물품의 포인트 금액과 조회한 포인트 금액을 누적시킨다.

        cursor = database.rawQuery("SELECT id FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});

        cursor.moveToNext();

        Log.i("나의 아이디 : ",cursor.getString(0));

        String myId = cursor.getString(0);
        database = helper.getWritableDatabase();

        //누적시켰으니 이제 다시 저장시킨다.
        database.execSQL("UPDATE UserInfo SET point="+accumPoint + " WHERE id = '" + UserInfo.getUserId()+"'");
        //database.execSQL("UPDATE UserInfo SET = ? WHERE = ?",new String[]{accumPoint, UserInfo.getUserId()});
    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    public void onBackPressed(){
        return;
    }
}
