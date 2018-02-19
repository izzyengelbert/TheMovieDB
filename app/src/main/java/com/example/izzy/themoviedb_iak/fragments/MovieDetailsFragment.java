package com.example.izzy.themoviedb_iak.fragments;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.WatchListContract;
import com.example.izzy.themoviedb_iak.WatchListDBHelper;
import com.example.izzy.themoviedb_iak.activity.MainActivity;
import com.example.izzy.themoviedb_iak.adapter.MovieItemViewAdapter;
import com.example.izzy.themoviedb_iak.adapter.ReviewsAdapter;
import com.example.izzy.themoviedb_iak.model.MovieItemModel;
import com.example.izzy.themoviedb_iak.model.ReviewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment {

    String MOVIE_URL;
    private static final String WEB_POSTER = "https://image.tmdb.org/t/p/w300";
    private static final String WEB_BACKDROP = "https://image.tmdb.org/t/p/w500";
    private static final String YOUTUBE = "https://www.youtube.com/watch?v=";
    private static final String TMDB_MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_REQUEST = "?api_key=";
    private static final String APPEND_VIDEOS = "&append_to_response=videos";
    private static final String REVIEW_REQUEST = "/reviews";
    private static final String SIMILAR_REQUEST = "/similar";
    private static final String LANG_REQUEST = "&language=en-US";
    private static final String NUM_PAGE = "&page=1";

    @BindView(R.id.movie_backdrop)
    ImageView movieBackdrop;
    @BindView(R.id.play_button)
    ImageButton playButton;
    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.movie_title_details)
    TextView movieTitle;
    @BindView(R.id.movie_time)
    TextView movieTime;
    @BindView(R.id.movie_rating)
    TextView movieRating;
    @BindView(R.id.watchlist_text)
    TextView watchListText;
    @BindView(R.id.watchlist_button)
    ImageButton watchListButton;
    @BindView(R.id.movie_desc)
    TextView movieDesc;

    ArrayList<ReviewModel> reviewModels;
    ArrayList<MovieItemModel> movieItemModels;
    ReviewsAdapter reviewsAdapter;
    MovieItemViewAdapter movieItemViewAdapter;

    SQLiteDatabase database;
    WatchListDBHelper dbHelper;

    boolean added;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new WatchListDBHelper(getContext());
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this,view);

//        Log.d("MovieDetailsFragment","In");



        reviewModels = new ArrayList<>();
        movieItemModels = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.review_recycler);
        reviewsAdapter = new ReviewsAdapter(reviewModels,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(reviewsAdapter);


        RecyclerView similar = (RecyclerView) view.findViewById(R.id.similar_container);
        movieItemViewAdapter = new MovieItemViewAdapter(movieItemModels,getContext());
        similar.setAdapter(movieItemViewAdapter);
        similar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        final MovieItemModel movieItemModel = getArguments().getParcelable("Movie");
        Log.d("MovieDetailsFragment", String.valueOf(movieItemModel.getMovieId()));
        Log.d("MovieDetailsFragment",movieItemModel.getMovieTitle());

        movieDetail(movieItemModel);

        movieSimilar(movieItemModel);

        movieReviews(movieItemModel);

        if(checkMovie(movieItemModel)){
            added = true;
            watchListButton.setImageResource(R.drawable.ic_bookmark_button);
            Log.d("MovieDetailsFragment", String.valueOf(added));
        }else{
            added = false;
            watchListButton.setImageResource(R.drawable.ic_bookmark_button_unmarked);
            Log.d("MovieDetailsFragment", String.valueOf(added));
        }

        watchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(added){
                    if(removeMovie(movieItemModel)){
                        watchListButton.setImageResource(R.drawable.ic_bookmark_button_unmarked);
                        Toast.makeText(getContext(),"Removed from Watchlist",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),"Remove failed",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    addNewMovie(movieItemModel);
                    watchListButton.setImageResource(R.drawable.ic_bookmark_button);
                    Toast.makeText(getContext(),"Added to Watchlist",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reviewModels.clear();
    }
    //add function
    private boolean removeMovie(MovieItemModel movieItemModel){
        return (database.delete(WatchListContract.WatchListEntry.TABLE_NAME, WatchListContract.WatchListEntry.COLUMN_MOVIE_ID+" = "+ movieItemModel.getMovieId(), null)>0);
    }

    //add function
    private long addNewMovie(MovieItemModel movieItemModel){
        ContentValues contentValues = new ContentValues();
        contentValues.put(WatchListContract.WatchListEntry.COLUMN_MOVIE_ID, movieItemModel.getMovieId());
        return database.insert(WatchListContract.WatchListEntry.TABLE_NAME, null, contentValues);
    }

    private boolean checkMovie(MovieItemModel movieItemModel){
        Cursor cursor;

        cursor= database.rawQuery("SELECT * FROM "+ WatchListContract.WatchListEntry.TABLE_NAME + " WHERE "
                + WatchListContract.WatchListEntry.COLUMN_MOVIE_ID + " = " + movieItemModel.getMovieId() ,null);
        if(cursor.getCount()>0){
            return true;
        }
        return false;
    }

    private void movieDetail(final MovieItemModel movieItemModel){
        MOVIE_URL = TMDB_MOVIE_URL +movieItemModel.getMovieId()+API_REQUEST+ MainActivity.API_KEY+APPEND_VIDEOS;

        JsonObjectRequest jsonMovieRequest = new JsonObjectRequest(Request.Method.GET, MOVIE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String overview = response.getString("overview");
                    String year = response.getString("release_date");
                    int minute = response.getInt("runtime");
                    double rating = response.getDouble("vote_average");
                    JSONObject youtubes = response.getJSONObject("videos");
                    JSONArray firstVideoArray = youtubes.getJSONArray("results");
                    JSONObject firstVideo = firstVideoArray.getJSONObject(0);
                    final String youtubeURL = firstVideo.getString("key");

                    SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat yearFormat = new SimpleDateFormat("MMM dd yyyy");

                    Date timeFormat = null;
                    try {
                        timeFormat = defaultFormat.parse(year);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    year = yearFormat.format(timeFormat);

                    Picasso.with(getContext()).load(WEB_BACKDROP+movieItemModel.getImageBackdropUrl()).error(R.drawable.sample_backdrop).into(movieBackdrop);
                    Picasso.with(getContext()).load(WEB_POSTER+movieItemModel.getImageUrl()).error(R.drawable.sample_image).into(moviePoster);
                    movieTitle.setText(movieItemModel.getMovieTitle());
                    movieTime.setText(year+", "+minute+"mins");
                    movieDesc.setText(overview);
                    movieRating.setText(String.valueOf(rating));
                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE+youtubeURL)));
                            Log.i("MovieDetailsFragment","Video is playing..");
                        }
                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorResponseMDFragment", String.valueOf(error));
            }
        });

        Volley.newRequestQueue(getContext()).add(jsonMovieRequest);
    }

    private void movieSimilar(final MovieItemModel movieItemModel){


        MOVIE_URL = TMDB_MOVIE_URL+movieItemModel.getMovieId()+SIMILAR_REQUEST+API_REQUEST+ MainActivity.API_KEY+LANG_REQUEST+NUM_PAGE;

        JsonObjectRequest similarObjectRequest = new JsonObjectRequest(Request.Method.GET, MOVIE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray movies = response.getJSONArray("results");
                    for(int i=0;i<movies.length();++i){
                        JSONObject movie = movies.getJSONObject(i);
                        movieItemModels
                                .add(new MovieItemModel(movie.getInt("id"),
                                        movie.getString("original_title"),
                                        movie.getString("poster_path"),
                                        movie.getString("backdrop_path")));
                    }
                    movieItemViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorResponseMDFragment", String.valueOf(error));
            }
        });

        Volley.newRequestQueue(getContext()).add(similarObjectRequest);
    }

    private void movieReviews(MovieItemModel movieItemModel){
        MOVIE_URL = TMDB_MOVIE_URL+movieItemModel.getMovieId()+REVIEW_REQUEST+API_REQUEST+MainActivity.API_KEY+LANG_REQUEST+NUM_PAGE;
        Log.d("MovieDetailsFragment2", String.valueOf(movieItemModel.getMovieId()));
        Log.d("MovieDetailsFragment2", MOVIE_URL);
        JsonObjectRequest reviewsObjectRequest = new JsonObjectRequest(Request.Method.GET, MOVIE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray reviews = response.getJSONArray("results");
                    Log.d("MovieDetailsFragment2", String.valueOf(reviews));
                    /*for(int i =0; i<reviews.length();++i){
                        JSONObject review = reviews.getJSONObject(i);
                        Log.d("MovieDetailsFragment2", String.valueOf(review));

//                        reviewModels.add(new ReviewModel(review.getString("author"),review.getString("url")));
                    }*/
                    for(int i=0;i<reviews.length();++i){
                        JSONObject rev = reviews.getJSONObject(i);
                        reviewModels.add(new ReviewModel(rev.getString("author"),rev.getString("content"), rev.getString("url")));

                        Log.d("MovieDetailsFragment2", reviewModels.get(i).getUsername());
                        Log.d("MovieDetailsFragment2", reviewModels.get(i).getReview());
                    }

                    reviewsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrorResponseMDFragment", String.valueOf(error));
            }
        });

        Volley.newRequestQueue(getContext()).add(reviewsObjectRequest);
    }

}
