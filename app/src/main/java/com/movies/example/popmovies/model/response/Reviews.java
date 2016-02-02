
package com.movies.example.popmovies.model.response;

import java.util.ArrayList;
import java.util.List;

public class Reviews {

    public Integer id;
    public Integer page;
    public List<ReviewDetails> results = new ArrayList<ReviewDetails>();
    public Integer totalPages;
    public Integer totalResults;

}
