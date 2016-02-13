
package com.movies.example.popmovies.model.response;

import com.google.gson.annotations.SerializedName;
import com.movies.example.popmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Renuka Challa on 09/02/16.
 */
public class MovieResponse {

    public Integer page;
    @SerializedName("results")
    public List<Movie> results = new ArrayList<Movie>();
    public Integer total_results;
    public Integer total_pages;
}
