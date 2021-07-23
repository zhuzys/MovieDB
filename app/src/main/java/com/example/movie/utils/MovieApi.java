package com.example.movie.utils;

import com.example.movie.models.Movie;
import com.example.movie.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    //Search for movies
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page


    );
    //making search with ID
    //movie_id =550 is for Jack Reache
    @GET("3/movie/{movie_id}?")
    Call<Movie> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key

    );
    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key")String key,
            @Query("page") int page

    );


}
