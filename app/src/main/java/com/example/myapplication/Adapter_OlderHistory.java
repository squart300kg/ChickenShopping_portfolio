package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_OlderHistory  extends RecyclerView.Adapter implements Adapter_OlderHistory_2.ClickListener {
    HashMap<String, ArrayList<BuyData>> buy;
    ArrayList<String> date;
    Adapter_OlderHistory_2 adapter_olderHistory_2;
    Context context;
    ArrayList<BuyDTO> buyDTOArray = new ArrayList();
    ArrayList<BuyDTO> buyDTOArray2 = new ArrayList();
    Adapter_OlderHistory(HashMap<String, ArrayList<BuyData>> buy, Context context){
        this.buy = buy;
        this.context = context;

        Set<String> set = buy.keySet();
        date = new ArrayList(set.size());
        for (String key : set) {
            date.add(key);
            Log.i("정렬되기 전의 날짜 - ",key);
        }
       Collections.sort(date);
        Collections.reverse(date);
        for(int i = 1 ; i <= date.size() ; i ++){
            Log.i("정렬된 후의 날짜 - ",date.get(i - 1));
        }

        //DB로부터 구매내역에 관한 데이터를 받아와 저장하기 위한 변수이다.
       buyDTOArray2 = new ArrayList();
        //구매내역 하나를 저장할 일회용 변수이다.
        BuyDTO buyDTO = new BuyDTO();
        //BuyDTO 저장소에서 데이터를 가져온다.
        SharedPreferences sp = context.getSharedPreferences("BuyDTO", MODE_PRIVATE);
        //BuyDTO 저장소 안에 있는 데이터 중, buyDTOStr 데이터를 가져온다.
        String strContact = sp.getString("buyDTOStr", "");

        //BuyDTO데이터들을 저장할 배열 변수 형식을 지정해준다.
        Type listType = new TypeToken<ArrayList<BuyDTO>>(){}.getType();
        gson = new GsonBuilder().create();

        //DB로부터 불러온 BuyDTO 데이터가 있다면  그 데이터를 buyDTOArray로 가리킨다.
        if(!strContact.equals("")){
            //Array참조변수에 장바구니에 대한 모든 데이터를 가져온다.
            buyDTOArray = gson.fromJson(strContact, listType);
        }
        for(int i = 1 ; i <= buyDTOArray.size() ; i ++){
            if(buyDTOArray.get(i - 1).getId().equals(UserInfo.getUserId())){//DB로부터 꺼내온 구매내역 정보의 아이디가 로그인중인 아이디와 일치한다면 다음 조건문을 시행한다.
                //한 유저의 구매내역 정보들을 buyDTOArray2 변수에 담도록 한다.
                buyDTOArray2.add(buyDTOArray.get(i - 1));
            }
        }
        //이로써 buyDTOArray2변수에는 현재 로그인중인 아이디인 유저의 구매내역 정보들이 모두 담기게 되었다.

    }
    Gson gson;
    ArrayList<HashMap<String, HashMap<String, ArrayList<BuyData>>>> user_buy;
    HashMap<String, HashMap<String, ArrayList<BuyData>>> user_buy_one = new HashMap();
    HashMap<String, ArrayList<BuyData>> user_buy_one_one;

    @Override
    public void OnReviewClick(BuyData buyData, String nowDate, int pos) {
        //여기서 후기쓰기를 어떤것을 클릭하느냐에 따라 해당 물품의 리뷰여부를 체크한다.
        UserInfo.buy.get(nowDate).get(pos).isReview = true;
        //=====================================================================================================
        //1. 구매목록 DB에 있는 데이터를 모두 불러온다.( Buy -> buyStr)
        //2. 구매목록 DB에서 로그인중인 아이디와 일치하는 Key값을 가진 데이터들을 추린다.
        //3. 추린 데이터를 DB에서 삭제한다.
        //4. 생성자로부터 받아온 데이터는(=UserInfo.buy) 한 유저가 구매한 목록의 모든 것이므로 이 데이터를 수정하여 최종적으로 DB에 추가해 주면 된다.
        //5. 수정할 정보는 BuyData의 isReview의 값을 false로 바꾸는 것이다.
        //======================================================================================================

        //1. 구매목록 DB에 있는 구매 데이터를 모두 불러온다( Buy -> buyStr)
        String strContact = UserInfo.getStrContact();

        //2. 구매목록 DB에서 로그인중인 아이디와 일치하는 Key값을 가진 데이터들을 추린다.
        Type listType = new TypeToken<ArrayList<HashMap<String, HashMap<String, ArrayList<BuyData>>>>>() {}.getType();
        gson = new GsonBuilder().create();

        if(!strContact.equals("")){
            user_buy = gson.fromJson(strContact, listType);
        }

        //이미 한명의 유저가 구매한 모든 구매 목록이 UserInfo.buy에 들어있으므로
        //해당 반복문에서는 아이디가 로그인중인 아이디인 회원이라면 DB에서 조건을 삭제하도록 한다.
        for(int i = user_buy.size() ; i >= 1 ; i --){
            //해당하는 key값이 로그인값과 같은지 검사한다.
            if(user_buy.get(i - 1).containsKey(UserInfo.getUserId())){ //만약 로그인중인 아이디와 같다면! 다음 작업을 수행한다.

                //구매내역의 정보를 삭제한다. && 해당 변수는 DB에 다시 들어갈 정보이다.
                user_buy.remove(i - 1);
            }
        }//3. 추린 데이터를 DB에서 삭제한다.

        //=====================================================================================================
        //지금부터 역순으로 데이터를 다시 담는다.
        //수정된 데이터를 Value값으로 해서 Key값을 아이디로 설정한다.
        user_buy_one.put(UserInfo.getUserId(), UserInfo.buy);

        user_buy.add(user_buy_one);

        strContact = gson.toJson(user_buy, listType);
        SharedPreferences sp = context.getSharedPreferences("Buy", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //쉐어드에 구매 내역을 최종적으로 저장한다.
        editor.putString("buyStr", strContact);
        editor.commit();
        //후기를 쓰는곳으로 이동한다.
        Intent intent = new Intent(context, ReviewWrite.class);
        intent.putExtra("img",buyData.getImg());
        intent.putExtra("name",buyData.getName());
        intent.putExtra("update",false);
        context.startActivity(intent);
    }

    class ItemView extends RecyclerView.ViewHolder{

        TextView date;
        TextView delivery;
        RecyclerView recyclerView;
        public ItemView(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.older_history_date);
            delivery = itemView.findViewById(R.id.older_history_delivery);
            recyclerView = itemView.findViewById(R.id.recyclerView_olderHistory_2);

        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.olderhistory_row, parent, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_OlderHistory.ItemView itemView = (Adapter_OlderHistory.ItemView)holder;

        //구매내역을 최신순으로 재배열하기 위해서 position변수를 재정의 한다.
        int position1 = buy.size() - 1 - position;

        String nowDate;
        ArrayList<BuyData> buyList;
        nowDate = date.get(position);
        String nowDate1 = date.get(position);

        String year = nowDate.substring(0,4);
        String month = nowDate.substring(4,6);
        String day = nowDate.substring(6,8);
        String hour = nowDate.substring(8,10);
        String minute = nowDate.substring(10,12);
        String second = nowDate.substring(12,14);

        nowDate = year + "/" + month +"/" + day + " - " + hour + ":" + minute + ":" + second;
        Log.i("Adapter 내림차순 - ",nowDate);
        itemView.date.setText(nowDate);//고객이 물품을 언제 주문했는지를 알려주는 시간이다.
        itemView.delivery.setText(buyDTOArray2.get(position1).getDelivery());//고객의 현재 주문상태를 알려준다.
        Log.i("주문 내역 1 - 시간 : ", nowDate);

        buyList = buy.get(date.get(position));
        adapter_olderHistory_2 = new Adapter_OlderHistory_2(buyList, nowDate1);
        adapter_olderHistory_2.setOnItemClick(this);
        itemView.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        itemView.recyclerView.setAdapter(adapter_olderHistory_2);
        adapter_olderHistory_2.notifyDataSetChanged();
        Log.i("주문 내역 어댑터 2 : ","끝");
    }

    @Override
    public int getItemCount() {
        return buy.size();
    }
}
