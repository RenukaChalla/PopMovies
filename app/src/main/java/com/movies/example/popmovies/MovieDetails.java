package com.movies.example.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_details);
//        setSupportActionBar(toolbar);
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
