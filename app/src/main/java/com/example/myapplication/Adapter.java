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

public class Adapter extends RecyclerView.Adapter{

    ArrayList<SosiziList> sosiziList;
    ClickListener cListener;

    public interface ClickListener{
        void OnItemClick(int position);
    }

    Adapter(ArrayList<SosiziList> list){
        this.sosiziList = list;
    }

    public void setOnItemClick(ClickListener  cListener){
        this.cListener = cListener;
    }


    class ItemView extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name;
        LinearLayout list_item;

        public ItemView(@NonNull View itemView) {
            super(itemView);

            list_item = itemView.findViewById(R.id.list_item);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);


        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_sosizi_row, parent, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemView itemView = (ItemView)holder;

        itemView.img.setImageResource(sosiziList.get(position).getImg());
        itemView.name.setText(sosiziList.get(position).getName());

        if(cListener != null){
            final int pos = position;
            itemView.list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cListener.OnItemClick(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return sosiziList.size();
    }
}
