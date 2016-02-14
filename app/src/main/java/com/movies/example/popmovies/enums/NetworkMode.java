package com.movies.example.popmovies.enums;

/**
 * Created by Renuka Challa on 9/17/15.
 */
public enum NetworkMode {
    ONLINE ("online"),
    OFFLINE("offline");

    private String value;

    private NetworkMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
