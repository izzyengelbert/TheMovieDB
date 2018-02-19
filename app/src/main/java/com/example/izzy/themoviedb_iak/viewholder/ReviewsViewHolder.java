package com.example.izzy.themoviedb_iak.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.izzy.themoviedb_iak.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by izzyengelbert on 2/16/2018.
 */

public class ReviewsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.review)
    TextView review;
    @BindView(R.id.see_more)
    TextView seeMore;


    public ReviewsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getSeeMore() {
        return seeMore;
    }

    public void setSeeMore(TextView seeMore) {
        this.seeMore = seeMore;
    }

    public TextView getUserName() {
        return userName;
    }

    public void setUserName(TextView userName) {
        this.userName = userName;
    }

    public TextView getReview() {
        return review;
    }

    public void setReview(TextView review) {
        this.review = review;
    }
}
