package com.example.izzy.themoviedb_iak.fragments;


import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.izzy.themoviedb_iak.activity.MainActivity;
import com.example.izzy.themoviedb_iak.adapter.CategoryViewAdapter;
import com.example.izzy.themoviedb_iak.adapter.MovieItemViewAdapter;
import com.example.izzy.themoviedb_iak.model.MovieCategoryModel;
import com.example.izzy.themoviedb_iak.model.MovieItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    String apiKey;
    private String CATEGORY_URL;
    RecyclerView categoryViewRecycler;

    ArrayList<MovieCategoryModel> movieCategoryModels = new ArrayList<>();
    CategoryViewAdapter categoryViewAdapter;


    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        apiKey = MainActivity.API_KEY;
        CATEGORY_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key="+apiKey+"&language=en-US";
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        categoryRecyclerView(view);

        return view;
    }

    private void categoryRecyclerView(View view){

        categoryViewRecycler = view.findViewById(R.id.home_fragment_recycler);
        categoryViewAdapter = new CategoryViewAdapter(movieCategoryModels,getContext());
        categoryViewRecycler.setAdapter(categoryViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        categoryViewRecycler.setLayoutManager(linearLayoutManager);

        JsonObjectRequest categoryObjectRequest = new JsonObjectRequest(Request.Method.GET, CATEGORY_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray categoryArray = response.getJSONArray("genres");

                    for(int i=0; i<categoryArray.length();++i){
                        JSONObject genre = categoryArray.getJSONObject(i);
                        movieCategoryModels.add(new MovieCategoryModel(genre.getInt("id"), genre.getString("name")));
//                        Log.d("HomeFragment",(genre.getInt("id") +" "+ genre.getString("name")));
                    }

                    categoryViewAdapter.notifyDataSetChanged();
                    Log.d("HomeFragment","In");
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorResponse HFragment", String.valueOf(error));
            }
        });

        Volley.newRequestQueue(getContext()).add(categoryObjectRequest);

    }
}
