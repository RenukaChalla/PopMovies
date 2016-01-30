
package com.movies.example.popmovies.model.response;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    public String poster_path;
    public Boolean adult;
    public String overview;
    public String release_date;
    public List<Integer> genreIds = new ArrayList<Integer>();
    public Integer id;
    public String original_title;
    public String original_language;
    public String title;
    public String backdrop_path;
    public Float popularity;
    public Integer vote_count;
    public Boolean video;
    public Float vote_average;
}
