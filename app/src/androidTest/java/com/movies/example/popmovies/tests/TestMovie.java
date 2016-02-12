package com.movies.example.popmovies.tests;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.test.AndroidTestCase;

import com.movies.example.popmovies.db.MovieContract;
import com.movies.example.popmovies.model.response.Movie;

public class TestMovie extends AndroidTestCase {

    private Movie m;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        m = new Movie(
                135397,
                "Jurassic World",
                "Twenty-two years after the events of Jurassic Park...",
                "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg",
                "2015-07-17",
                7.2
        );

    }


    public void testbuildDetailBundleFromCursor() throws Throwable {

        String plot = "Twenty-two years after the events of Jurassic Park, " +
                "Isla Nublar now features a fully functioning dinosaur theme park, " +
                "Jurassic World, as originally envisioned by John Hammond.";

        // clear database
        mContext.getContentResolver().delete(
                MovieContract.MovieTable.CONTENT_URI,
                null,
                null
        );

        // insert JW movie
        ContentValues cv = TestUtils.createJWMovieContentValues();
        mContext.getContentResolver().insert(MovieContract.MovieTable.CONTENT_URI, cv);

        // query database
        Cursor c = mContext.getContentResolver().query(
                MovieContract.MovieTable.CONTENT_URI,
                null, null, null, null);
        c.moveToFirst();

        // create and check bundle
        Bundle b = Movie.buildDetailBundle(c);
        assertEquals(135397, b.getInt(Movie.TMDB_ID));
        assertEquals("Jurassic World", b.getString(Movie.TMDB_ORIGINAl_TITLE));
        assertEquals("2015", b.getString(Movie.RELEASE_YEAR));
        assertEquals(7.0, Double.parseDouble(b.getString(Movie.TMDB_USER_RATING)));
        assertEquals(plot, b.getString(Movie.TMDB_PLOT_SYNOPSIS));
        c.close();

    }


}















