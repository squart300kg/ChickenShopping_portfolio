package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter_ManageDelivery extends RecyclerView.Adapter{

    ArrayList<BuyDTO> buyDTOArray = new ArrayList();
    ClickListener cListener;
    public interface ClickListener{
        void onManageDeliveryClick(int position);
    }
    public void setOnItemClick(ClickListener cListener){
        this.cListener = cListener;
    }
    public Adapter_ManageDelivery(ArrayList<BuyDTO> buyDTOArray) {
        this.buyDTOArray = buyDTOArray;
    }

    class ItemView extends RecyclerView.ViewHolder{
        TextView id;
        TextView name;
        TextView address;
        TextView date;
        TextView manage_delivery_state;
        LinearLayout manage_list;
        public ItemView(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.manage_id);
            name = itemView.findViewById(R.id.manage_name);
            address = itemView.findViewById(R.id.manage_address);
            date = itemView.findViewById(R.id.manage_date);
            manage_list = itemView.findViewById(R.id.manage_list);
            manage_delivery_state = itemView.findViewById(R.id.manage_delivery_state);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_manage_delivery_row, parent, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_ManageDelivery.ItemView itemView = (Adapter_ManageDelivery.ItemView)holder;

        itemView.id.setText("아이디 : " + buyDTOArray.get(buyDTOArray.size() - 1 - position).getId());
        itemView.name.setText("이름 : " + buyDTOArray.get(buyDTOArray.size() - 1 - position).getName());
        itemView.address.setText("주소 : " + buyDTOArray.get(buyDTOArray.size() - 1 - position).getAddress());
        itemView.date.setText("주문일 : " + buyDTOArray.get(buyDTOArray.size() - 1 - position).getDate());
        itemView.manage_delivery_state.setText("배송 현황 : " + buyDTOArray.get(buyDTOArray.size() - 1 - position).getDelivery());

        if(cListener != null){
            final int pos = buyDTOArray.size() - 1 - position;
            //final Adapter_ManageDelivery.ItemView itemView1 = itemView;
            itemView.manage_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cListener.onManageDeliveryClick(pos);
                    //itemView1.manage_delivery_state.setText("배송 현황 : " + buyDTOArray.get(buyDTOArray.size() - 1 - pos).getDelivery());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return buyDTOArray.size();
    }
}
