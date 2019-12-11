package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class PayPhone_API extends AppCompatActivity {

    private int stuck = 10;

    Gson gson;
    ArrayList<HashMap<String, CartData>> user_cart;
    HashMap<String, CartData> one_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_phone__api);

        BootpayAnalytics.init(this, "5d3d85c64f74b40022051d10");

        //물품 구매한 고객의 핸드폰 번호
        String phone1 = UserInfo.getUserPhone().substring(0, 3);
        String phone2 = UserInfo.getUserPhone().substring(3, 7);
        String phone3 = UserInfo.getUserPhone().substring(7, 11);
        String phone = phone1 + "-" + phone2 + "-" + phone3;

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

        String product_name;
        Log.i("payPhone_API - size : ",UserInfo.cartList.size()+"");
        if(UserInfo.cartList.size() == 1){
            //만약 회원이 구매하려는 물품이 1개라면
            product_name = UserInfo.cartList.get(0).getName();
        }else{
            //회원이 구매하려는 물품이 2개 이상이라면
            product_name = UserInfo.cartList.get(0).getName() + " 외 . . . (" + (UserInfo.cartList.size() - 1) + ")개 ";
        }

        // 결제호출
        BootUser bootUser = new BootUser().setPhone(phone);
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

        Bootpay.init(getFragmentManager())
                .setApplicationId("5d3d85c64f74b40022051d10") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.INICIS) // 결제할 PG 사
                .setMethod(Method.PHONE) // 결제수단
                .setContext(this)
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setUX(UX.PG_DIALOG)
//                .setUserPhone(phone) // 구매자 전화번호
                .setName(product_name) // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
                .setPrice(OlderProduct.total_price) // 결제할 금액
//                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
//                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {

                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        else 
                            Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d("done", message);
                        Toast.makeText(getApplicationContext(), "결제가 완료되었습니다!.", Toast.LENGTH_SHORT).show();
                        finish();


                        ArrayList<HashMap<String, HashMap<String, ArrayList<BuyData>>>> user_buy= new ArrayList();
                        HashMap<String, HashMap<String, ArrayList<BuyData>>> user_buy_one = new HashMap();
                        HashMap<String, ArrayList<BuyData>> user_buy_one_one = new HashMap();
                        HashMap<String, CartData> user_cart_one = new HashMap();
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

                        startActivity(new Intent(getApplicationContext(), PayComplete.class));

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
                        //이로써 한 시각에 구매한 물품들을 키와 값으로 저장했다. (HashMap<String, ArrayList<BuyData>>)
                        Calendar calendar = Calendar.getInstance();
                        java.util.Date date = calendar.getTime();
                        String nowDate = (new SimpleDateFormat("yyyyMMddHHmmss").format(date));
                        user_buy_one_one.put(nowDate, buyList);

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
                        buyDTO.setDelivery(" - 입금 완료");
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

                        helper = new MySQLiteHelper(getApplicationContext());
                        database = helper.getReadableDatabase();

                        cursor = database.rawQuery("SELECT point FROM UserInfo WHERE id = ?", new String[]{UserInfo.getUserId()});

                        cursor.moveToNext();
                        //물건을 구매한 회원의 포인트를 가져온다.
                        int point = cursor.getInt(0);

                        point -= OlderProduct.point;//포인트를 사용한 만큼 포인트를 차감시켜준다.

                        database = helper.getWritableDatabase();//이제 회원의 포인트 내역을 갱신한다.

                        database.execSQL("UPDATE UserInfo SET point="+point + " WHERE id = '" + UserInfo.getUserId()+"'");

                        //상품구매를 다 했으면 회원에게 포인트를 적립해 주어야 한다. 5%를 적립해 준다.


                        helper = new MySQLiteHelper(getApplicationContext());
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




                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {

                        Log.d("cancel", message);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Log.d("error", message);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                Log.d("close", "close");
                            }
                        })
                .request();
    }
}
