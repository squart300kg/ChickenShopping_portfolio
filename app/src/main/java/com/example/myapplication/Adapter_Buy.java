package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Buy  extends RecyclerView.Adapter{
    ArrayList<CartData> cartList;

    Adapter_Buy(ArrayList<CartData> list){
        this.cartList = list;
    }
    class ItemView extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name;
        TextView option;
        TextView count;
        TextView price;
        public ItemView(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.buy_img_1);
            name = itemView.findViewById(R.id.buy_name_1);
            option = itemView.findViewById(R.id.buy_option_1);
            count = itemView.findViewById(R.id.buy_count_1);
            price = itemView.findViewById(R.id.buy_total_price_1);


        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_row, parent, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_Buy.ItemView itemView = (Adapter_Buy.ItemView)holder;

        itemView.img.setImageResource(cartList.get(position).getImg());
        itemView.name.setText(cartList.get(position).getName());
        itemView.option.setText(cartList.get(position).getOption());
        itemView.count.setText(cartList.get(position).getCount() + "개 | ");
        itemView.price.setText((Integer.parseInt(cartList.get(position).getPrice()) * Integer.parseInt(cartList.get(position).getCount())) + "원");
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
