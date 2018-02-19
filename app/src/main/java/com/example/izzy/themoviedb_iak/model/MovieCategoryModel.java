package com.example.izzy.themoviedb_iak.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by izzyengelbert on 2/13/2018.
 */

public class MovieCategoryModel implements Parcelable{

    private int id;
    private String category;

    public MovieCategoryModel(int id, String category){
        this.id = id;
        this.category = category;
    }

    protected MovieCategoryModel(Parcel in) {
        id = in.readInt();
        category = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieCategoryModel> CREATOR = new Creator<MovieCategoryModel>() {
        @Override
        public MovieCategoryModel createFromParcel(Parcel in) {
            return new MovieCategoryModel(in);
        }

        @Override
        public MovieCategoryModel[] newArray(int size) {
            return new MovieCategoryModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
