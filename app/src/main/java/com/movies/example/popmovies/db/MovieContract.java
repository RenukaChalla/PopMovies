package com.movies.example.popmovies.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Renuka Challa on 11/02/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.movies.example.popmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final class MovieTable implements BaseColumns {

        public static final String DB_TABLE_NAME = "movie";

        public static final String DB_MOVIE_ID = "movie_id";
        public static final String DB_TITLE = "title";
        public static final String DB_POSTER_PATH = "poster_path";
        public static final String DB_RELEASE_DATE = "release_date";
        public static final String DB_USER_RATING = "user_rating";
        public static final String DB_OVERVIEW = "overview";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String CONTENT_TYPE = ContentResolver
                .CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver
                .CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getMovieId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
