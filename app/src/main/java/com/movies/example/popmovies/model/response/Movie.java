
package com.movies.example.popmovies.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    @SerializedName("poster_path")
    public String poster_path;
    public Boolean adult;
    @SerializedName("overview")
    public String overview;
    @SerializedName("release_date")
    public String release_date;
    public List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    public Integer id;
    @SerializedName("original_title")
    public String original_title;
    public String original_language;
    @SerializedName("title")
    public String title;
    public String backdrop_path;
    public Float popularity;
    public Integer vote_count;
    public Boolean video;
    @SerializedName("vote_average")
    public Float vote_average;

    @Override
    public String toString() {
        return original_title;
    }

//    @Override
//    public int compareTo(Object another) {
//        int compareMovieId=(((Movie) another).id);
//        return (this.id) - compareMovieId;
//    }
}
