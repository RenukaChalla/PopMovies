package com.movies.example.popmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.movies.example.popmovies.db.MovieContract.MovieTable;

/**
 * Created by Renuka Challa on 11/02/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favourites.db";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieTable.DB_TABLE_NAME + " (" +
                MovieTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieTable.DB_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MovieTable.DB_TITLE + " TEXT NOT NULL, " +
                MovieTable.DB_POSTER_PATH + " TEXT NOT NULL, " +
                MovieTable.DB_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieTable.DB_USER_RATING + " REAL NOT NULL, " +
                MovieTable.DB_OVERVIEW + " TEXT NOT NULL " +
                " );";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieTable.DB_TABLE_NAME);
    }
}
