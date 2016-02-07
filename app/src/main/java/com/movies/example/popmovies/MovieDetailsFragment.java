package com.movies.example.popmovies;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.movies.example.popmovies.model.response.Movie;
import com.movies.example.popmovies.model.response.TrailerDetails;
import com.movies.example.popmovies.model.response.Trailers;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    private final String LOG_TAG = getClass().getName().toString();
    public static String DETAIL_MOVIE_KEY = "DETAIL_MOVIE";
    public String DETAIL_MOVIE_VALUE;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null){
            DETAIL_MOVIE_VALUE = arguments.getString(DETAIL_MOVIE_KEY,"");
        }

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_movie_details, container, false);
        linearLayout = (LinearLayout) rootview.findViewById(R.id.trailers_layout);
        Log.v(LOG_TAG, "In moviedetails fragment");
        Movie movie = new Gson().fromJson(DETAIL_MOVIE_VALUE, Movie.class);
        TextView title = (TextView) rootview.findViewById(R.id.movie_details_title_textview);
        TextView rating = (TextView) rootview.findViewById(R.id.movie_details_rating_textview);
        TextView date = (TextView) rootview.findViewById(R.id.movie_details_release_date_textview);
        TextView overview = (TextView) rootview.findViewById(R.id.movie_details_overview_textview);
        ImageView poster = (ImageView) rootview.findViewById(R.id.movie_details_poster_imageview);
        if(movie != null){
            title.setText(movie.title);
            rating.setText(movie.vote_average.toString() + "/10");
            overview.setText(movie.overview);
            date.setText(movie.release_date);
            String posterURL = "http://image.tmdb.org/t/p/w185/" + movie.poster_path;
            Picasso.with(poster.getContext()).load(posterURL).into(poster);
            Log.v("Movie Details: ", movie.title);
            getTrailerUrl(movie.id.toString());
        }

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
                for (TrailerDetails trailer : trailerlist) {
                    if (trailer.key != "") {
                        Log.v(LOG_TAG, " Trailer Path  :" + "https://www.youtube.com/watch?v=" + trailer.key);
                    }
                }
                loadTrailers(trailerlist);
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

    public void loadTrailers(List<TrailerDetails> trailers){
        for(TrailerDetails trailer : trailers){
            View view = getActivity().getLayoutInflater().inflate(R.layout.recycler_view, null);
            TextView heading = (TextView)view.findViewById(R.id.movie_details_trailer_textview);
            heading.setText(trailer.name);
            linearLayout.addView(view);
        }
    }
}
