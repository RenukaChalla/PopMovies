package com.movies.example.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView movieRecyclerView;
    private RecyclerView.Adapter movieAdapter;
    private RecyclerView.LayoutManager movieLayoutManager;
    String[] movieDataset;
    private final String LOG_TAG = getClass().getName().toString();
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        movieRecyclerView = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        movieRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        movieLayoutManager = new GridLayoutManager(getContext(), 2);
        movieRecyclerView.setLayoutManager(movieLayoutManager);

        // specify an adapter (see also next example)
        movieDataset = new String[10];
        for(int i = 0; i < movieDataset.length; i++)
        {
            movieDataset[i] = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        }
        movieAdapter = new MovieGridAdapter(getContext(), movieDataset);
        movieRecyclerView.setAdapter(movieAdapter);
        updateMoviesList();
        setHasOptionsMenu(true);
        return rootview;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    public void updateMoviesList() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute("popularity.desc");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class FetchMoviesTask extends AsyncTask<String, Void, String[]> {


        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJSONStr = null;
            String sortBy = params[0];
            String apikey = BuildConfig.THE_MOVIES_DB_API_KEY;

            try {
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
//                        "http://api.themoviedb.org/3/discover/movie?" +
//                        "sort_by=popularity.desc&api_key=apikey";

                final String SORT_BY = "sort_by";
                final String APPID_PARAMS = "api_key";
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, sortBy)
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
                    // Stream was empty.  No point in parsing.
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
        protected void onPostExecute(String[] result) {
            //if (result != null) {
            movieDataset = new String[10];
            for(int i = 0; i < movieDataset.length; i++) {
                movieDataset[i] = "http://image.tmdb.org/t/p/w185//oXUWEc5i3wYyFnL1Ycu8ppxxPvs.jpg";
            }
            ((MovieGridAdapter)movieAdapter).updateData(movieDataset);
            // }
        }
    }
    private String[] getMovieDataFromJSON(String moviesJSONStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String DBM_LIST = "results";
        final String DBM_MOVIE = "movie";
        final String DBM_TITLE = "title";
        final String DBM_POSTER_PATH = "poster_path";
        final String DBM_OVERVIEW = "overview";
        final String DBM_RATING = "vote_average";
        final String DBM_DATE = "release_date";


        JSONObject movieJSON = new JSONObject(moviesJSONStr);
        JSONArray movieArray = movieJSON.getJSONArray(DBM_LIST);
        String [] posterPaths  = new String[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String poster;
            String overview;
            String rating;
            String release_date;
            JSONObject movieDetails = movieArray.getJSONObject(i);
            //JSONObject movieObject = movieDetails.getJSONArray(DBM_MOVIE).getJSONObject(0);
            title = movieDetails.getString(DBM_TITLE);
            poster = movieDetails.getString(DBM_POSTER_PATH);
            overview = movieDetails.getString(DBM_OVERVIEW);
            rating = movieDetails.getString(DBM_RATING);
            release_date = movieDetails.getString(DBM_DATE);
            posterPaths[i] = poster;
        }


        for (String s : posterPaths) {
            Log.v(LOG_TAG, "Movie poster path " + s);
        }
        return posterPaths;

    }
}

