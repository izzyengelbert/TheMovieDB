package com.example.izzy.themoviedb_iak.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.model.ReviewModel;
import com.example.izzy.themoviedb_iak.viewholder.ReviewsViewHolder;

import java.util.ArrayList;

/**
 * Created by izzyengelbert on 2/16/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsViewHolder>{

    private ArrayList<ReviewModel> reviewModels;
    private Context context;

    public ReviewsAdapter(ArrayList<ReviewModel> reviewModels, Context context) {
        this.reviewModels = reviewModels;
        this.context = context;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews,parent,false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, final int position) {
        Log.d("ReviewsAdapter", reviewModels.get(position).getUsername());
        Log.d("ReviewsAdapter", reviewModels.get(position).getReview());
        holder.getUserName().setText(reviewModels.get(position).getUsername());
        if(reviewModels.get(position).getReview().length()>200){
            holder.getReview().setText(reviewModels.get(position).getReview().substring(0,199)+"...");
        }else{
            holder.getReview().setText(reviewModels.get(position).getReview());
        }
        holder.getSeeMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(reviewModels.get(position).getUrl())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewModels.size();
    }
}
