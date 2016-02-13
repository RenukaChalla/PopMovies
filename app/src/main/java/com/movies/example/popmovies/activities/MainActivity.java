package com.movies.example.popmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.movies.example.popmovies.R;
import com.movies.example.popmovies.fragments.FavFragment;
import com.movies.example.popmovies.fragments.MainActivityFragment;
import com.movies.example.popmovies.fragments.MovieDetailsFragment;
import com.movies.example.popmovies.utils.SystemUtils;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback,
        MainActivityFragment.FavCallback, FavFragment.Callback {

    private boolean mTwoPane = false;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String FAVFRAGMENT_TAG = "FAV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (SystemUtils.isNetworkConnected(this)) {
            if (findViewById(R.id.container_mainactivity) != null) {
                MainActivityFragment fragment = new MainActivityFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_mainactivity, fragment)
                        .commit();
            }
        } else{
            onFavSelected();
        }
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

    public void onFavSelected() {
        FavFragment fragment = new FavFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_mainactivity, fragment, FAVFRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();

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
