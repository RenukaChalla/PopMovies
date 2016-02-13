package com.movies.example.popmovies.fragments;


import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.movies.example.popmovies.BuildConfig;
import com.movies.example.popmovies.R;
import com.movies.example.popmovies.api.ApiManager;
import com.movies.example.popmovies.models.Movie;
import com.movies.example.popmovies.models.ReviewDetails;
import com.movies.example.popmovies.model.response.ReviewsResponse;
import com.movies.example.popmovies.models.TrailerDetails;
import com.movies.example.popmovies.model.response.TrailersResponse;
import com.movies.example.popmovies.utils.FavDbUtils;
import com.squareup.picasso.Picasso;

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
    ViewGroup rootview;
    public static String DETAIL_MOVIE_KEY = "DETAIL_MOVIE";
    public String DETAIL_MOVIE_VALUE;
    private LinearLayout trailersLabelLinearLayout;
    private LinearLayout trailersLinearLayout;
    private LinearLayout reviewsLinearLayout;
    private List<TrailerDetails> trailers = new ArrayList<TrailerDetails>();
    private List<ReviewDetails> reviews = new ArrayList<ReviewDetails>();
    private String shareURL = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            DETAIL_MOVIE_VALUE = arguments.getString(DETAIL_MOVIE_KEY, "");
        }
        rootview = (ViewGroup) inflater.inflate(R.layout.fragment_movie_details, container, false);
        trailersLinearLayout = (LinearLayout) rootview.findViewById(R.id.trailers_layout);
        reviewsLinearLayout = (LinearLayout) rootview.findViewById(R.id.reviews_layout);
        trailersLabelLinearLayout = (LinearLayout) rootview.findViewById(R.id.trailers_label_layout);
        Log.v(LOG_TAG, "In moviedetails fragment" + DETAIL_MOVIE_VALUE);
        populateUI();
        setHasOptionsMenu(false);
        return rootview;
    }

    private Movie selectedMovie;

    private void populateUI() {
        //TODO:check if it is fav url or json string
        selectedMovie = new Gson().fromJson(DETAIL_MOVIE_VALUE, Movie.class);
        TextView title = (TextView) rootview.findViewById(R.id.movie_details_title_textview);
        TextView rating = (TextView) rootview.findViewById(R.id.movie_details_rating_textview);
        TextView date = (TextView) rootview.findViewById(R.id.movie_details_release_date_textview);
        TextView overview = (TextView) rootview.findViewById(R.id.movie_details_overview_textview);
        ImageView poster = (ImageView) rootview.findViewById(R.id.movie_details_poster_imageview);

        if (selectedMovie != null) {
            title.setText(selectedMovie.title);
            rating.setText(selectedMovie.vote_average.toString() + "/10");
            overview.setText(selectedMovie.overview);
            date.setText(selectedMovie.release_date);
            String posterURL = "http://image.tmdb.org/t/p/w185/" + selectedMovie.poster_path;
            Picasso.with(poster.getContext()).load(posterURL).into(poster);
            isFavourite();
            onFavButtonClick();
            Log.v("Movie Details: ", selectedMovie.title);
            init(selectedMovie.id.toString());
        }
    }

    private void isFavourite() {
        Button favbtn = (Button) rootview.findViewById(R.id.favorite_button);
        Cursor cursor = FavDbUtils.checkIfInFav(getActivity(), selectedMovie.id);
        if (cursor.getCount() == 0) {
            favbtn.setText("Mark as \n favorite");
            favbtn.setAllCaps(true);
        } else if(cursor.getCount() == 1){
            favbtn.setText("Remove from \n favorites");
            favbtn.setAllCaps(true);
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

    private void onFavButtonClick() {
        final Button favbtn = (Button) rootview.findViewById(R.id.favorite_button);
        if (selectedMovie != null) {
            favbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cursor cursor = FavDbUtils.checkIfInFav(getActivity(), selectedMovie.id);
                    if (cursor.getCount() == 0) {
                        FavDbUtils.insertFavMovieIntoDb(getActivity(), selectedMovie);
                        favbtn.setText("Remove from \n favorites");
                        favbtn.setAllCaps(true);
                    } else {
                        if ((cursor != null) && (cursor.getCount() == 1)) {
                            FavDbUtils.deleteFavMovieFromDb(getActivity(), selectedMovie.id);
                            favbtn.setText("Mark as \n favorite");
                            favbtn.setAllCaps(true);
                        }
                    }
                }
            });
        }
    }

    public void loadTrailers(List<TrailerDetails> trailersList) {
        this.trailers = trailersList;
        if (trailers.size() > 0) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.trailers_label_view, null);
            TextView trailerLabel = (TextView) view.findViewById(R.id.movie_details_trailers_textview);
            trailerLabel.setText("Trailers: ");
            trailersLabelLinearLayout.addView(view);
            String url = "https://www.youtube.com/watch?v=" + trailers.get(0).key;
            shareURL = url;
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
        if (mShareActionProvider != null) {
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
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "SHARE TRAILER : ");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareURL);
        return shareIntent;
    }
}
