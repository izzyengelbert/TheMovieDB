package com.example.izzy.themoviedb_iak.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.izzy.themoviedb_iak.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by izzyengelbert on 2/15/2018.
 */

public class MovieCategoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.category_title)
    TextView categoryTitle;
    @BindView(R.id.movie_item_recycler)
    RecyclerView movieItemView;
    @BindView(R.id.more_button)
    TextView moreButton;


    public MovieCategoryViewHolder(View itemView) {
        super(itemView);
        movieItemView = (RecyclerView) itemView.findViewById(R.id.movie_item_recycler);
        Log.d("CategoryViewHolder", getMovieItemView().toString());
        ButterKnife.bind(this, itemView);
    }

    public TextView getMoreButton() {
        return moreButton;
    }

    public void setMoreButton(TextView moreButton) {
        this.moreButton = moreButton;
    }

    public TextView getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(TextView categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public RecyclerView getMovieItemView() {
        return movieItemView;
    }

    public void setMovieItemView(RecyclerView movieItemView) {
        this.movieItemView = movieItemView;
    }
}
