package com.movies.example.popmovies.api;

import android.content.Context;
import android.util.Log;

import com.movies.example.popmovies.config.AppConfig;
import com.movies.example.popmovies.enums.NetworkMode;
import com.movies.example.popmovies.model.response.MovieResponse;
import com.movies.example.popmovies.model.response.ReviewsResponse;
import com.movies.example.popmovies.model.response.TrailersResponse;
import com.movies.example.popmovies.services.ApiClientService;
import com.movies.example.popmovies.utils.SystemUtils;

import retrofit.Callback;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class ApiManager {

    private static ApiManager instance;
    private Context context;
    final public String LOG_TAG = getClass().getName().toString();

    public static ApiManager getInstance(Context context) {
        if (instance == null) {
            instance = new ApiManager();
        }
        instance.context = context;
        return instance;
    }

    public boolean isNetworkConnected() {
        return SystemUtils.isNetworkConnected(context);
    }
    public boolean isApiOnline() {
        Log.i(LOG_TAG, "Network Mode is " + AppConfig.NETWORK_MODE.getValue());
        return AppConfig.NETWORK_MODE == NetworkMode.ONLINE;
    }

    public void getMoviesByPopularity(String sortByParam,
                                      String apiParam, Callback<MovieResponse> callback){
        if (isApiOnline() && isNetworkConnected()) {
            ApiClientService.get().getMoviesByPopularity(sortByParam, apiParam, callback);
        }

    }

    public void getMoviesByHighestRating(String sortByParam,
                                      String apiParam, Callback<MovieResponse> callback){
        if (isApiOnline() && isNetworkConnected()) {
            ApiClientService.get().getMoviesByHighestRating(sortByParam, apiParam, callback);
        }

    }

    public void getTrailers(String movieId, String apiParam, Callback<TrailersResponse> callback ) {
        if (isApiOnline() && isNetworkConnected()) {
            ApiClientService.get().getTrailers(movieId, apiParam, callback);
        }
    }

    public void getReviews(String movieId, String apiParam, Callback<ReviewsResponse> callback ) {
        if (isApiOnline() && isNetworkConnected()) {
            ApiClientService.get().getReviews(movieId, apiParam, callback);
        }
    }
}
