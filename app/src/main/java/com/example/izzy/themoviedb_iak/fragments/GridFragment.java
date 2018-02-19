package com.example.izzy.themoviedb_iak.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.activity.SearchActivity;
import com.example.izzy.themoviedb_iak.adapter.MovieItemViewAdapter;
import com.example.izzy.themoviedb_iak.model.MovieItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridFragment extends Fragment {

    RecyclerView searchGrid;

    ArrayList<MovieItemModel> searchResults;

    String URL;
    MovieItemViewAdapter movieItemViewAdapter;
    private int pageNumber = 1;
    private static final String PAGE = "&page=";

    public GridFragment() {
        searchResults = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        URL = getArguments().getString("KEY")+PAGE+pageNumber;
        searchGrid = (RecyclerView) view.findViewById(R.id.search_grid);
        movieItemViewAdapter = new MovieItemViewAdapter(searchResults,getContext());
        searchGrid.setAdapter(movieItemViewAdapter);
        searchGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));


        JsonObjectRequest searchObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    for(int i=0;i<results.length();++i){
                        JSONObject movie = results.getJSONObject(i);
                        searchResults.add(new MovieItemModel(movie.getInt("id"),movie.getString("original_title"),movie.getString("poster_path"),movie.getString("backdrop_path")));
                        Log.d("GridFragment",(movie.getInt("id") +" "+ movie.getString("original_title")));
                        movieItemViewAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorResponseGFragment", String.valueOf(error));
            }
        });

        Volley.newRequestQueue(getContext()).add(searchObjectRequest);
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        searchResults.clear();
    }
}
