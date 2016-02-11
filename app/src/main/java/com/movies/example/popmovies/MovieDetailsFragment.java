package com.movies.example.popmovies;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.movies.example.popmovies.api.ApiManager;
import com.movies.example.popmovies.model.response.Movie;
import com.movies.example.popmovies.model.response.ReviewDetails;
import com.movies.example.popmovies.model.response.ReviewsResponse;
import com.movies.example.popmovies.model.response.TrailerDetails;
import com.movies.example.popmovies.model.response.TrailersResponse;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class MovieDetailsFragment extends Fragment {

    private final String LOG_TAG = getClass().getName().toString();
    public static String DETAIL_MOVIE_KEY = "DETAIL_MOVIE";
    public String DETAIL_MOVIE_VALUE;
    private LinearLayout trailersLabelLinearLayout;
    private LinearLayout trailersLinearLayout;
    private LinearLayout reviewsLinearLayout;
    private List<TrailerDetails> trailers = new ArrayList<TrailerDetails>();
    private List<ReviewDetails> reviews = new ArrayList<ReviewDetails>();
    private URL shareURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            DETAIL_MOVIE_VALUE = arguments.getString(DETAIL_MOVIE_KEY, "");
        }
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_movie_details, container, false);
        trailersLinearLayout = (LinearLayout) rootview.findViewById(R.id.trailers_layout);
        reviewsLinearLayout = (LinearLayout) rootview.findViewById(R.id.reviews_layout);
        trailersLabelLinearLayout = (LinearLayout) rootview.findViewById(R.id.trailers_label_layout);
        Log.v(LOG_TAG, "In moviedetails fragment");
        Movie movie = new Gson().fromJson(DETAIL_MOVIE_VALUE, Movie.class);
        populateUI(rootview, movie);
        setHasOptionsMenu(false);
        return rootview;
    }

    private void populateUI(ViewGroup rootview, Movie movie) {
        TextView title = (TextView) rootview.findViewById(R.id.movie_details_title_textview);
        TextView rating = (TextView) rootview.findViewById(R.id.movie_details_rating_textview);
        TextView date = (TextView) rootview.findViewById(R.id.movie_details_release_date_textview);
        TextView overview = (TextView) rootview.findViewById(R.id.movie_details_overview_textview);
        ImageView poster = (ImageView) rootview.findViewById(R.id.movie_details_poster_imageview);
        if (movie != null) {
            title.setText(movie.title);
            rating.setText(movie.vote_average.toString() + "/10");
            overview.setText(movie.overview);
            date.setText(movie.release_date);
            String posterURL = "http://image.tmdb.org/t/p/w185/" + movie.poster_path;
            Picasso.with(poster.getContext()).load(posterURL).into(poster);
            Log.v("Movie Details: ", movie.title);
            init(movie.id.toString());
        }
    }

    private void init(String movieId) {
        String apikey = BuildConfig.THE_MOVIES_DB_API_KEY;
        ApiManager.getInstance(getActivity()).getTrailers(movieId, apikey, new Callback<TrailersResponse>() {
            @Override
            public void success(TrailersResponse trailersResponse, Response response) {
                List<TrailerDetails> trailerList = trailersResponse.results;
                loadTrailers(trailerList);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Trailers Response failed");
            }
        });
        ApiManager.getInstance(getActivity()).getReviews(movieId, apikey, new Callback<ReviewsResponse>() {
            @Override
            public void success(ReviewsResponse reviewsResponse, Response response) {
                List<ReviewDetails> reviewsList = reviewsResponse.results;
                loadReviews(reviewsList);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Reviews Response failed");
            }
        });
    }

    public void loadTrailers(List<TrailerDetails> trailersList) {
        this.trailers = trailersList;
        if (trailers.size() > 0) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.trailers_label_view, null);
            TextView trailerLabel = (TextView) view.findViewById(R.id.movie_details_trailers_textview);
            trailerLabel.setText("Trailers: ");
            trailersLabelLinearLayout.addView(view);
            String url = "https://www.youtube.com/watch?v=" + trailers.get(0).key;
            try {
                shareURL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            setHasOptionsMenu(true);
        }
        for (TrailerDetails trailer : trailers) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.trailer_view, null);
            TextView heading = (TextView) view.findViewById(R.id.movie_details_trailer_textview);
            heading.setText(trailer.name);
            final TrailerDetails trail = trailer;
            ImageButton playbtn = (ImageButton) view.findViewById(R.id.movie_details_trailer_imgbtn);
            playbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(LOG_TAG, "OnPlayButtonClick");
                    String url = "https://www.youtube.com/watch?v=" + trail.key;
                    Log.v(LOG_TAG, url);
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Cannot play video. No supported application.", Toast.LENGTH_LONG);
                    }
                }
            });
            trailersLinearLayout.addView(view);
        }

    }

    public void loadReviews(List<ReviewDetails> reviewsList) {
        this.reviews = reviewsList;
        for (ReviewDetails review : reviews) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.reviews_view, null);
            TextView author = (TextView) view.findViewById(R.id.movie_details_reviews_author_textview);
            author.setText("Review by " + review.author);
            TextView content = (TextView) view.findViewById(R.id.movie_details_reviews_content_textview);
            content.setText(review.content);
            reviewsLinearLayout.addView(view);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(callShareIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if ((id == R.id.action_share) && (trailers.size() > 0)) {
            callShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent callShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareURL);
        return shareIntent;
    }
}
