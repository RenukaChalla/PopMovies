package com.movies.example.popmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.movies.example.popmovies.R;
import com.movies.example.popmovies.fragments.MovieDetailsFragment;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null){
            Bundle arguments = new Bundle();
            arguments.putString(MovieDetailsFragment.DETAIL_MOVIE_KEY,getIntent().getExtras().getString("movie"));
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

}
