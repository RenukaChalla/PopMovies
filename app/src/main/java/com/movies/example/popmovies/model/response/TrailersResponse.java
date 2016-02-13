
package com.movies.example.popmovies.model.response;

import com.movies.example.popmovies.models.TrailerDetails;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Renuka Challa on 09/02/16.
 */
public class TrailersResponse {

    public Integer id;
    public List<TrailerDetails> results = new ArrayList<TrailerDetails>();

}
