package com.example.myapplication;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Review extends RecyclerView.Adapter {
    ArrayList<Review> reviewList;
    ClickListener cListener;

    public Adapter_Review(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }
    public interface ClickListener{
        void OnReviewClick(int posistion);
    }
    public void setOnItemClick(ClickListener cListener){
        this.cListener = cListener;
    }

    class ItemView extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title;
        TextView writter;
        LinearLayout one_review;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.review_list_img);
            title = itemView.findViewById(R.id.review_list_title);
            writter = itemView.findViewById(R.id.review_list_writter);
            one_review = itemView.findViewById(R.id.review_list);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Adapter_Review.ItemView itemView = (Adapter_Review.ItemView)holder;
        Log.i("리뷰 - 제목 : ", ReviewServer.reviewList.get(position).getTitle());

        Log.i("리뷰 - 작성자 : ", ReviewServer.reviewList.get(position).getWritter());

        Log.i("리뷰 - 이미지 : ", ReviewServer.reviewList.get(position).getImg()+".");

        itemView.title.setText(ReviewServer.reviewList.get(position).getTitle());
        itemView.writter.setText(ReviewServer.reviewList.get(position).getWritter());
        itemView.img.setImageResource(ReviewServer.reviewList.get(position).getImg());

        if(cListener != null){
            final int pos = position;
            itemView.one_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("AdapterReview-position:",pos+"");
                    Log.i("AdapterReview-size:",ReviewServer.reviewList.size()+"");
                    cListener.OnReviewClick(pos);
                    notifyItemRemoved(pos);
                    notifyItemChanged(pos);
                    notifyItemRangeChanged(pos, ReviewServer.reviewList.size());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return ReviewServer.reviewList.size();
    }
}
