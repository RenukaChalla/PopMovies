package com.movies.example.popmovies;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.movies.example.popmovies.api.ApiManager;
import com.movies.example.popmovies.db.MovieContract;
import com.movies.example.popmovies.model.response.Movie;
import com.movies.example.popmovies.model.response.MovieResponse;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView movieRecyclerView;
    private MovieGridAdapter movieAdapter;
    private FavouriteAdapter favouriteAdapter;
    private RecyclerView.LayoutManager movieLayoutManager;
    private List<Movie> movieDataset;
    private final String LOG_TAG = getClass().getName();
    public static String MOVIEDETAILS;
    private boolean isfav = false;

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
        movieAdapter = new MovieGridAdapter(movieDataset);
        movieRecyclerView.setAdapter(movieAdapter);
        init();
        setHasOptionsMenu(true);
        return rootview;
    }

    private void init() {
        retrofit.Callback<MovieResponse> callback = new retrofit.Callback<MovieResponse>() {
            @Override
            public void success(MovieResponse movieResponse, Response response) {
                movieDataset = movieResponse.results;
                movieAdapter.updateData(movieDataset);
            }

            @Override
            public void failure(RetrofitError error) {
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
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    public void onResume() {
        super.onResume();
        if (isfav == true) {
            favouriteAdapter = new FavouriteAdapter(getActivity(), null, 0);
            View rootview = getActivity().getLayoutInflater().inflate(R.layout.fragment_main, null, false);
            GridView gridView = (GridView) rootview.findViewById(R.id.my_recycler_view);
            gridView.setAdapter(favouriteAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Uri detailUri = MovieContract.MovieTable.buildMovieUri(id);
                    ((Callback) getActivity()).onItemSelected(detailUri.toString());
                }
            });

        }
        (movieAdapter).setOnItemClickListener(new MovieGridAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                Movie movie = movieDataset.get(position);
                String movieJson = new Gson().toJson(movie);
                MOVIEDETAILS = movieJson;
                ((Callback) getActivity()).onItemSelected(MOVIEDETAILS);
            }
        });
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
            isfav = false;
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        if (id == R.id.action_favorite) {
            isfav = true;
            onResume();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.MovieTable.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favouriteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favouriteAdapter.swapCursor(null);
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String movie);
    }
}

