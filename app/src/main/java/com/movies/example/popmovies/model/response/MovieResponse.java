
package com.movies.example.popmovies.model.response;

import java.util.ArrayList;
import java.util.List;

public class MovieResponse {

    public Integer page;
    public List<Movie> results = new ArrayList<Movie>();
    public Integer total_results;
    public Integer total_pages;
}
