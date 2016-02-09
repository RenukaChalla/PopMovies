package com.movies.example.popmovies.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movies.example.popmovies.api.IPopMoviesApi;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class ApiClientService {

    private static IPopMoviesApi client;
    private static String PROD = "http://api.themoviedb.org/3";
    private static String ROOT = PROD;

    public static String getBaseUrl() {
        return ROOT;
    }

    static {
        setupRestClient();
    }

    public static IPopMoviesApi get() {
        return client;
    }

    public static void setBaseUrl(String url) {
        PROD = url;
    }

    private static void setupRestClient() {

        Gson gson = new GsonBuilder()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setConverter(new GsonConverter(gson))
                .build();
        client = restAdapter.create(IPopMoviesApi.class);
    }
}
