package com.example.izzy.themoviedb_iak.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.fragments.MovieDetailsFragment;
import com.example.izzy.themoviedb_iak.fragments.PageFragment;

public class DetailsActivity extends AppCompatActivity {

    public static final String SIMILAR = "similar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(getIntent().getExtras().getParcelable("Category")!=null){
            PageFragment pageFragment = new PageFragment();
            Bundle bundle = getIntent().getExtras();
            bundle.putBoolean(SIMILAR,true);
            pageFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, pageFragment).commit();
        }else{
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            Bundle bundle = getIntent().getExtras();
            bundle.putBoolean(SIMILAR,true);
            movieDetailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_container, movieDetailsFragment).commit();
        }

    }
}
