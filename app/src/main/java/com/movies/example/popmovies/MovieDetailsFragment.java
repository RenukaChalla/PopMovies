package com.movies.example.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.movies.example.popmovies.model.response.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_movie_details, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        String movieJson = bundle.getString("movie_json","");
        Movie movie = new Gson().fromJson(movieJson,Movie.class);
        TextView title = (TextView) rootview.findViewById(R.id.movie_details_title_textview);
        TextView rating = (TextView) rootview.findViewById(R.id.movie_details_rating_textview);
        TextView date = (TextView) rootview.findViewById(R.id.movie_details_release_date_textview);
        TextView overview = (TextView) rootview.findViewById(R.id.movie_details_overview_textview);
        ImageView poster = (ImageView) rootview.findViewById(R.id.movie_details_poster_imageview);
        title.setText(movie.title);
        rating.setText(movie.vote_average.toString() + "/10");
        overview.setText(movie.overview);
        date.setText(movie.release_date);
        String posterURL = "http://image.tmdb.org/t/p/w185/"+movie.poster_path;
        Picasso.with(poster.getContext()).load(posterURL).into(poster);
        Log.v("Movie Details: ", movie.title);
        return rootview;
    }


}
