package com.example.izzy.themoviedb_iak.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.activity.DetailsActivity;
import com.example.izzy.themoviedb_iak.activity.MainActivity;
import com.example.izzy.themoviedb_iak.fragments.MovieDetailsFragment;
import com.example.izzy.themoviedb_iak.model.MovieItemModel;
import com.example.izzy.themoviedb_iak.viewholder.MovieItemViewHolder;

import java.util.ArrayList;

/**
 * Created by izzyengelbert on 2/15/2018.
 */

public class MovieItemViewAdapter extends RecyclerView.Adapter<MovieItemViewHolder>{

    private ArrayList<MovieItemModel> movieItemModels;
    private Context context;

    public MovieItemViewAdapter(ArrayList<MovieItemModel> movieItemModels, Context context){
        this.movieItemModels = movieItemModels;
        this.context = context;
        //Log.d("MovieItemViewAdapter", "Constructor");
    }


    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View movieItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_view,parent,false);
        return new MovieItemViewHolder(movieItemView);
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        final MovieItemModel movieItemModel = movieItemModels.get(position);
        holder.updateUI(movieItemModel,context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieDetailFragment(movieItemModel);
            }
        });
        holder.getMovieImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieDetailFragment(movieItemModel);
            }
        });
        holder.getMovieTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieDetailFragment(movieItemModel);
            }
        });
        Log.d("MovieItemViewAdapter", movieItemModel.toString());
    }

    @Override
    public int getItemCount() {
        return movieItemModels.size();
    }

    private void movieDetailFragment(MovieItemModel movieItemModel){
        Bundle bundle = new Bundle();
        bundle.putParcelable("Movie", movieItemModel);
        Intent intent = new Intent(context,DetailsActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
