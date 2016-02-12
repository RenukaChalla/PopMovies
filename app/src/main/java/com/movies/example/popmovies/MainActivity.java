package com.movies.example.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean mTwoPane = false;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailsFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;

        }
    }


    @Override
    public void onItemSelected(String movieJson) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putString(MovieDetailsFragment.DETAIL_MOVIE_KEY, movieJson);
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetails.class);
            intent.putExtra("movie", movieJson);
            startActivity(intent);
        }
    }
}
