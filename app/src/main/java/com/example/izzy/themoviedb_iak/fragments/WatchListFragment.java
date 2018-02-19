package com.example.izzy.themoviedb_iak.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.izzy.themoviedb_iak.R;
import com.example.izzy.themoviedb_iak.WatchListContract;
import com.example.izzy.themoviedb_iak.WatchListDBHelper;
import com.example.izzy.themoviedb_iak.adapter.WatchListAdapter;
import com.example.izzy.themoviedb_iak.model.MovieItemModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatchListFragment extends Fragment {

    ArrayList<MovieItemModel> movieItemModels;
    @BindView(R.id.wishListTitle)
    TextView wishListTitle;

    WatchListDBHelper dbHelper;
    SQLiteDatabase database;

    public WatchListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new WatchListDBHelper(getContext());
        dbHelper.getWritableDatabase();
        database = dbHelper.getWritableDatabase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_watch_list, container, false);
        ButterKnife.bind(this,view);
        //Insert preferences here
        /*final MovieCategoryModel movieCategoryModel = getArguments().getParcelable("WishList");*/
        RecyclerView gridRecyclerView = (RecyclerView) view.findViewById(R.id.watch_list_grid_container);
        WatchListAdapter watchListAdapter = new WatchListAdapter(getWatchListIDs(),getContext());
        gridRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        gridRecyclerView.setAdapter(watchListAdapter);


        return view;
    }

    private Cursor getWatchListIDs(){
        return database.query(WatchListContract.WatchListEntry.TABLE_NAME,null,null,null,null,null, WatchListContract.WatchListEntry.COLUMN_MOVIE_ID);
    }

}
