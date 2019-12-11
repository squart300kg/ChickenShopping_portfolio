package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_ManageDeliverySee extends RecyclerView.Adapter {
    ArrayList<BuyData> buyData;
    public Adapter_ManageDeliverySee(ArrayList<BuyData> buyData) {
        this.buyData = buyData;
    }

    class ItemView extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name;
        TextView option;
        TextView count;
        TextView price;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.manage_delivery_see_row_img);
            name = itemView.findViewById(R.id.manage_delivery_see_row_name);
            option = itemView.findViewById(R.id.manage_delivery_see_row_option);
            count = itemView.findViewById(R.id.manage_delivery_see_row_count);
            price = itemView.findViewById(R.id.manage_delivery_see_row_price);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_manage_delivery_see_row, parent, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_ManageDeliverySee.ItemView itemView = (Adapter_ManageDeliverySee.ItemView)holder;

        itemView.img.setImageResource(buyData.get(position).getImg());
        itemView.name.setText(buyData.get(position).getName());
        itemView.option.setText(buyData.get(position).getOption());
        itemView.count.setText(buyData.get(position).getCount()+"개 | ");
        itemView.price.setText(Integer.parseInt(buyData.get(position).getPrice()) * Integer.parseInt(buyData.get(position).getCount()) + "원");


    }

    @Override
    public int getItemCount() {
        return buyData.size();
    }
}
