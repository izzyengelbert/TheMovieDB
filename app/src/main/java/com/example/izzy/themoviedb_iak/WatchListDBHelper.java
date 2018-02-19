package com.example.izzy.themoviedb_iak;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by izzyengelbert on 2/19/2018.
 */

public class WatchListDBHelper extends SQLiteOpenHelper {

    private static final String DATABATE_NAME = "watchlist.db";
    private static final int DATABASE_VERSION = 1;

    public WatchListDBHelper(Context context) {
        super(context, DATABATE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WATCHLIST_TABLE = "CREATE TABLE " +
                WatchListContract.WatchListEntry.TABLE_NAME+" ("+
                WatchListContract.WatchListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WatchListContract.WatchListEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL"+
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WATCHLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WatchListContract.WatchListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
