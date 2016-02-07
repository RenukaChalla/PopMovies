package com.movies.example.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView movieRecyclerView;
    private RecyclerView.Adapter movieAdapter;
    private RecyclerView.LayoutManager movieLayoutManager;
    private List<Movie> movieDataset ;
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
        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        movieRecyclerView = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        movieRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        movieLayoutManager = new GridLayoutManager(getContext(), 2);
        movieRecyclerView.setLayoutManager(movieLayoutManager);
        movieDataset = new List<Movie>() {
            @Override
            public void add(int location, Movie object) {

            }

            @Override
            public boolean add(Movie object) {
                return true;
            }

            @Override
            public boolean addAll(int location, Collection<? extends Movie> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Movie> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean contains(Object object) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public Movie get(int location) {
                return null;
            }

            @Override
            public int indexOf(Object object) {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Movie> iterator() {
                return null;
            }

            @Override
            public int lastIndexOf(Object object) {
                return 0;
            }

            @Override
            public ListIterator<Movie> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Movie> listIterator(int location) {
                return null;
            }

            @Override
            public Movie remove(int location) {
                return null;
            }

            @Override
            public boolean remove(Object object) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public Movie set(int location, Movie object) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public List<Movie> subList(int start, int end) {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(T[] array) {
                return null;
            }
        };
        movieAdapter = new MovieGridAdapter(movieDataset);
        movieRecyclerView.setAdapter(movieAdapter);


        updateMoviesList();
        setHasOptionsMenu(true);
        return rootview;
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
                ((Callback)getActivity()).onItemSelected(MOVIEDETAILS);
            }
        });
    }

    public void updateMoviesList() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortby = sharedPrefs.getString(
                getString(R.string.pref_key_sort_by),
                getString(R.string.pref_popularity_sort_by));
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(sortby);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
                Intent settingsIntent = new Intent(getActivity(),SettingsActivity.class);
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
                String MOVIES_BASE_URL ;
//                        "http://api.themoviedb.org/3/discover/movie?" +
//                        "sort_by=popularity.desc&api_key=apikey";
                String sortbyparam;
                final String SORT_BY = "sort_by";
                if(sortBy.equalsIgnoreCase("popularity")){
                    MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                    sortbyparam = "popularity.desc";
                } else{
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

                for ( Movie movie : movieList) {
                    if(movie.poster_path !="") {
                        Log.v(LOG_TAG," Poster Path  :" +"http://image.tmdb.org/t/p/w185/"+movie.poster_path);
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
        for(Movie movie : movieArray){
            Log.v(LOG_TAG," Poster Path  :" +"http://image.tmdb.org/t/p/w185/"+movie.poster_path);
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

