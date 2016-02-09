
package com.movies.example.popmovies.model.response;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Renuka Challa on 09/02/16.
 */
public class ReviewsResponse {

    public Integer id;
    public Integer page;
    public List<ReviewDetails> results = new ArrayList<ReviewDetails>();
    public Integer totalPages;
    public Integer totalResults;

}
