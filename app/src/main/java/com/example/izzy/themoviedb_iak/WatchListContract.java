package com.example.izzy.themoviedb_iak;

import android.provider.BaseColumns;

/**
 * Created by izzyengelbert on 2/19/2018.
 */

public class WatchListContract {

    public static final class WatchListEntry implements BaseColumns{

        public static final String TABLE_NAME = "watchlist";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }

}
