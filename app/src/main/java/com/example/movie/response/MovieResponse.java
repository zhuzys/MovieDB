package com.example.movie.response;

import com.example.movie.models.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieResponse {
    //1- Finding the Movie Object
    @SerializedName("results")
    @Expose
    private Movie movie;
    public Movie getMovie() {
        return movie;

    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
