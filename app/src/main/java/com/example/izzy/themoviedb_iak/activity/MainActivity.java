package com.example.izzy.themoviedb_iak.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.fragments.HomeFragment;
import com.example.izzy.themoviedb_iak.fragments.PageFragment;
import com.example.izzy.themoviedb_iak.fragments.WatchListFragment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String API_KEY = "";
    private static final String TMDB_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_REQUEST = "?api_key=";
    private static final String NOW_PLAYING = "now_playing";
    private static final String LATEST = "latest";
    private static final String HIGHEST_RATED = "top_rated";
    private static final String MOST_POPULAR = "popular";
    private static final String UPCOMING = "upcoming";
    private static final String LANGUAGE_US = "&language=en-US";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getting the api key from the file
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("api_key.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            Log.d("Main Activity", new String(buffer));
            API_KEY = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Main Activity", API_KEY);

        if(isNetworkAvailable()){
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.search_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            homeFragment();

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_now_playing) {
            nowPlayingFragment();
            return true;
        }if (id == R.id.action_genres) {
            homeFragment();
            return true;
        }if (id == R.id.action_highest_rated) {
            highestRatedFragment();
            return true;
        }if (id == R.id.action_most_popular) {
            mostPopularFragment();
            return true;
        }if (id == R.id.action_upcoming) {
            upcomingFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            homeFragment();
        } else if (id == R.id.nav_watchlist) {
            watchListFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null&&networkInfo.isConnected()){
            return true;
        }

        return false;
    }

    private void homeFragment(){
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
    }

    private void watchListFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new WatchListFragment()).commit();
    }

    private void nowPlayingFragment(){
        PageFragment pageFragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("KEY",TMDB_URL+NOW_PLAYING+API_REQUEST+ MainActivity.API_KEY+LANGUAGE_US);
        bundle.putString("Sort","now playing");
        Log.d("MainActivity",TMDB_URL+NOW_PLAYING+API_REQUEST+ MainActivity.API_KEY+LANGUAGE_US);
        pageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,pageFragment).commit();
    }

    private void highestRatedFragment(){
        PageFragment pageFragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("KEY",TMDB_URL+HIGHEST_RATED+API_REQUEST+ MainActivity.API_KEY+LANGUAGE_US);
        bundle.putString("Sort","highest rated");
        Log.d("MainActivity",TMDB_URL+HIGHEST_RATED+API_REQUEST+ MainActivity.API_KEY+LANGUAGE_US);
        pageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,pageFragment).commit();
    }

    private void mostPopularFragment(){
        PageFragment pageFragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("KEY",TMDB_URL+MOST_POPULAR+API_REQUEST+ MainActivity.API_KEY+LANGUAGE_US);
        bundle.putString("Sort","most popular");
        Log.d("MainActivity",TMDB_URL+MOST_POPULAR+API_REQUEST+ MainActivity.API_KEY+LANGUAGE_US);
        pageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,pageFragment).commit();
    }

    private void upcomingFragment(){
        PageFragment pageFragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("KEY",TMDB_URL+UPCOMING+API_REQUEST+ MainActivity.API_KEY+LANGUAGE_US);
        bundle.putString("Sort","upcoming");
        Log.d("MainActivity",TMDB_URL+UPCOMING+API_REQUEST+ MainActivity.API_KEY+LANGUAGE_US);
        pageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,pageFragment).commit();
    }
}
