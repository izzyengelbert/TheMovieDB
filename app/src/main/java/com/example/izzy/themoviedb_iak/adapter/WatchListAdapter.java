package com.example.izzy.themoviedb_iak.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.WatchListContract;
import com.example.izzy.themoviedb_iak.activity.DetailsActivity;
import com.example.izzy.themoviedb_iak.activity.MainActivity;
import com.example.izzy.themoviedb_iak.model.MovieItemModel;
import com.example.izzy.themoviedb_iak.viewholder.MovieItemViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by izzyengelbert on 2/19/2018.
 */

public class WatchListAdapter extends RecyclerView.Adapter<MovieItemViewHolder>{

    static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    static final String API_KEY = "?api_key=";
    static final String LANGUAGE_US = "&language=en-US";

    Cursor cursor;
    Context context;

    MovieItemModel movieItemModel;
    Bundle bundle;

    public WatchListAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
        movieItemModel = new MovieItemModel();
        bundle = new Bundle();
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_view,parent,false);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieItemViewHolder holder, int position) {



        if(!cursor.moveToPosition(position)){
            return;
        }

        int id = cursor.getInt(cursor.getColumnIndex(WatchListContract.WatchListEntry.COLUMN_MOVIE_ID));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, MOVIE_URL + id + API_KEY + MainActivity.API_KEY + LANGUAGE_US, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int id = response.getInt("id");
                    String name = response.getString("original_title");
                    String posterPath = response.getString("poster_path");
                    String backdropPath = response.getString("backdrop_path");
                    movieItemModel.setMovieId(id);
                    movieItemModel.setMovieTitle(name);
                    movieItemModel.setImageUrl(posterPath);
                    movieItemModel.setImageBackdropUrl(backdropPath);
                    holder.updateUI(movieItemModel, context);
                    bundle.putParcelable("Movie", movieItemModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(context).add(jsonObjectRequest);

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

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    private void movieDetailFragment(MovieItemModel movieItemModel){
        Intent intent = new Intent(context,DetailsActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
