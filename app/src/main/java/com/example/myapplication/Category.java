package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class Category extends AppCompatActivity implements Adapter.ClickListener {

    Adapter adapter;
    ArrayList<SosiziList> sosiziList;
    Intent intent;
    TextView categoryname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_sosizi);

        intent = getIntent();
        //상품 하나의 상단 타이틀바 이름을 적어준다.
        categoryname = findViewById(R.id.categoryname);

        switch (intent.getExtras().getString("from")){
            case "sosizi":
                insertSosiziList();//sosiziList참조변수에 Call-By-Reference로 변수를 초기화해 주었다.
                categoryname.setText("소시지 닭가슴살");
                Log.i("카테고리 : ","소시지");
                break;
            case "stake":
                insertStakeList();
                categoryname.setText("스테이크 닭가슴살");
                Log.i("카테고리 : ","스테이크");
                break;
            case "steam":
                insertSteamList();
                categoryname.setText("스팀 닭가슴살");
                Log.i("카테고리 : ","스팀");
                break;
            case "ball":
                insertBallList();
                categoryname.setText("볼 닭가슴살");
                Log.i("카테고리 : ","볼");
                break;
            case "raw":
                insertRawList();
                categoryname.setText("생 닭가슴살");
                break;
            case "yukpo":
                insertYukpoList();
                categoryname.setText("육포 닭가슴살");
                break;
            case "hunje":
                insertHunjeList();
                categoryname.setText("훈제 닭가슴살");
                break;
            case "slice":
                insertSliceList();
                categoryname.setText("슬라이스 닭가슴살");
                break;
            case "all":
                insertAllList();
                categoryname.setText("전체 품목");
        }

        adapter = new Adapter(sosiziList);
        adapter.setOnItemClick(Category.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    public void insertAllList() {
        sosiziList = new ArrayList();

        sosiziList.add(new SosiziList("(맛있닭)직화구이 향의 소시지 닭가슴살", R.drawable.sosizi_list1));
        sosiziList.add(new SosiziList("(파워닭)고소~한 치즈 향의 소시지 닭가슴살", R.drawable.sosizi_list2));
        sosiziList.add(new SosiziList("(또먹닭)네가지 맛의 소시지 닭가슴살", R.drawable.sosizi_list3));
        sosiziList.add(new SosiziList("(도마닭)근육이 올라오는 소시지 닭가슴살", R.drawable.sosizi_list4));

        sosiziList.add(new SosiziList("(맛있닭)직화구이 향의 소시지 닭가슴살", R.drawable.sosizi_list1));
        sosiziList.add(new SosiziList("(파워닭)고소~한 치즈 향의 소시지 닭가슴살", R.drawable.sosizi_list2));
        sosiziList.add(new SosiziList("(또먹닭)네가지 맛의 소시지 닭가슴살", R.drawable.sosizi_list3));
        sosiziList.add(new SosiziList("(도마닭)근육이 올라오는 소시지 닭가슴살", R.drawable.sosizi_list4));

        sosiziList.add(new SosiziList("(맛있닭)6성급 레스토랑급의 스테이크 닭가슴살", R.drawable.stake2));
        sosiziList.add(new SosiziList("(맛있닭)햄버그 모양의 근육 빵빵 닭가슴살",R.drawable.stake));
        sosiziList.add(new SosiziList("(근육닭)3대500이 가능해지는 햄버그 닭가슴살",R.drawable.stake3));
        sosiziList.add(new SosiziList("(다요트닭)살이 쭉쭉 빠지는 햄버그 닭가슴살",R.drawable.stake4));

        sosiziList.add(new SosiziList("(맛있닭)퍽퍽하지 않고 촉촉한 스팀 닭가슴살", R.drawable.steam2));
        sosiziList.add(new SosiziList("(부드닭)닭가슴살같지 않은 촉촉 닭가슴살",R.drawable.steam));
        sosiziList.add(new SosiziList("(닭닭닭)정말 닭다리를 먹는 식감의 닭가슴살",R.drawable.steam3));
        sosiziList.add(new SosiziList("(닭가닭)세상에 이런일이! 에 출연한 닭가슴살",R.drawable.subide2));

        sosiziList.add(new SosiziList("(슬라이스닭)1NM의 얇기의 슬라이스 닭가슴살",R.drawable.slice));
        sosiziList.add(new SosiziList("(샤브닭)샤브샤브를 먹는것 같은 얇은 닭가슴살",R.drawable.slice2));
        sosiziList.add(new SosiziList("(노퍽닭)슬라이스모양에 안퍽퍽한 닭가슴살",R.drawable.slice3));
        sosiziList.add(new SosiziList("(정말닭)정말 얇은 슬라이스 모양의 닭가슴살",R.drawable.slice5));

        sosiziList.add(new SosiziList("(신난닭)캠핑에서 훈제를 먹는맛의 닭가슴살",R.drawable.hunje));
        sosiziList.add(new SosiziList("(바베닭)바베큐를 먹는 맛의 훈제 닭가슴살",R.drawable.hunje2));
        sosiziList.add(new SosiziList("(신난닭)캠핑에서 분위기의 훈제맛의 닭가슴살",R.drawable.hunje3));
        sosiziList.add(new SosiziList("(캠핑닭)캠프파이어를 재현한 그 맛의 닭가슴살",R.drawable.hunje4));

        sosiziList.add(new SosiziList("(영화닭)영화를 보며 먹는 육포같은 닭가슴살",R.drawable.yukpo));
        sosiziList.add(new SosiziList("(회사닭)회사에서 열심히 만든 육포 닭가슴살",R.drawable.yukpo2));
        sosiziList.add(new SosiziList("(휴대닭)휴대하기가 편한 육포 닭가슴살",R.drawable.yukpo3));
        sosiziList.add(new SosiziList("(국산닭)국내산 닭을 사용한 육포 닭가슴살",R.drawable.yukpo4));

        sosiziList.add(new SosiziList("(저렴닭)조리가 번거롭지만 저렴한 생닭가슴살",R.drawable.raw));
        sosiziList.add(new SosiziList("(담백닭)닭본연의 맛을 살린 생닭가슴살",R.drawable.raw2));
        sosiziList.add(new SosiziList("(요리닭)다양한 요리가 가능한 생닭가슴살",R.drawable.raw3));
        sosiziList.add(new SosiziList("(닭닭닭)여러마리의 닭으로 만든 생닭가슴살",R.drawable.raw4));
    }


    public void insertSosiziList(){
        sosiziList = new ArrayList();

        sosiziList.add(new SosiziList("(맛있닭)직화구이 향의 소시지 닭가슴살", R.drawable.sosizi_list1));
        sosiziList.add(new SosiziList("(파워닭)고소~한 치즈 향의 소시지 닭가슴살", R.drawable.sosizi_list2));
        sosiziList.add(new SosiziList("(또먹닭)네가지 맛의 소시지 닭가슴살", R.drawable.sosizi_list3));
        sosiziList.add(new SosiziList("(도마닭)근육이 올라오는 소시지 닭가슴살", R.drawable.sosizi_list4));
    }
    public void insertBallList(){
        sosiziList = new ArrayList();

        sosiziList.add(new SosiziList("(맛있닭)네가지맛의 사각볼 닭가슴살", R.drawable.ball2));
        sosiziList.add(new SosiziList("(닭닭닭)사각사각사각 모양의 사각닭가슴살",R.drawable.ball));
        sosiziList.add(new SosiziList("(큐브닭)큐브 영화를 보며 먹는 큐브 닭가슴살",R.drawable.ball3));
        sosiziList.add(new SosiziList("(구기닭)구기종목 볼모양을 닮은 볼 닭가슴살",R.drawable.ball4));
    }
    public void insertStakeList(){
        sosiziList = new ArrayList();

        sosiziList.add(new SosiziList("(맛있닭)6성급 레스토랑급의 스테이크 닭가슴살", R.drawable.stake2));
        sosiziList.add(new SosiziList("(맛있닭)햄버그 모양의 근육 빵빵 닭가슴살",R.drawable.stake));
        sosiziList.add(new SosiziList("(근육닭)3대500을 찍을 수 있는 햄버그 닭가슴살",R.drawable.stake3));
        sosiziList.add(new SosiziList("(다요트닭)살이 쭉쭉 빠지는 햄버그 닭가슴살",R.drawable.stake4));
    }
    public void insertSteamList(){
        sosiziList = new ArrayList();

        sosiziList.add(new SosiziList("(맛있닭)퍽퍽하지 않고 촉촉한 스팀 닭가슴살", R.drawable.steam2));
        sosiziList.add(new SosiziList("(부드닭)닭가슴살같지 않은 촉촉 닭가슴살",R.drawable.subide));
        sosiziList.add(new SosiziList("(닭닭닭)정말 닭다리를 먹는 식감의 닭가슴살",R.drawable.steam3));
        sosiziList.add(new SosiziList("(닭가닭)세상에 이런일이! 에 출연한 닭가슴살",R.drawable.subide2));
    }
    public void insertSliceList() {
        sosiziList = new ArrayList();
        sosiziList.add(new SosiziList("(슬라이스닭)1NM의 얇기를 가진 슬라이스 닭가슴살",R.drawable.slice));
        sosiziList.add(new SosiziList("(샤브닭)샤브샤브를 먹는것 같은 얇은 닭가슴살",R.drawable.slice2));
        sosiziList.add(new SosiziList("(노퍽닭)슬라이스모양에 안퍽퍽한 닭가슴살",R.drawable.slice3));
        sosiziList.add(new SosiziList("(정말닭)정말 얇은 슬라이스 모양의 닭가슴살",R.drawable.slice5));
    }

    public void insertHunjeList() {
        sosiziList = new ArrayList();
        sosiziList.add(new SosiziList("(신난닭)캠핑에서 훈제를 먹는맛의 닭가슴살",R.drawable.hunje));
        sosiziList.add(new SosiziList("(바베닭)바베큐를 먹는 맛의 훈제 닭가슴살",R.drawable.hunje2));
        sosiziList.add(new SosiziList("(신난닭)캠핑에서 분위기의 훈제맛의 닭가슴살",R.drawable.hunje3));
        sosiziList.add(new SosiziList("(캠핑닭)캠프파이어를 재현한 그 맛의 닭가슴살",R.drawable.hunje4));
    }

    public void insertYukpoList() {
        sosiziList = new ArrayList();
        sosiziList.add(new SosiziList("(영화닭)영화를 보며 먹는 육포같은 닭가슴살",R.drawable.yukpo));
        sosiziList.add(new SosiziList("(회사닭)회사에서 열심히 만든 육포 닭가슴살",R.drawable.yukpo2));
        sosiziList.add(new SosiziList("(휴대닭)휴대하기가 편한 육포 닭가슴살",R.drawable.yukpo3));
        sosiziList.add(new SosiziList("(국산닭)국내산 닭을 사용한 육포 닭가슴살",R.drawable.yukpo4));
    }

    public void insertRawList() {
        sosiziList = new ArrayList();
        sosiziList.add(new SosiziList("(저렴닭)조리가 번거롭지만 저렴한 생닭가슴살",R.drawable.raw));
        sosiziList.add(new SosiziList("(담백닭)닭본연의 맛을 살린 생닭가슴살",R.drawable.raw2));
        sosiziList.add(new SosiziList("(요리닭)다양한 요리가 가능한 생닭가슴살",R.drawable.raw3));
        sosiziList.add(new SosiziList("(닭닭닭)여러마리의 닭으로 만든 생닭가슴살",R.drawable.raw4));
    }


    @Override
    public void OnItemClick(int position) {
        //어떠한 항목을 누르느냐에 따라 다른 이미지와 텍스트 네임이 인텐트로 전송된다.
        Intent intent = new Intent(getApplicationContext(), BuySosizi_1.class);

        intent.putExtra("name", sosiziList.get(position).getName());
        intent.putExtra("img", sosiziList.get(position).getImg());
        startActivity(intent);
    }

    public void goToBallList2(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","ball");
        finish();
        startActivity(intent);
    }

    public void goToSliceList2(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","slice");
        finish();
        startActivity(intent);

    }

    public void goToSosiziList2(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","sosizi");
        finish();
        startActivity(intent);
    }

    public void goToStakeList2(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","stake");
        finish();
        startActivity(intent);
    }

    public void goToYukpoList2(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","yukpo");
        finish();
        startActivity(intent);
    }

    public void goTohunjeList2(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","hunje");
        finish();
        startActivity(intent);
    }

    public void goToRawList2(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","raw");
        finish();
        startActivity(intent);
    }

    public void goToSteamList2(View view) {
        Intent intent = new Intent(this, Category.class);
        intent.putExtra("from","steam");
        finish();
        startActivity(intent);
    }
}