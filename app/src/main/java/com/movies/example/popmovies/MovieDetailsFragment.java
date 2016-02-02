package com.movies.example.popmovies;


import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.movies.example.popmovies.model.response.Movie;
import com.movies.example.popmovies.model.response.MovieResponse;
import com.movies.example.popmovies.model.response.TrailerDetails;
import com.movies.example.popmovies.model.response.Trailers;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {
    private RecyclerView trailerRecyclerView;
    private RecyclerView.Adapter trailerAdapter;
    private RecyclerView.LayoutManager trailerLayoutManager;
    private List<TrailerDetails> trailerDataset;
    private final String LOG_TAG = getClass().getName().toString();

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_movie_details, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        String movieJson = bundle.getString("movie_json", "");
        Movie movie = new Gson().fromJson(movieJson, Movie.class);
        TextView title = (TextView) rootview.findViewById(R.id.movie_details_title_textview);
        TextView rating = (TextView) rootview.findViewById(R.id.movie_details_rating_textview);
        TextView date = (TextView) rootview.findViewById(R.id.movie_details_release_date_textview);
        TextView overview = (TextView) rootview.findViewById(R.id.movie_details_overview_textview);
        ImageView poster = (ImageView) rootview.findViewById(R.id.movie_details_poster_imageview);
        title.setText(movie.title);
        rating.setText(movie.vote_average.toString() + "/10");
        overview.setText(movie.overview);
        date.setText(movie.release_date);
        String posterURL = "http://image.tmdb.org/t/p/w185/" + movie.poster_path;
        Picasso.with(poster.getContext()).load(posterURL).into(poster);
        Log.v("Movie Details: ", movie.title);
        trailerRecyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //trailerRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        trailerLayoutManager = new LinearLayoutManager(getContext());
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        getTrailerUrl(movie.id.toString());
        trailerDataset = new List<TrailerDetails>() {
            @Override
            public void add(int location, TrailerDetails object) {

            }

            @Override
            public boolean add(TrailerDetails object) {
                return false;
            }

            @Override
            public boolean addAll(int location, Collection<? extends TrailerDetails> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends TrailerDetails> collection) {
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
            public TrailerDetails get(int location) {
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
            public Iterator<TrailerDetails> iterator() {
                return null;
            }

            @Override
            public int lastIndexOf(Object object) {
                return 0;
            }

            @Override
            public ListIterator<TrailerDetails> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<TrailerDetails> listIterator(int location) {
                return null;
            }

            @Override
            public TrailerDetails remove(int location) {
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
            public TrailerDetails set(int location, TrailerDetails object) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public List<TrailerDetails> subList(int start, int end) {
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
        trailerAdapter = new TrailerNReviewAdapter(trailerDataset);
        trailerRecyclerView.setAdapter(trailerAdapter);
        setHasOptionsMenu(true);
        return rootview;

    }

    private void openSelectedTrailer(String movieId) {
        getTrailerUrl(movieId);
    }

    private void getTrailerUrl(String movieId) {
        FetchTrailerTask fetchTrailerTask = new FetchTrailerTask();
        fetchTrailerTask.execute(movieId);
    }

    private class FetchTrailerTask extends AsyncTask<String, Void, List<TrailerDetails>> {


        private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();

        @Override
        protected List<TrailerDetails> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String result = null;
            String movieId = params[0];
            String apikey = BuildConfig.THE_MOVIES_DB_API_KEY;

            try {
                String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/" + movieId + "/" + "videos";
                final String APPID_PARAMS = "api_key";
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
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
                    result = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    result = null;
                }
                result = buffer.toString();
                Log.v(LOG_TAG, "Trailers JSON String: " + result);
                return getTrialerDetails(result);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IO Error ", e);
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
        protected void onPostExecute(List<TrailerDetails> trailerlist) {
            if (trailerlist != null) {
                trailerDataset = trailerlist;

                for (TrailerDetails trailer : trailerlist) {
                    if (trailer.key != "") {
                        Log.v(LOG_TAG, " Trailer Path  :" + "https://www.youtube.com/watch?v=" + trailer.key);
                    }
                }
                ((TrailerNReviewAdapter) trailerAdapter).updateData(trailerDataset);
            }
        }
    }

    private List<TrailerDetails> getTrialerDetails(String movieTrailerStr) {
        Gson gson = new Gson();
        Trailers trailerlist = gson.fromJson(movieTrailerStr, Trailers.class);
        List<TrailerDetails> trailerArray = trailerlist.results;
        for (TrailerDetails trailer : trailerArray) {
            Log.v(LOG_TAG, " Trailer Path  :" + "https://www.youtube.com/watch?v=" + trailer.key);
        }
        return trailerArray;
    }
}
