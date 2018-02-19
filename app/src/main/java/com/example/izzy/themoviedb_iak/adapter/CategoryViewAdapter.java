package com.example.izzy.themoviedb_iak.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.activity.DetailsActivity;
import com.example.izzy.themoviedb_iak.activity.MainActivity;
import com.example.izzy.themoviedb_iak.model.MovieCategoryModel;
import com.example.izzy.themoviedb_iak.model.MovieItemModel;
import com.example.izzy.themoviedb_iak.viewholder.MovieCategoryViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by izzyengelbert on 2/15/2018.
 */

public class CategoryViewAdapter extends RecyclerView.Adapter<MovieCategoryViewHolder>{

    String MOVIES_URL;

    private static final String TMDB_GENRE_URL = "https://api.themoviedb.org/3/genre/";
    private static final String REQUEST = "/movies?api_key=";
    private static final String OTHER_REQUEST = "&language=en-US&include_adult=true";
    private static final String SORT_BY = "&sort_by=";
    private static final String ASC = "created_at.asc";
    private static final String POP = "popularity";
    private static final String TOP = "created_at.asc";

    private ArrayList<MovieCategoryModel> movieCategoryModels;
    private Context context;

    MovieItemViewAdapter movieItemViewAdapter;

    public CategoryViewAdapter(ArrayList<MovieCategoryModel> movieCategoryModels, Context context){
        this.movieCategoryModels = movieCategoryModels;
        this.context = context;
    }

    @Override
    public MovieCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_category_view,parent,false);
        return new MovieCategoryViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(MovieCategoryViewHolder holder, final int position) {

        MOVIES_URL = TMDB_GENRE_URL+Integer.toString(movieCategoryModels.get(position).getId())+REQUEST+ MainActivity.API_KEY+OTHER_REQUEST+SORT_BY+ASC;

        final ArrayList<MovieItemModel> movieItemModels = new ArrayList<>();

        movieItemViewAdapter = new MovieItemViewAdapter(movieItemModels, context);
        holder.getMovieItemView().setAdapter(movieItemViewAdapter);
        holder.getMovieItemView().setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.getCategoryTitle().setText(movieCategoryModels.get(position).getCategory().toUpperCase());

        //Log.d("CategoryViewAdapter", holder.getMovieItemView().toString());

        JsonObjectRequest moviesObjectRequest = new JsonObjectRequest(Request.Method.GET, MOVIES_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray movieList = response.getJSONArray("results");

                    //Log.d("CategoryViewAdapter", String.valueOf(movieList.length()));
                    for(int i=0;i<10;++i){
                        JSONObject movie = movieList.getJSONObject(i);
                        movieItemModels
                                .add(new MovieItemModel(movie.getInt("id"),
                                        movie.getString("original_title"),
                                        movie.getString("poster_path"),
                                        movie.getString("backdrop_path")));

                        Log.d("CategoryViewAdapter",(movie.getInt("id") +" "+ movie.getString("original_title")));
                    }

                    Log.d("CategoryViewAdapter", String.valueOf(movieItemModels.size()));

                    movieItemViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorResponse CVAdapter", String.valueOf(error));
            }
        });


        Volley.newRequestQueue(context).add(moviesObjectRequest);

        holder.getMoreButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("Category",movieCategoryModels.get(position));
                bundle.putString("KEY",TMDB_GENRE_URL+movieCategoryModels.get(position).getId()+REQUEST+ MainActivity.API_KEY+OTHER_REQUEST+SORT_BY+ASC);
                Log.d("CategoryViewAdapter", movieCategoryModels.get(position).toString());
                Log.d("CategoryViewAdapter", String.valueOf(movieItemModels.size()));
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieCategoryModels.size();
    }



}
