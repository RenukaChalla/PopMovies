package com.movies.example.popmovies.deserailizer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.movies.example.popmovies.model.response.Movie;
import com.movies.example.popmovies.model.response.MovieResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Renuka Challa on 09/02/16.
 */
public class MovieResponseDeserializer implements JsonDeserializer<MovieResponse> {

    @Override
    public MovieResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        JsonElement value = json.getAsJsonObject();
        JsonObject jsonObject = json.getAsJsonObject();
        MovieResponse movieResponse = null;
        if (jsonObject != null) {
            movieResponse = gson.fromJson(jsonObject, MovieResponse.class);
            Iterable<Map.Entry<String, JsonElement>> entries = value.getAsJsonObject().entrySet();

            for (Map.Entry<String, JsonElement> entry : entries) {
                if (entry.getKey().equalsIgnoreCase("results")) {
                    JsonArray jsonArray = entry.getValue().getAsJsonArray();
                    int size = jsonArray.size();
                    movieResponse.results = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        JsonObject jObject = jsonArray.get(i).getAsJsonObject();
                        Movie movie = new Movie();
                        if (entry.getKey().equalsIgnoreCase("id")) {
                            movie.id = entry.getValue().getAsInt();
                        }
                        if (entry.getKey().equalsIgnoreCase("poster_path")) {
                            movie.poster_path = entry.getValue().getAsString();
                        }
                        if (entry.getKey().equalsIgnoreCase("overview")) {
                            movie.overview = entry.getValue().getAsString();
                        }
                        if (entry.getKey().equalsIgnoreCase("release_date")) {
                            movie.release_date = entry.getValue().getAsString();
                        }
                        if (entry.getKey().equalsIgnoreCase("title")) {
                            movie.title = entry.getValue().getAsString();
                        }
                        if (entry.getKey().equalsIgnoreCase("vote_average")) {
                            movie.vote_average = entry.getValue().getAsFloat();
                        }
                    }
                }
            }

        }
        return movieResponse;
    }
}
