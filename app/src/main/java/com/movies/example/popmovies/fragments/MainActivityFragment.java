package com.movies.example.popmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.movies.example.popmovies.BuildConfig;
import com.movies.example.popmovies.R;
import com.movies.example.popmovies.activities.SettingsActivity;
import com.movies.example.popmovies.adapters.MovieGridAdapter;
import com.movies.example.popmovies.api.ApiManager;
import com.movies.example.popmovies.model.response.MovieResponse;
import com.movies.example.popmovies.models.Movie;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView movieRecyclerView;
    private MovieGridAdapter movieAdapter;
    private RecyclerView.LayoutManager movieLayoutManager;
    private List<Movie> movieDataset;
    private final String LOG_TAG = getClass().getName();
    public static String MOVIEDETAILS;
    private ProgressDialog progress;

    private ProgressDialog progress;

    private void showProgress() {
        progress = ProgressDialog.show(getActivity(), "Please wait", "Connecting to server...", true);
    }

    private void hideProgress() {
        if(progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(getClass().getName(), "onCreateView");
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        movieRecyclerView = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);
        movieRecyclerView.setHasFixedSize(true);
        movieLayoutManager = new GridLayoutManager(getContext(), 2);
        movieRecyclerView.setLayoutManager(movieLayoutManager);
        movieDataset = new ArrayList<Movie>();
        movieAdapter = new MovieGridAdapter(getActivity(), movieDataset);
        movieRecyclerView.setAdapter(movieAdapter);
        init();
        setHasOptionsMenu(true);
        return rootview;
    }

    private void showProgress() {
        progress = ProgressDialog.show(getActivity(), "Please wait", "Connecting to server...", true);
    }

    private void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public void onResume() {
        super.onResume();
        (movieAdapter).setOnItemClickListener(new MovieGridAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                movieRecyclerView.setBackgroundColor(getResources().getColor(R.color.black));
                v.setActivated(true);
                Log.i(LOG_TAG, " Clicked on Item " + position);
                Movie movie = movieDataset.get(position);
                String movieJson = new Gson().toJson(movie);
                MOVIEDETAILS = movieJson;
                ((Callback) getActivity()).onItemSelected(MOVIEDETAILS);
            }
        });
    }

    private void init() {
        showProgress();
        retrofit.Callback<MovieResponse> callback = new retrofit.Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                hideProgress();
                movieDataset = movieResponse.results;
                movieAdapter.updateData(movieDataset);
            }

            @Override
            public void failure(RetrofitError error) {
                hideProgress();
                Log.e(LOG_TAG, "Movie Response failed");
            }
        };
        String apikey = BuildConfig.THE_MOVIES_DB_API_KEY;
        String sortby = updateMoviesList();
        String sortbyparam;
        if (sortby.equalsIgnoreCase("popularity")) {
            sortbyparam = "popularity.desc";
            ApiManager.getInstance(getActivity()).getMoviesByPopularity(sortbyparam, apikey, callback);
        } else {
            sortbyparam = "vote_average.desc";
            ApiManager.getInstance(getActivity()).getMoviesByHighestRating(sortbyparam, apikey, callback);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    public String updateMoviesList() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortby = sharedPrefs.getString(
                getString(R.string.pref_key_sort_by),
                getString(R.string.pref_popularity_sort_by));
        return sortby;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings_main) {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        if (id == R.id.action_favorite) {
            ((FavCallback) getActivity()).onFavSelected();
            Toast.makeText(getActivity(), "Your Favourites!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String movie);
    }

    public interface FavCallback {
        public void onFavSelected();
    }
}

