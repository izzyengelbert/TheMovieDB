package com.example.izzy.themoviedb_iak.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.adapter.MovieItemViewAdapter;
import com.example.izzy.themoviedb_iak.model.MovieCategoryModel;
import com.example.izzy.themoviedb_iak.model.MovieItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment{

    RecyclerView gridContainer;

    ArrayList<MovieItemModel> searchResults;
    String URL;

    private static final int MAX_PAGE = 1000;
    private int pageNumber = 1;
    private int maxPage = 0;
    private static final String PAGE = "&page=";

    @BindView(R.id.genreTitle)
    TextView genreTitle;
    @BindView(R.id.search_page)
    EditText searchPage;
    @BindView(R.id.search_page_button)
    ImageButton searchPageButton;
    @BindView(R.id.prev_button)
    TextView prevButton;
    @BindView(R.id.next_button)
    TextView nextButton;
    MovieItemViewAdapter movieItemViewAdapter;
    public PageFragment() {
        searchResults = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);

        getMaxPage();
        pageSearch(pageNumber);


        gridContainer = (RecyclerView) view.findViewById(R.id.grid_container);
        if(getArguments().getParcelable("Category")!=null){
            MovieCategoryModel movieCategoryModel = getArguments().getParcelable("Category");
            genreTitle.setText(movieCategoryModel.getCategory().toUpperCase());
        }else {
            searchPage.setVisibility(View.INVISIBLE);
            searchPageButton.setVisibility(View.INVISIBLE);
            searchPage.setEnabled(false);
            searchPageButton.setEnabled(false);
            if(getArguments().getString("Sort").equals("latest")){
                genreTitle.setText(getArguments().getString("Sort").toUpperCase());
            }else if(getArguments().getString("Sort").equals("now playing")){
                genreTitle.setText(getArguments().getString("Sort").toUpperCase());
            }else if(getArguments().getString("Sort").equals("highest rated")){
                genreTitle.setText(getArguments().getString("Sort").toUpperCase());
            }else if(getArguments().getString("Sort").equals("most popular")){
                genreTitle.setText(getArguments().getString("Sort").toUpperCase());
            }else if(getArguments().getString("Sort").equals("upcoming")){
                genreTitle.setText(getArguments().getString("Sort").toUpperCase());
            }
        }
        movieItemViewAdapter = new MovieItemViewAdapter(searchResults,getContext());
        gridContainer.setAdapter(movieItemViewAdapter);
        gridContainer.setLayoutManager(new GridLayoutManager(getContext(), 3));

        searchPage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    int number = pageNumber;
                    if(!Pattern.matches("[a-zA-Z]+", searchPage.getText().toString())){
                       number = Integer.parseInt(searchPage.getText().toString());
                    }
                    Log.d("PageFragment", String.valueOf(maxPage));
                    if(number>maxPage){
                        Toast.makeText(getContext(),"Page number "+number+" does not exist",Toast.LENGTH_SHORT);
                    }else if(number<1){
                        Toast.makeText(getContext(),"Page number "+number+" does not exist",Toast.LENGTH_SHORT);
                    }else{
                        pageNumber = number;
                        pageSearch(pageNumber);
                    }
                    searchPage.setText("");
                    return true;
                }
                return false;
            }
        });

        searchPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = pageNumber;
                if(!Pattern.matches("[a-zA-Z]+", searchPage.getText().toString())){
                    number = Integer.parseInt(searchPage.getText().toString());
                }
                Log.d("PageFragment", String.valueOf(maxPage));
                if(number>maxPage){
                    Toast.makeText(getContext(),"Page number "+number+" does not exist",Toast.LENGTH_LONG).show();
                }else if(number<1){
                    Toast.makeText(getContext(),"Page number "+number+" does not exist",Toast.LENGTH_LONG).show();
                }else{
                    pageNumber = number;
                    pageSearch(pageNumber);
                }
                searchPage.setText("");
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPage.setText("");
                if(pageNumber==1){
                    Toast.makeText(getContext(),"This is the first page",Toast.LENGTH_SHORT).show();
                }else{
                    pageNumber-=1;
                    Log.d("PageFragment", String.valueOf(pageNumber));
                    pageSearch(pageNumber);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPage.setText("");
                Log.d("PageFragment", "Max Page : "+String.valueOf(maxPage));
                if(pageNumber==maxPage){
                    Toast.makeText(getContext(),"This is the last page",Toast.LENGTH_SHORT).show();
                }else{
                    pageNumber+=1;
                    pageSearch(pageNumber);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchResults.clear();
    }

    private void getMaxPage(){
        URL = getArguments().getString("KEY");
        Log.d("PageFragment", "maxpage url : "+URL);
        JsonObjectRequest maxPageObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    Log.d("PageFragment","total results "+response.getJSONArray("results").length());
                    if(results.length()>0){
                        maxPage = MAX_PAGE;
                    }else{
                        Log.d("PageFragment","total page "+response.getInt("total_pages"));
                        maxPage = response.getInt("total_pages");
                        Log.d("PageFragment","total page "+maxPage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorResponseSFragment", String.valueOf(error));
            }
        });
        Volley.newRequestQueue(getContext()).add(maxPageObjectRequest);
    }

    private void pageSearch(int number){
        searchResults.clear();
        URL = getArguments().getString("KEY")+PAGE+number;
        Log.d("PageFragment", URL);
        JsonObjectRequest searchObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for(int i=0;i<results.length();++i){
                        JSONObject movie = results.getJSONObject(i);
                        searchResults.add(new MovieItemModel(movie.getInt("id"),movie.getString("original_title"),movie.getString("poster_path"),movie.getString("backdrop_path")));
                    }

                    movieItemViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorResponseSFragment", String.valueOf(error));
            }
        });
        Volley.newRequestQueue(getContext()).add(searchObjectRequest);
    }
}
