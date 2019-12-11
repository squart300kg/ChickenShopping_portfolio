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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Cart extends AppCompatActivity implements Adapter_Cart.ClickListener  {
    String name_1;
    int image_1;
    String option;
    int price;
    int count;

    Gson gson;
    ArrayList<HashMap<String, CartData>> user_cart;
    HashMap<String, CartData> one_cart;

    Adapter_Cart adapter_cart;
    CartData cartData;

    Button goToBy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        goToBy = findViewById(R.id.goToBuy);//구매하기 버튼에 대한 뷰 위젯을 가져온다.
        if(UserInfo.isLogin == false) {//만약 회원이 로그인중이 아니라면
            //장바구니에 왔을 때, 물건 구매하기를 없앤다.
            goToBy.setVisibility(View.GONE);
        }else{
            goToBy.setVisibility(View.VISIBLE);
        }
        //이전 액티비티가 전달한 product 정보를 수신한다.
        Log.i("장바구니 갯수", UserInfo.cartList.size() + "개");
        Intent intent = getIntent();
        if(!intent.getExtras().getBoolean("main")) {//만약 지금 이 장바구니 액티비티가 메인으로부터 온 것이 아니라면...
            image_1 = intent.getExtras().getInt("sosizi_list1_img");
            //Log.i("이미지", Integer.toString(image_1));
            name_1 = intent.getExtras().getString("sosizi_list1_name");
            //Log.i("이름", name_1);
            option = intent.getExtras().getString("option");
           // Log.i("옵션", option);
            count = Integer.parseInt(intent.getExtras().getString("count"));
           // Log.i("갯수", Integer.toString(count));
            price = Integer.parseInt(intent.getExtras().getString("price"));
          //  Log.i("가격", Integer.toString(price));

            //수신한 정보를 DTO에 담는다.
            cartData = new CartData();

            cartData.setName(name_1);
            cartData.setImg(image_1);
            cartData.setOption(option);
            cartData.setCount(Integer.toString(count));
            cartData.setPrice(Integer.toString(price));

            //한 유저가 장바구니에 제품을 하나 담음
            //UserInfo.cartList.add(cartData);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UserInfo.cartList.clear();
    }



    @Override
    protected void onResume() {
        super.onResume();
       //장바구니에서 불러올라고 할 때, DB에서 데이터를 가져온다.
        //UserInfo.cartData => 현재 장바구니 데이터들을 담고있는 리스트 변수

        //Cart 저장소에서 데이터를 가져온다.
        SharedPreferences sp = getSharedPreferences("Cart", MODE_PRIVATE);
        //Cart 저장소 안에 있는 데이터 중, cartStr 데이터를 가져온다.
        String strContact = sp.getString("cartStr", "");
        //리스트 형태의 데이터를 가져오기 위한 사전준비 작업이다 - Type클래스를 정의한다.
        Type listType = new TypeToken<ArrayList<HashMap<String, CartData>>>() {}.getType();
        gson = new Gson();

        //저장소로부터 데이터를 가져와서 초기화한다.
        user_cart = gson.fromJson(strContact,listType);

        if(strContact.equals("")){//만약 장바구니에 물품이 없다면 빈 객체를 새로 만들어라.
            user_cart = new ArrayList();
        }
        Log.i("Cart - 장바구니 DB갯수 : ",user_cart.size()+" ");
        Log.i("Cart - 장바구니 User갯수 : ",UserInfo.cartList.size()+" ");
        for(int i = 1 ; i <= user_cart.size() ; i ++){
            //유저의 아이디가 UserInfo.getUserId()와 일치한다면
            //UserInfo.cartList에 장바구니 목록을 추가한다.
            Log.i("Cart : ","물품 데이터를 장바구니에 뿌려주기");


            //저장소에 있는 장바구니에 대한 데이터 하나를 index 순서에 따라 one_cart에 저장한다.
            //Key - 유저 아이디,  Value - 구매한 물품 1개
            one_cart = user_cart.get(i - 1);

            //하나의 Hash Key값인 유저아이디가 로그인한 아이디( = UserInfo.getUserId() )와 일치한다면 그 정보를 UserInfo.cartList에 넣는다.
            if(one_cart.containsKey(UserInfo.getUserId())){
                //이로써 UserInfo.cartList에는 한명의 회원이 담은 물품 목록들이 모두 들어가게 된다.
                //ArrayList<CartData>
                UserInfo.cartList.add(one_cart.get(UserInfo.getUserId()));
            }
        }

        //만약  로그인중인 유저의 장바구니 갯수가 0이라면 바로구매 버튼을 없앤다.
        if(UserInfo.cartList.size() == 0){
            goToBy.setVisibility(View.GONE);
        }else{
            goToBy.setVisibility(View.VISIBLE);
        }
        Log.i("Cart - position갯수:",UserInfo.cartList.size()+"개");
        for(int i = 1 ; i <= UserInfo.cartList.size() ; i ++){
            Log.i("Cart - 리사이클러 들가기 전", UserInfo.cartList.get(i - 1).getName());
        }
        //장바구니 리스트에 한명의 회원이 담은 모든 물품들을 표시해준다.
        adapter_cart = new Adapter_Cart(this, UserInfo.cartList);
        adapter_cart.setOnItemClick(Cart.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_cart);

        //총 주문금액을 알려줌줌
        int count = 0;
        int lastPrice = 0;
        int totalPrice = 0;
        for(int i = 1 ; i <= UserInfo.cartList.size() ; i ++){
            price = Integer.parseInt(UserInfo.cartList.get(i - 1).getPrice());
            count = Integer.parseInt(UserInfo.cartList.get(i - 1).getCount());
            lastPrice = price * count;
            totalPrice += lastPrice;
        }
        TextView cart_total_price = findViewById(R.id.cart_total_price);
        cart_total_price.setText("상품 금액 : " + totalPrice + "원");

        TextView last_price = findViewById(R.id.last_price);
        //총 상품금액을 알려줌
        if(totalPrice < 50000){//상품금액이 5만원 미만이면 배송비가 추가된다.
            TextView delivery_charge = findViewById(R.id.delivery_charge);
            delivery_charge.setText("배송비  2500원");
            last_price.setText("총 주문 금액 : " + (totalPrice + 2500) + "원");
            if(totalPrice == 0){
                delivery_charge.setText("배송비  0원");
                cart_total_price.setText("상품 금액 : " + totalPrice + "원");
                last_price.setText("총 주문 금액 : " + totalPrice + "원");
            }
        }else{
            TextView delivery_charge = findViewById(R.id.delivery_charge);
            delivery_charge.setText("배송비  0원");
            last_price.setText("총 주문 금액 : " + totalPrice + "원");
        }
    }

    public void goToBuy(View view){
        Intent intent = new Intent(getApplicationContext(), Buy.class);
        intent.putExtra("option", option);
        intent.putExtra("price", Integer.toString(price));
        intent.putExtra("sosizi_list1_name", name_1);
        intent.putExtra("sosizi_list1_img", image_1);
        intent.putExtra("count", Integer.toString(count));

        startActivity(intent);
    }
    public void goToShopping(View view){
        finish();
    }

    @Override
    public void OnDeleteClick(int position) {
        //position 변수를 이용하여 DB의 Cart 데이터를 삭제할 수 있어야 한다.
        ArrayList<HashMap<String, CartData>> one_user_cart = new ArrayList();

        //ArrayList의 경우 삭제할 때, 앞에서부터 삭제하면 인덱스가 앞으로 당겨지는 현상으로 인해, 모든 데이터를 검사할 수 없는 현상이 발생한다. 따라서 역순으로 한다.
        for(int i = user_cart.size() ; i >= 1 ; i --){
            //Cart DB로부터 가져온 데이터들의 key값이 만약 로그인중인 아이디라면 다음 조건문을 실행한다.
            if(user_cart.get(i - 1).containsKey(UserInfo.getUserId())){
                //데이터 하나를 임시변수에다 저장하고(Key : Id, Value : CartData)
                one_cart = user_cart.get(i - 1);
                //이를 리스트에 추가한다.
                one_user_cart.add(one_cart);
                //추가한 후, Cart DB로부터 가져온 해당 데이터를 지운다.(최종적으로 DB에 넣을 데이터이다)
                user_cart.remove(i - 1);
                //이로써 user_cart에는 로그인중인 아이디에 대한 값이 모두 지워졌다.
            }
        }
        //이로써 one_user_cart에 담긴 데이터는 UserInfo.cartList에 담긴 데이터와 순서와 내용이 동일하게 되었다.
        //인덱스도 똑같으므로 position에 맞춰서 지워준다.
        one_user_cart.remove(position);

        //이제부터 DB에 수정된 데이터를 넣는 작업을 할것이다.
        for(int i = 1 ; i <= one_user_cart.size() ; i ++){
            //하나의 장바구니 변수를 초기화 한다
            one_cart = one_user_cart.get(i - 1);
            //이를 장바구니 변수 데이터를 리스트 배열에 넣는다. 이로써 최종적으로
            //로그인중인 아이디 key값의 데이터 - 1개의 데이터가 다시 들어가게 된다.
            user_cart.add(one_cart);
        }
        Type listType = new TypeToken<ArrayList<HashMap<String, CartData>>>(){}.getType();
        String cartStr = gson.toJson(user_cart,listType);

        SharedPreferences sp = getSharedPreferences("Cart",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("cartStr",cartStr);
        editor.commit();

        Toast.makeText(this, position + "번째 삭제 클릭", Toast.LENGTH_SHORT).show();
        UserInfo.cartList.remove(position);


        //총 주문금액을 알려줌줌
        int count = 0;
        int lastPrice = 0;
        int totalPrice = 0;
        for(int i = 1 ; i <= UserInfo.cartList.size() ; i ++){
            price = Integer.parseInt(UserInfo.cartList.get(i - 1).getPrice());
            count = Integer.parseInt(UserInfo.cartList.get(i - 1).getCount());
            lastPrice = price * count;
            totalPrice += lastPrice;
        }
        TextView cart_total_price = findViewById(R.id.cart_total_price);
        cart_total_price.setText("상품 금액 : " + totalPrice + "원");

        TextView last_price = findViewById(R.id.last_price);
        //총 상품금액을 알려줌
        if(totalPrice < 50000){//상품금액이 5만원 미만이면 배송비가 추가된다.
            TextView delivery_charge = findViewById(R.id.delivery_charge);
            delivery_charge.setText("배송비  2500원");
            last_price.setText("총 주문 금액 : " + (totalPrice + 2500) + "원");
            if(totalPrice == 0){
                delivery_charge.setText("배송비  0원");
                cart_total_price.setText("상품 금액 : " + totalPrice + "원");
                last_price.setText("총 주문 금액 : " + totalPrice + "원");
            }
        }else{
            last_price.setText("총 주문 금액 : " + totalPrice + "원");
        }
        //물품 삭제를 하다가 물품 갯수가 0개가 되면 구매하기 버튼을 없앤다.
        if(UserInfo.cartList.size() == 0){
            goToBy.setVisibility(View.GONE);
        }else{
            goToBy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnUpdateClick(int position){
        //장바구니에 담긴 물건 하나를 수정하기 위해 들어왔다.
        Log.i("OnUpdateClick-position",position+"");
        Toast.makeText(this, position + "번째 업데이트 클릭", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Update.class);

        //장바구니 액티비티에서 선택된 상품의 속성들을 불러온다.
        int img = UserInfo.cartList.get(position).getImg();//선택된 상품의 이미지
        Log.i("OnUpdateClick-img",img+"");
        String name = UserInfo.cartList.get(position).getName();//선택된 상품의 이름
        Log.i("OnUpdateClick-name",name+"");
        String update_count = UserInfo.cartList.get(position).getCount();//선택된 상품의 갯수
        Log.i("OnUpdateClick_count",update_count+"");
        String option = UserInfo.cartList.get(position).getOption();//선택된 상품의 옵션
        Log.i("OnUpdateClick_option",option+"");

        //장바구니 액티비티에서 불러온 속성들은 업데이트 액티비티로 넘겨주기 위해, 상품 속성들을
        //intent로 넘겨주기 위한 작업을 한다.
        intent.putExtra("sosizi_list1_img", img);//선택된 상품의 이미지
        intent.putExtra("sosizi_list1_name", name);//선택된 상품의 이름
        intent.putExtra("update_count", update_count);//선택된 상품의 갯수
        intent.putExtra("option",option);//선택된 상품의 옵션
        intent.putExtra("position", position);//선택된 상품의 인덱스 번호

        //업데이트 액티비티로 인텐트 값을 넘겨주고 업데이트 액티비티를 띄워준다.
        startActivity(intent);

        //총 주문금액을 알려줌줌
        int count = 0;//하나의 상품에 대한 물품의 갯수를 의미한다.
        int lastPrice = 0;//물품 한개 자체의 가격을 의미한다.(물품 1개 가격 X 갯수)
        int totalPrice = 0;//배송비를 포함한 물품 총 가격을 의미한다.
        for(int i = 1 ; i <= UserInfo.cartList.size() ; i ++){//한명의 유저가 가지고 있는 물품 갯수만큼 반복한다.
            price = Integer.parseInt(UserInfo.cartList.get(i - 1).getPrice());//i번째, 한개의 물품 가격
            count = Integer.parseInt(UserInfo.cartList.get(i - 1).getCount());//i번째, 한개의 물품 갯수
            lastPrice = price * count;//한개 물품의 총 가격을 의미한다.(물품 X 갯수)
            totalPrice += lastPrice;//여러개의 물품을 누적한 총 가격을 의미한다.
        }
        TextView cart_total_price = findViewById(R.id.cart_total_price);
        cart_total_price.setText("상품 금액 : " + totalPrice + "원");//물품 총 가격을 알려준다.(배송비 제외)

        TextView last_price = findViewById(R.id.last_price);
        //총 상품금액을 알려줌
        if(totalPrice < 50000){//상품금액이 5만원 미만이면 배송비가 추가된다.
            TextView delivery_charge = findViewById(R.id.delivery_charge);
            delivery_charge.setText("배송비  2500원");
            last_price.setText("총 주문 금액 : " + (totalPrice + 2500) + "원");
            if(totalPrice == 0){//물품 가격 자체가 0원이면
                delivery_charge.setText("배송비  0원");//배송비가 없다,
                cart_total_price.setText("상품 금액 : " + totalPrice + "원");//0원
                last_price.setText("총 주문 금액 : " + totalPrice + "원");//0원
            }
        }else{
            last_price.setText("총 주문 금액 : " + totalPrice + "원");//물품의 가격이 0원이 아니고 5만원이 넘는 경우라면 그냥 물품 총 가격을 표시
        }
    }
}
