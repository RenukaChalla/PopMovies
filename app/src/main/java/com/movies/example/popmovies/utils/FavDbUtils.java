package com.movies.example.popmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.movies.example.popmovies.db.MovieContract;
import com.movies.example.popmovies.model.response.Movie;

/**
 * Created by admin on 11/02/16.
 */
public class FavDbUtils {
    public static Bundle getBundleFromUri(Context context, Uri movieUri) {

        Bundle b;
        Integer tmdbId;

        // query movie table
        Cursor cMovie = context.getContentResolver().query(movieUri,
                null, null, null, null);
        b = Movie.buildDetailBundle(cMovie);
        tmdbId = cMovie.getInt(cMovie.getColumnIndex(MovieContract.MovieTable.DB_MOVIE_ID));
        cMovie.close();
        return b;
    }
    public static void insertFavMovieIntoDb(Context context, Bundle b) {
        Log.v("UTils: ", b.toString());
        ContentValues cv = Movie.buildMovieContentValues(b);
        context.getContentResolver().insert(MovieContract.MovieTable.CONTENT_URI,cv);
    }
}