package com.movies.example.popmovies.api;

import com.movies.example.popmovies.model.response.MovieResponse;
import com.movies.example.popmovies.model.response.ReviewsResponse;
import com.movies.example.popmovies.model.response.TrailersResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public interface IPopMoviesApi {
    @GET("/discover/movie?")
    void getMoviesByPopularity(@Query("sort_by") String sortByParam,
                               @Query("api_key") String APPID_PARAMS,
                               Callback<MovieResponse> callback);

    @GET("/discover/movie?certification_country=US&certification=R")
    void getMoviesByHighestRating(@Query("sort_by") String sortByParam,
                               @Query("api_key") String APPID_PARAMS,
                               Callback<MovieResponse> callback);

    @GET("/movie/{movieId}/videos")
    void getTrailers(@Path("movieId") String movieId, @Query("api_key") String APPID_PARAMS,
                                  Callback<TrailersResponse> callback);

    @GET("/movie/{movieId}/reviews")
    void getReviews(@Path("movieId") String movieId, @Query("api_key") String APPID_PARAMS,
                     Callback<ReviewsResponse> callback);

}
