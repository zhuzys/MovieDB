package com.example.movie.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie.AppExecutors;
import com.example.movie.models.Movie;
import com.example.movie.response.MovieSearchResponse;
import com.example.movie.utils.Credentials;
import com.example.movie.viewmodel.MovieListViewModel;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    //Live Data for search
    private MutableLiveData<List<Movie>>  mMovies;
    private static MovieApiClient instance;
    //makeing Global request
    private RetrieveMoviesRunnable  retrieveMoviesRunnable;


    //Live Data for popular movies
    private MutableLiveData<List<Movie>> moviesPop;
    //makeing Global request
    private RetrieveMoviesRunnablePop  retrieveMoviesRunnablePop;


    public static  MovieApiClient getInstance(){
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient()
    {
        mMovies = new MutableLiveData<>();
        moviesPop = new MutableLiveData<>();
    }



    public LiveData<List<Movie>> getMovies() {
        return  mMovies;
    }
    public LiveData<List<Movie>> getMoviesPop() {
        return  moviesPop;
    }










    //This method that Im gonna call through classes
    public void searchMoviesApi(String query, int pageNumber) {
        if(retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;

        }
        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable); 

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancelling the retrofit call
                myHandler.cancel(true);
            }
        }, 4000, TimeUnit.MILLISECONDS);
    }



    public void searchMoviesApiPop( int pageNumber) {
        if(retrieveMoviesRunnablePop != null) {
            retrieveMoviesRunnablePop = null;

        }
        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop( pageNumber);


       // final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);
        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePop);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancelling the retrofit call
                myHandler2.cancel(true);
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }



    //REST API by runnable class
    //2 types of queries: ID & search
    private class RetrieveMoviesRunnable implements  Runnable {
        private String query;
        private int pageNumber;
        boolean cancelRequest;
//?
        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            this.cancelRequest = cancelRequest;
        }

        @Override
        public void run() {
            //Get the response objects
            try {
                Response response = getMovies(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if(response.code() == 200) {
                    List<Movie> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1) {
                        //Sending data to live data
                        //postValue used for backgroun thread
                        mMovies.postValue(list);
                    }
                    else {
                        List<Movie> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);

                    }
                }
                else {
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error:" + error);
                    mMovies.postValue(null);

                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }



        }
            //search query method
            private Call<MovieSearchResponse>  getMovies(String query, int pageNumber) {
                return Service.getMovieApi().searchMovie(
                        Credentials.API_KEY,
                        query,
                       pageNumber
                );
            }
            private void cancelRequest() {
                Log.v("Tag", "Cancelling Search Request");
                cancelRequest = true;
            }
        }


    private class RetrieveMoviesRunnablePop implements  Runnable {

        private int pageNumber;
        boolean cancelRequest;
        //?
        public RetrieveMoviesRunnablePop( int pageNumber) {

            this.pageNumber = pageNumber;
            this.cancelRequest = cancelRequest;
        }

        @Override
        public void run()  {
            //Get the response objects
            try {
                Response response2 = getPop(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if(response2.code() == 200) {
                    List<Movie> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if (pageNumber == 1) {
                        //Sending data to live data
                        //postValue used for backgroun thread
                        moviesPop.postValue(list);
                    }
                    else {
                        List<Movie> currentMovies =moviesPop.getValue();
                        currentMovies.addAll(list);
                        moviesPop.postValue(currentMovies);

                    }
                }
                else {
                    String error = response2.errorBody().string();
                    Log.v("Tag", "Error:" + error);
                    mMovies.postValue(null);

                }
            } catch (IOException e) {
                e.printStackTrace();
                moviesPop.postValue(null);
            }



        }
        //search query method
        private Call<MovieSearchResponse>  getPop( int pageNumber) {
            return Service.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }
        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }
    }


