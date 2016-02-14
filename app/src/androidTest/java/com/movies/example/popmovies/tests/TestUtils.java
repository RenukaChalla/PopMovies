package com.movies.example.popmovies.tests;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import com.movies.example.popmovies.db.MovieContract;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtils extends AndroidTestCase {

    public static final String TAG = TestUtils.class.getSimpleName();

    public static String getJsonMoviesString() throws Throwable {
        String filename = "assets/movies2.json";
        return readFile(filename);
    }
    public static String getJWJsonMovieString() throws Throwable {
        String filename = "assets/movie2.json";
        return readFile(filename);
    }

    public static ContentValues createJWMovieContentValues() throws Throwable {

        ContentValues jwcv = new ContentValues();
        String plot = "Twenty-two years after the events of Jurassic Park, " +
                "Isla Nublar now features a fully functioning dinosaur theme park, " +
                "Jurassic World, as originally envisioned by John Hammond.";

        jwcv.put(MovieContract.MovieTable.DB_MOVIE_ID, 135397);
        jwcv.put(MovieContract.MovieTable.DB_TITLE, "Jurassic World");
        jwcv.put(MovieContract.MovieTable.DB_POSTER_PATH,
                "http://image.tmdb.org/t/p/w185/uXZYawqUsChGSj54wcuBtEdUJbh.jpg");
        jwcv.put(MovieContract.MovieTable.DB_RELEASE_DATE, "2015");
        jwcv.put(MovieContract.MovieTable.DB_USER_RATING, 7.0);
        jwcv.put(MovieContract.MovieTable.DB_OVERVIEW, plot);

        return jwcv;
    }

    // helper functions
    private static String readFile(String filename) throws Throwable {
        InputStream inputStream = TestUtils.class.getClassLoader().getResourceAsStream(filename);
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }
        return sb.toString();
    }
}
