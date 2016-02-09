package com.movies.example.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.movies.example.popmovies.api.ApiManager;
import com.movies.example.popmovies.model.response.Movie;
import com.movies.example.popmovies.model.response.MovieResponse;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView movieRecyclerView;
    private MovieGridAdapter movieAdapter;
    private RecyclerView.LayoutManager movieLayoutManager;
    private List<Movie> movieDataset;
    private final String LOG_TAG = getClass().getName();
    public static String MOVIEDETAILS;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(getClass().getName(), "onCreateView");
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        movieRecyclerView = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        movieRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
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
        String apikey = BuildConfig.THE_MOVIES_DB_API_KEY;
        String sortby = updateMoviesList();
        String sortbyparam;
        if (sortby.equalsIgnoreCase("popularity")) {
            sortbyparam = "popularity.desc";
            ApiManager.getInstance(getActivity()).getMoviesByPopularity(sortbyparam, apikey, new retrofit.Callback<MovieResponse>() {
                @Override
                public void success(MovieResponse movieResponse, Response response) {
                    movieDataset = movieResponse.results;
                    movieAdapter.updateData(movieDataset);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, "Movie Response failed");
                }
            });
        }else {
            sortbyparam = "vote_average.desc";
            ApiManager.getInstance(getActivity()).getMoviesByHighestRating(sortbyparam, apikey, new retrofit.Callback<MovieResponse>() {
                @Override
                public void success(MovieResponse movieResponse, Response response) {
                    movieDataset = movieResponse.results;
                    movieAdapter.updateData(movieDataset);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(LOG_TAG, "Movie Response failed");
                }
            });
        }



    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    public void onResume() {
        super.onResume();
        ((MovieGridAdapter) movieAdapter).setOnItemClickListener(new MovieGridAdapter.ItemClickListener() {
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
//        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
//        fetchMoviesTask.execute(sortby);

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {


        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJSONStr = null;
            String sortBy = params[0];
            String apikey = BuildConfig.THE_MOVIES_DB_API_KEY;

            try {
                String MOVIES_BASE_URL;
//                        "http://api.themoviedb.org/3/discover/movie?" +
//                        "sort_by=popularity.desc&api_key=apikey";
                String sortbyparam;
                final String SORT_BY = "sort_by";
                if (sortBy.equalsIgnoreCase("popularity")) {
                    MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                    sortbyparam = "popularity.desc";
                } else {
                    MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie?certification_country=US&certification=R";
                    sortbyparam = "vote_average.desc";
                }
                final String APPID_PARAMS = "api_key";
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, sortbyparam)
                        .appendQueryParameter(APPID_PARAMS, apikey)
                        .build();


                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    moviesJSONStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    moviesJSONStr = null;
                }
                moviesJSONStr = buffer.toString();
                Log.v(LOG_TAG, "Movies JSON String: " + moviesJSONStr);
                return getMovieDataFromJSON(moviesJSONStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IO Error ", e);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSON Error " + moviesJSONStr, e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            if (movieList != null) {
                movieDataset = movieList;

                for (Movie movie : movieList) {
                    if (movie.poster_path != "") {
                        Log.v(LOG_TAG, " Poster Path  :" + "http://image.tmdb.org/t/p/w185/" + movie.poster_path);
                    }
                    //movieDataset.add(movie);
                }
                ((MovieGridAdapter) movieAdapter).updateData(movieDataset);
            }
        }
    }

    private List<Movie> getMovieDataFromJSON(String moviesJSONStr)
            throws JSONException {

        Gson gson = new Gson();
        MovieResponse moviesList = gson.fromJson(moviesJSONStr, MovieResponse.class);
        List<Movie> movieArray = moviesList.results;
        for (Movie movie : movieArray) {
            Log.v(LOG_TAG, " Poster Path  :" + "http://image.tmdb.org/t/p/w185/" + movie.poster_path);
        }
        return movieArray;
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String movie);
    }


}

