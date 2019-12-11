package com.example.myapplication;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private ArrayList<ListData> listData = new ArrayList<>();

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView sosizi_list_name;
        private ImageView sosizi_list_img;

        ItemViewHolder(View itemView) {
            super(itemView);

           // sosizi_list_name = itemView.findViewById(R.id.sosizi_list_name);
           // sosizi_list_img = itemView.findViewById(R.id.sosizi_list_img);
        }

        void onBind(ListData data) {
            sosizi_list_name.setText(data.getName());
            sosizi_list_img.setImageResource(data.getImg());
        }
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_sosizi_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(ListData data) {
        listData.add(data);
    }



}