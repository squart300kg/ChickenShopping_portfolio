package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_OlderHistory_2  extends RecyclerView.Adapter{
    ArrayList<BuyData> buyData;
    ClickListener cListener;
    String nowDate;

    public interface ClickListener{
        void OnReviewClick(BuyData buyData, String nowDate,int pos);
    }
    public void setOnItemClick(ClickListener cListener){
        this.cListener = cListener;
    }
    public Adapter_OlderHistory_2(ArrayList<BuyData> buyData, String nowDate) {
        this.buyData = buyData;
        this.nowDate = nowDate;
    }

    class ItemView extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name;
        TextView option;
        TextView count;
        TextView price;
        TextView review;
        public ItemView(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.older_history_img_1);
            name = itemView.findViewById(R.id.older_history_name_1);
            option = itemView.findViewById(R.id.older_history_option_1);
            count = itemView.findViewById(R.id.older_history_count_1);
            price = itemView.findViewById(R.id.older_history_total_price_1);
            review = itemView.findViewById(R.id.older_history_review);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.olderhistory_row_2, parent, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_OlderHistory_2.ItemView itemView = (Adapter_OlderHistory_2.ItemView)holder;

        Log.i("position : ",position + ".");
        itemView.img.setImageResource(buyData.get(position).getImg());
        Log.i("주문 내역 2 - 이미지 : ", buyData.get(position).getImg()+".");

        itemView.name.setText(buyData.get(position).getName());
        Log.i("주문 내역 2 - 이름 : ", buyData.get(position).getName()+".");

        itemView.option.setText(buyData.get(position).getOption());
        Log.i("주문 내역 2 - 옵션 : ", buyData.get(position).getOption()+".");

        itemView.count.setText(buyData.get(position).getCount() + "개 | ");
        Log.i("주문 내역 2 - 갯수 : ", buyData.get(position).getCount()+".");

        itemView.price.setText(Integer.parseInt(buyData.get(position).getPrice()) * Integer.parseInt(buyData.get(position).getCount()) + "원");
        Log.i("주문 내역 2 - 가격 : ", buyData.get(position).getPrice()+".");

        if(!buyData.get(position).isReview()){
            itemView.review.setText("후기 작성");
            //물품 변수 안에 isReview를 검사해서 리뷰가 쓰여졌다면 "후기 작성"을 표시해주고 아니면 표시하지 말라.
            //여기에서 DB값을 꺼내와서 검사를 하면 된다.
        }


        if(cListener != null){
            final int pos = position;
            final Adapter_OlderHistory_2.ItemView itemView_1 = itemView;
            itemView_1.review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cListener.OnReviewClick(buyData.get(pos), nowDate, pos);

                    itemView_1.review.setVisibility(View.GONE);
                    buyData.get(pos).isReview = true;
                    //notifyItemRemoved(pos);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return buyData.size();
    }


}
