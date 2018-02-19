package com.example.izzy.themoviedb_iak.model;

/**
 * Created by izzyengelbert on 2/16/2018.
 */

public class ReviewModel {

    private String username;
    private String review;
    private String url;


    public ReviewModel(String username, String review, String url) {
        this.username = username;
        this.review = review;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
