package com.example.izzy.themoviedb_iak.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.model.MovieItemModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by izzyengelbert on 2/15/2018.
 */

public class MovieItemViewHolder extends RecyclerView.ViewHolder{

    private String WEB = "https://image.tmdb.org/t/p/w300";

    @BindView(R.id.movie_image)
    ImageView movieImage;
    @BindView(R.id.movie_title)
    TextView movieTitle;

    public MovieItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updateUI(MovieItemModel movieItemModel, Context context){
        //Log.d("MovieItemViewHolder",movieItemModel.getImageUrl());
        //Log.d("MovieItemViewHolder",movieItemModel.getMovieTitle());
        //movieImage.setImageResource(R.drawable.sample_image);
        Picasso.with(context).load(WEB+movieItemModel.getImageUrl()).error(R.drawable.sample_image).into(movieImage);
        movieTitle.setText(movieItemModel.getMovieTitle());
    }

    public TextView getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(TextView movieTitle) {
        this.movieTitle = movieTitle;
    }

    public ImageView getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(ImageView movieImage) {
        this.movieImage = movieImage;
    }
}
