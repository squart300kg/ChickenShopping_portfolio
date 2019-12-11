package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class BuySosizi_1 extends AppCompatActivity {

    TextView name;
    ImageView img;
    String name_1;
    int img_1;
    String count_1;
    String option_2;
    String price_2;
    Intent intent;

    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstsosizi);

        TextView buy = findViewById(R.id.buySosizi_1);

        intent = getIntent();
        img = findViewById(R.id.sosizi_list1_img);
        name = findViewById(R.id.sosizi_list1_name);

        name_1 = intent.getExtras().getString("name");
        name.setText(name_1);

        img_1 = intent.getExtras().getInt("img");
        img.setImageResource(img_1);


    }
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void buySosizi(View view){

        //로그인 상태가 아닐시엔 로그인 페이지로 이동시간다.
        if(UserInfo.getUserId().equals(" ")){
            startActivity(new Intent(this, Popup_Request_Login.class));
            finish();
            return;
        }
        EditText count = findViewById(R.id.count);

        RadioButton option_1 = findViewById(R.id.option_1);
        RadioButton option_2 = findViewById(R.id.option_2);
        RadioButton option_3 = findViewById(R.id.option_3);
        RadioButton option_4 = findViewById(R.id.option_4);
        RadioButton option_5 = findViewById(R.id.option_5);
        Intent intent = new Intent(getApplicationContext(), Buy.class);

        intent.putExtra("count",count.getText().toString());
        count_1 = count.getText().toString();
        if(option_1.isChecked()){
            intent.putExtra("option","100g X 10개 (1KG)");
            intent.putExtra("price","20000");
        }else if(option_2.isChecked()){
            intent.putExtra("option","100g X 20개 (2KG)");
            intent.putExtra("price","40000");
        }else if(option_3.isChecked()){
            intent.putExtra("option","100g X 30개 (3KG)");
            intent.putExtra("price","60000");
        }else if(option_4.isChecked()){
            intent.putExtra("option","100g X 40개 (4KG)");
            intent.putExtra("price","80000");
        }else if(option_5.isChecked()){
            intent.putExtra("option","100g X 50개 (5KG)");
            intent.putExtra("price","100000");
        }

        intent.putExtra("sosizi_list1_name",name_1);
        intent.putExtra("sosizi_list1_img", img_1);
        intent.putExtra("from_buy",true);

        startActivity(intent);

    }

    public void goCart(View view){
        //로그인 상태가 아닐시엔 로그인 페이지로 이동시간다.
        if(UserInfo.getUserId().equals(" ")){
            Log.i("로그인 안했는데 Cart로옴","확인");
            startActivity(new Intent(this, Popup_Request_Login.class));
            finish();
            return;
        }

        //장바구니 담기 버튼을 누를 때, 데이터베이스에 저장해야 한다.
        CartData cartData = new CartData();
        HashMap<String, CartData> user_cart_one = new HashMap();
        ArrayList<HashMap<String, CartData>> user_cart_one_array = new ArrayList();
        ArrayList<HashMap<String, CartData>> user_cart = new ArrayList();

        EditText count = findViewById(R.id.count);

        RadioButton option_1 = findViewById(R.id.option_1);
        RadioButton option_2 = findViewById(R.id.option_2);
        RadioButton option_3 = findViewById(R.id.option_3);
        RadioButton option_4 = findViewById(R.id.option_4);
        RadioButton option_5 = findViewById(R.id.option_5);

        cartData.setName(name_1);
        cartData.setImg(img_1);
        cartData.setCount(count.getText().toString());

        Intent intent = new Intent(getApplicationContext(), Cart.class);

        intent.putExtra("count",count.getText().toString());
        count_1 = count.getText().toString();
        if(option_1.isChecked()){
            intent.putExtra("option","100g X 10개 (1KG)");
            intent.putExtra("price","20000");

            cartData.setOption("100g X 10개 (1KG)");
            cartData.setPrice("20000");
           // Log.i("BuySosizi : ","장바구니에 물건 투척 성공");
        }else if(option_2.isChecked()){
            intent.putExtra("option","100g X 20개 (2KG)");
            intent.putExtra("price","40000");

            cartData.setOption("100g X 20개 (2KG)");
            cartData.setPrice("40000");

            //Log.i("BuySosizi : ","장바구니에 물건 투척 성공");
        }else if(option_3.isChecked()){
            intent.putExtra("option","100g X 30개 (3KG)");
            intent.putExtra("price","60000");

            cartData.setOption("100g X 30개 (3KG)");
            cartData.setPrice("60000");
          //  Log.i("BuySosizi : ","장바구니에 물건 투척 성공");
        }else if(option_4.isChecked()){
            intent.putExtra("option","100g X 40개 (4KG)");
            intent.putExtra("price","80000");

            cartData.setOption("100g X 40개 (4KG)");
            cartData.setPrice("80000");
           // Log.i("BuySosizi : ","장바구니에 물건 투척 성공");
        }else if(option_5.isChecked()){
            intent.putExtra("option","100g X 50개 (5KG)");
            intent.putExtra("price","100000");

            cartData.setOption("100g X 50개 (5KG)");
            cartData.setPrice("100000");
            //Log.i("BuySosizi : ","장바구니에 물건 투척 성공");
        }
        intent.putExtra("sosizi_list1_name",name_1);
        intent.putExtra("sosizi_list1_img", img_1);

        gson = new GsonBuilder().create();
        String cartStr1 = gson.toJson(cartData , CartData.class);
        Log.i("cartStr : ",cartStr1);//여기에는 DTO가 담긴다!!!

        //사용자가 물건을 장바구니에 담으려고 할 때, user_cart에서 '해당 사용자가', '같은 품목'을 가지고 있으면
        //본래 가지고 있던 품목에서 갯수만 늘려준다.
        SharedPreferences sp = getSharedPreferences("Cart",MODE_PRIVATE);
        String strContact = sp.getString("cartStr","");
        Type listType = new TypeToken<ArrayList<HashMap<String, CartData>>>(){}.getType();
        gson = new GsonBuilder().create();
        if(!strContact.equals("")){
            //Array참조변수에 장바구니에 대한 모든 데이터를 가져온다.
            user_cart = gson.fromJson(strContact, listType);
        }

        for(int i = 1 ; i <= user_cart.size() ; i ++){//장바구니 저장소에 담긴 전체 갯수만큼 반복한다.
            //인덱스에 해당하는 한개의 데이터가 지금 현재 로그인한 회원의 것이라면 다음의 조건물을 실행한다.
            if(user_cart.get(i - 1).containsKey(UserInfo.getUserId())){
                //장바구니 정보를 담고있는 저장소로부터 한명의 회원이 가지고 있는 장바구니 정보를
                //부분적으로 추출하여 이를 배열로 따로 만든다..
                user_cart_one_array.add(user_cart.get(i - 1));//한명의 회원이 가지고 있는 장바구니 전체 품목
            }
        }

        //이제 한명의 회원이 가지고 있는 장바구니 전체 데이터들을 조건문으로 검사하여, 회원이 현재 담으려고
        //하는 물품이 원래 가지고 있던 품목과 같은것이 있는지 검사한다. 같은것이 있다면 원래 것에 갯수를 늘려주고
        //그게 아니라면 추가해 준다.
        String product_name;
        for(int i = 1 ; i <= user_cart_one_array.size() ; i ++){

            //회원이 원래 가지고 있던 장바구니 품목 1개를 의미한다.
            product_name = user_cart_one_array.get( i - 1).get(UserInfo.getUserId()).getName();


            //회원이 현재 담으려고 하는 장바구니 품목이 원래 가지고 있던 품목과 같은지 검사한다. 같다면 다음 조건문을 실행한다.
            if(cartData.getName().equals(product_name)){
                String count_1 = user_cart_one_array.get( i - 1 ).get(UserInfo.getUserId()).getCount();
                int count_2 = Integer.parseInt(count_1);//회원이 본래 가지고 있던 같은 물품의 갯수를 추출한다.
                count_2 += Integer.parseInt(cartData.getCount());//같은 품목의 갯수를 합산해 주었다.

                //이제 이 갯수를 저장소에 다시 넣어주면 된다.
                user_cart_one_array.get(i - 1).get(UserInfo.getUserId()).setCount(Integer.toString(count_2));

                //이로써 한명의 회원의 장바구니 정보는 모두 갱신되었다. 이제 저장소에 원래 저장되어 있던
                //회원의 장바구니 정보를 다 지우면 된다.
                for(int j = user_cart.size() ; j >= 1 ; j --){
                    //장바구니 저장소에 저장된 정보가 현재 로그인한 정보의 정보이면 모두 지운다
                    if(user_cart.get(j - 1).containsKey(UserInfo.getUserId())){
                        user_cart.remove(j - 1);
                    }
                }
                //이제 장바구니 저장소에 내가 갱신한 정보를 뒤에 붙여주기만 하면 된다.
                user_cart.addAll(user_cart_one_array);

                //이제 쉐어드 프리퍼런스에 직접 넣어주자.
                String cartStr = gson.toJson(user_cart, listType);


                SharedPreferences.Editor editor = sp.edit();
                Log.i("BuySosizi - 중복 저장 : ",cartStr);
                editor.putString("cartStr",cartStr);
                editor.commit();

                startActivity(intent);
                return;

            }
        }


        user_cart_one.put(UserInfo.getUserId(), cartData);//한명의 유저가 담은 모든 장바구니 품목이다.

        //1. 우선 먼저 장바구니에 있는 정보들을 전부다 불러온다.
        //2. 불러온 후에, 내가 장바구니에 담은 정보들을 불러온 정보 뒤에다가 붙인다.
        //3. 붙인 다음에 다시 DB에 넣는다.


        //저장소가 비어있지 않다면 다음 조건문을 시행한다.

        user_cart.add(user_cart_one);//모든 유저들의 장바구니 품목들이 담겨있는 변수이다.

        String cartStr = gson.toJson(user_cart, listType);


        SharedPreferences.Editor editor = sp.edit();
        Log.i("BuySosizi - 리스트 : ",cartStr);
        editor.putString("cartStr",cartStr);
        editor.commit();

        //DB에 담기 성공
        Log.i("장바구니 버튼을 눌렀을 때 : ","데이터 넣기 성공");
        startActivity(intent);


    }

    public void goToBallList3(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","ball");
        finish();
        startActivity(intent);
    }

    public void goToSteamList3(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","steam");
        finish();
        startActivity(intent);
    }

    public void goToSosiziList3(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","sosizi");
        finish();
        startActivity(intent);
    }

    public void goToStakeList3(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","stake");
        finish();
        startActivity(intent);
    }

    public void goToYukpoList3(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","yukpo");
        finish();
        startActivity(intent);
    }

    public void goTohunjeList3(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","hunje");
        finish();
        startActivity(intent);
    }

    public void goToRawList3(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","raw");
        finish();
        startActivity(intent);
    }

    public void goToSliceList3(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","slice");
        finish();
        startActivity(intent);
    }
}
