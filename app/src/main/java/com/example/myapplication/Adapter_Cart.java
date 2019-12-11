package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Cart  extends RecyclerView.Adapter{

    ArrayList<CartData> cartList;
    ClickListener cListener;
    int last_price = 0;
    Context context;

    public interface ClickListener{
        void OnUpdateClick(int posistion);
        void OnDeleteClick(int position);
    }
    public void setOnItemClick(ClickListener cListener){
        this.cListener = cListener;
    }

    Adapter_Cart(Context context, ArrayList<CartData> list){
        this.cartList = list;
        this.context = context;
    }
    class ItemView extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name;
        TextView option;
        TextView count;
        TextView price;
        Button delete;
        Button update;

        public ItemView(@NonNull View itemView) {
            super(itemView);

            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
            img = itemView.findViewById(R.id.cart_img_1);
            name = itemView.findViewById(R.id.cart_name_1);
            option = itemView.findViewById(R.id.cart_option_1);
            count = itemView.findViewById(R.id.cart_count_1);
            price = itemView.findViewById(R.id.cart_total_price_1);


        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Adapter_Cart.ItemView itemView = (Adapter_Cart.ItemView)holder;

        //장바구니 목록을 뿌려준다.
        itemView.img.setImageResource(cartList.get(position).getImg());
        itemView.name.setText(cartList.get(position).getName());
        itemView.option.setText(cartList.get(position).getOption());
        itemView.count.setText(cartList.get(position).getCount() + "개 | ");
        itemView.price.setText((Integer.parseInt(cartList.get(position).getPrice()) * Integer.parseInt(cartList.get(position).getCount())) + "원");

        if(cListener != null){
            final int pos = holder.getAdapterPosition();
            itemView.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cListener.OnDeleteClick(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, cartList.size());
                }
            });
            itemView.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cListener.OnUpdateClick(pos);
                    notifyItemChanged(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
