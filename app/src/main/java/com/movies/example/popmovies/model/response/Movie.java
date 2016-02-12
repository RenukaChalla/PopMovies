
package com.movies.example.popmovies.model.response;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.google.gson.annotations.SerializedName;
import com.movies.example.popmovies.db.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class Movie {

    @SerializedName("poster_path")
    public String poster_path;
    public Boolean adult;
    @SerializedName("overview")
    public String overview;
    @SerializedName("release_date")
    public String release_date;
    public List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    public Integer id;
    @SerializedName("original_title")
    public String original_title;
    public String original_language;
    @SerializedName("title")
    public String title;
    public String backdrop_path;
    public Float popularity;
    public Integer vote_count;
    public Boolean video;
    @SerializedName("vote_average")
    public Double vote_average;

    public static final String TMDB_ID =                         "id";
    public static final String TMDB_ORIGINAl_TITLE =             "original_title";
    public static final String TMDB_POSTER_PATH_RELATIVE =       "poster_path";
    public static final String TMDB_RELEASE_DATE =               "release_date";
    public static final String RELEASE_YEAR =                    "release_year";
    public static final String TMDB_USER_RATING =                "vote_average";
    public static final String TMDB_PLOT_SYNOPSIS =              "overview";

    public Movie(Integer id, String originalTitle,
                 String plotSynopsis, String posterPathRelative,
                 String releaseDate, Double userRating) {
        this.id = id;
        this.title = originalTitle;
        this.overview = plotSynopsis;
        this.poster_path = posterPathRelative;
        this.release_date = releaseDate;
        this.vote_average = userRating;
    }
    @Override
    public String toString() {
        return original_title;
    }

    //    @Override
//    public int compareTo(Object another) {
//        int compareMovieId=(((Movie) another).id);
//        return (this.id) - compareMovieId;
//    }
    public static Bundle buildDetailBundle(Cursor c) {

        c.moveToFirst();
        Bundle bundle = new Bundle();
        bundle.putInt("id", c.getInt(1));
        bundle.putString("title", c.getString(2));
        bundle.putString("poster_path", c.getString(3));
        bundle.putString("release_date", c.getString(4));
        bundle.putString("vote_average", Double.toString(c.getDouble(5)));
        bundle.putString("overview", c.getString(6));

        return bundle;
    }

    public static ContentValues buildMovieContentValues(Movie movie) {

        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieTable.DB_MOVIE_ID, movie.id);
        cv.put(MovieContract.MovieTable.DB_TITLE, movie.title);
        cv.put(MovieContract.MovieTable.DB_POSTER_PATH, movie.poster_path);
        cv.put(MovieContract.MovieTable.DB_RELEASE_DATE, movie.release_date);
        cv.put(MovieContract.MovieTable.DB_USER_RATING, movie.vote_average);
        cv.put(MovieContract.MovieTable.DB_OVERVIEW, movie.overview);

        return cv;
    }
}
