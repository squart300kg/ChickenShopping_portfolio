package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Update extends AppCompatActivity {

    Intent intent;
    int position;
    Gson gson;
    ArrayList<HashMap<String, CartData>> user_cart;
    HashMap<String, CartData> one_cart;
    CartData cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        intent = getIntent();

        ImageView img = findViewById(R.id.sosizi_list1_img);
        TextView name = findViewById(R.id.sosizi_list1_name);
        EditText number = findViewById(R.id.update_count);
        RadioButton option_1 = findViewById(R.id.option_update_1);
        RadioButton option_2 = findViewById(R.id.option_update_2);
        RadioButton option_3 = findViewById(R.id.option_update_3);
        RadioButton option_4 = findViewById(R.id.option_update_4);
        RadioButton option_5 = findViewById(R.id.option_update_5);

        img.setImageResource(intent.getExtras().getInt("sosizi_list1_img"));//장바구니 액티비티로부터 넘겨받은 상품 이미지 데이터다. 이를 설정한다.
        name.setText(intent.getExtras().getString("sosizi_list1_name"));//장바구니 액티비티로부터 넘겨받은 상품 이름 데이터이다. 이를 설정한다.
        number.setText(intent.getExtras().getString("update_count"));//장바구니 액티비티로부터 넘겨받은 상품 갯수 데이터이다. 이를 설정한다.

        String option = intent.getExtras().getString("option");
        Log.i("Update.java - :",option);
        if(option.equals("100g X 10개 (1KG)")){
            option_1.setChecked(true);
        }else if(option.equals("100g X 20개(2KG)")){
            option_1.setChecked(false);
            option_2.setChecked(true);
        }else if(option.equals("100g X 30개 (3KG)")){
            option_1.setChecked(false);
            option_3.setChecked(true);
        }else if(option.equals("100g X 40개 (4KG)")){
            option_1.setChecked(false);
            option_4.setChecked(true);
        }else if(option.equals("100g X 50개 (5KG)")){
            option_1.setChecked(false);
            option_5.setChecked(true);
        }

        position = intent.getExtras().getInt("position");//장바구니 액티비티로부터 넘겨받은 장바구니 물품 인덱스번호 데이터이다.

    }

    //수정하기 버튼을 눌렀을 때, 동작하는 메서드이다.
    public void update(View view) {
        //업데이트할 물품을 삭제해 주고 갱신 완료한 정보를 넣어주면 된다.
        //업데이트할때도 마찬가지로 Cart DB에 데이터를 한개만 삭제 후 다시 저장해주면 된다.
        SharedPreferences sp = getSharedPreferences("Cart", MODE_PRIVATE);
        //Cart 저장소 안에 있는 데이터 중, cartStr 데이터를 가져온다.
        String strContact = sp.getString("cartStr", "");

        //Key(유저아이디), Value(구매물품1개)를 리스트로 갖는 타입을 선언
        Type listType = new TypeToken<ArrayList<HashMap<String, CartData>>>() {}.getType();
        gson = new Gson();

        //DB에 장바구니에 대한 모든데이터를 가져온다.
        user_cart = gson.fromJson(strContact,listType);

        //Key(유저아이디), Value(구매물품 1개)를 리스트로 갖는 타입을 선언하는데,
        //해당 변수는 Key(유저아이디)가 모두 같은 Hash만 저장한다.
        //Ex) 'Squart300kg' Key값의 Hash만 저장한다.
        ArrayList<HashMap<String, CartData>> one_user_cart = new ArrayList();

        //ArrayList의 경우 삭제할 때, 앞에서부터 삭제하면 인덱스가 앞으로 당겨지는 현상으로 인해, 모든 데이터를 검사할 수 없는 현상이 발생한다.
        //따라서 역순으로 한다.
        for(int i = user_cart.size() ; i >= 1 ; i --){
            //Cart DB로부터 가져온 데이터들의 key값이 만약 로그인중인 아이디( = UserInfo.getUserId )라면 다음 조건문을 실행한다.
            if(user_cart.get(i - 1).containsKey(UserInfo.getUserId())){
                //해쉬 하나(Key - 유저 아이디 , Value - 장바구니 물품 1개)를 임시변수에다 저장하고
                one_cart = user_cart.get(i - 1);
                //한명의 유저가 소유하고 있는 장바구니 리스트 변수에 데이터를 추가한다.
                one_user_cart.add(one_cart);
                //추가한 후, Cart DB로부터 가져온 해당 데이터들을 모두 지운다.(최종적으로 재가공한 데이터들을 이 리스트변수에 넣을거다)
                //왜냐하면 지금 지운 모든 데이터들은 갱신해서 다시 저장될 것이기 때문이다.
                user_cart.remove(i - 1);
                //이로써 user_cart에는 로그인중인 아이디( = UserInfo.getUserId() )에 대한 장바구니 리스트 데이터가 모두 지워졌다.
            }
        }
        //이로써 one_user_cart에 담긴 데이터는 UserInfo.cartList에 담긴 데이터와 순서와 내용이 역순으로 되었다.
        //ArrayList배열 순서를 뒤집는다.
        //인덱스도 역순으로 적용시켜주면 되므로 position에 맞춰서 지워준다. (여기서 position의 의미는수정될 데이터의 인덱스라는 뜻이다.)
        Collections.reverse(one_user_cart);
        Collections.reverse(one_user_cart);
        Collections.reverse(one_user_cart);
        one_user_cart.remove(position);//여기서 직화구이향의 소시지 닭가슴살은 지워졌다.

        for(int i = 1 ; i <= one_user_cart.size() ; i ++){
            Log.i("Update 노가다 로그1 - ",one_user_cart.get(i - 1).get(UserInfo.getUserId()).getName());
        }

        Log.i("Update - position : ",position+"");
        //액티비티에 있는 물품 갯수 위젯뷰를 가져온다.
        EditText update_count = findViewById(R.id.update_count);

        RadioButton option_1 = findViewById(R.id.option_update_1);
        RadioButton option_2 = findViewById(R.id.option_update_2);
        RadioButton option_3 = findViewById(R.id.option_update_3);
        RadioButton option_4 = findViewById(R.id.option_update_4);
        RadioButton option_5 = findViewById(R.id.option_update_5);

        cart = new CartData();
        String name = intent.getExtras().getString("sosizi_list1_name");//장바구니 액티비티로부터 넘겨받은 상품 이름 데이터이다.
        cart.setName(name);
        int img = intent.getExtras().getInt("sosizi_list1_img");//장바구니 액티비티로부터 넘겨받은 상품 이미지 데이터이다.
        cart.setImg(img);
//        UserInfo.cartList.get(0).setCount(count.getText().toString());
        cart.setCount(update_count.getText().toString());
        if(option_1.isChecked()){
           // UserInfo.cartList.get(0).setOption("100g X 10개 (1KG)");
           // UserInfo.cartList.get(0).setPrice("20000");
            cart.setOption("100g X 10개 (1KG)");
            cart.setPrice("20000");
        }else if(option_2.isChecked()){
           // UserInfo.cartList.get(0).setOption("100g X 20개 (2KG)");
           // UserInfo.cartList.get(0).setPrice("40000");
            cart.setOption("100g X 20개 (2KG)");
            cart.setPrice("40000");
        }else if(option_3.isChecked()){
          //  UserInfo.cartList.get(0).setOption("100g X 30개 (3KG)");
           // UserInfo.cartList.get(0).setPrice("60000");
            cart.setOption("100g X 30개 (3KG)");
            cart.setPrice("60000");
        }else if(option_4.isChecked()){
          //  UserInfo.cartList.get(0).setOption("100g X 40개 (4KG)");
           // UserInfo.cartList.get(0).setPrice("80000");
            cart.setOption("100g X 40개 (4KG)");
            cart.setPrice("80000");
        }else if(option_5.isChecked()){
           // UserInfo.cartList.get(0).setOption("100g X 50개 (5KG)");
           // UserInfo.cartList.get(0).setPrice("100000");
            cart.setOption("100g X 50개 (5KG)");
            cart.setPrice("100000");
        }
        //cart변수에는 갱신한 값이 잘 들어갔다.

        //one_cart에 값이 남아있으므로 값을 비워준다.
        one_cart = new HashMap();
        one_cart.put(UserInfo.getUserId(), cart);//Key - 유저 아이디와, Value - 장바구니 물품 1개( 수정이 완료된 물품 )를 담는다.
        one_user_cart.add(one_cart);//수정 완료된 해쉬값을 배열에 넣는다.
        user_cart.addAll(one_user_cart);//여러 아이디를 Key값으로 가지고 있는 해쉬( 모든 회원의 장바구니 데이터 ) 에 나의 장바구니 리스트를 넣는다.

        String cartStr = gson.toJson(user_cart,listType);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString("cartStr",cartStr);
        editor.commit();
        finish();

        UserInfo.cartList.clear();
    }
}
