package com.example.izzy.themoviedb_iak.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by izzyengelbert on 2/13/2018.
 */

public class MovieItemModel implements Parcelable{

    private int movieId;
    private String movieTitle;
    private String imageUrl;
    private String imageBackdropUrl;


    public MovieItemModel(int movieId, String movieTitle, String imageUrl, String imageBackdropUrl){
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.imageUrl = imageUrl;
        this.imageBackdropUrl = imageBackdropUrl;
    }

    public MovieItemModel() {
    }

    public MovieItemModel(String movieTitle, String imageUrl) {
        this.movieTitle = movieTitle;
        this.imageUrl = imageUrl;
    }

    protected MovieItemModel(Parcel in) {
        movieId = in.readInt();
        movieTitle = in.readString();
        imageUrl = in.readString();
        imageBackdropUrl = in.readString();
    }

    public static final Creator<MovieItemModel> CREATOR = new Creator<MovieItemModel>() {
        @Override
        public MovieItemModel createFromParcel(Parcel in) {
            return new MovieItemModel(in);
        }

        @Override
        public MovieItemModel[] newArray(int size) {
            return new MovieItemModel[size];
        }
    };

    public String getImageBackdropUrl() {
        return imageBackdropUrl;
    }

    public void setImageBackdropUrl(String imageBackdropUrl) {
        this.imageBackdropUrl = imageBackdropUrl;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieId) {
        this.movieTitle = movieId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(movieTitle);
        parcel.writeString(imageUrl);
        parcel.writeString(imageBackdropUrl);
    }
}
