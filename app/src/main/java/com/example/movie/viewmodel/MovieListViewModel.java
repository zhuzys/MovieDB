package com.example.movie.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie.models.Movie;
import com.example.movie.repository.MovieRepository;
import com.example.movie.response.MovieResponse;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    private MovieRepository movieRepository;

    //this class is used for View Model

   /* //Live Data
    private MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();*/

    //Construtor

    public MovieListViewModel () {
        movieRepository = MovieRepository.getInstance();

    }

    public LiveData<List<Movie>> getMovies() {
        return movieRepository.getMovies();
    }
    public LiveData<List<Movie>> getPop() {
        return movieRepository.getPop();
    }




    //Calling method in view-model
    public void searchMovieApp(String query, int pageNumber) {
        movieRepository.searchMovieApi(query, pageNumber);
    }


    public void searchMoviePop(int pageNumber) {
        movieRepository.searchMoviePop( pageNumber);
    }

    public  void searchNextPage() {
        movieRepository.searchNextPage();

    }

}
