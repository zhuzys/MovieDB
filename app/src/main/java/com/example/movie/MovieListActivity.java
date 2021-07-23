package com.example.movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.movie.adapter.MovieRecyclerView;
import com.example.movie.adapter.OnMovieListener;
import com.example.movie.databinding.ActivityMovieListBinding;
import com.example.movie.models.Movie;
import com.example.movie.response.MovieSearchResponse;
import com.example.movie.utils.Credentials;
import com.example.movie.utils.MovieApi;
import com.example.movie.request.Service;
import com.example.movie.viewmodel.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {
    //add netword security
    ActivityMovieListBinding binding;
    private MovieRecyclerView movieRecyclerViewAdapter;
    //ViewModel
    private MovieListViewModel movieListViewModel;
    boolean isPopular = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SetupSearchView();

        movieListViewModel =new  ViewModelProvider(this).get(MovieListViewModel.class);
        ConfigureRV();
        //Calling observes
        ObserveAnyChange();

        ObservePopularMovies();
        //Geting popular movies
        movieListViewModel.searchMoviePop(1);


        //search movie
      // searchMovieApi("fast", 1);



      /* binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  GetRetrofitResponse();
              //  GetRetrofitResponseAccordingToID();

                //Displaying only the result of page 1
                searchMovieApi("Fast", 1);
            }
        });*/
    }

    private void ObservePopularMovies() {
        movieListViewModel.getPop().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null) {
                    for (Movie movie: movies) {
                        movieRecyclerViewAdapter.setmMovies(movies);
                    }
                }

            }
        });
    }


    //Observing any data change
    private  void ObserveAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null) {
                    for (Movie movie: movies) {
                        movieRecyclerViewAdapter.setmMovies(movies);
                    }
                }

            }
        });
    }

  /*  //4- Calling method in Main Actviity
    private void searchMovieApi(String query, int pageNumber) {
        movieListViewModel.searchMovieApp(query, pageNumber);

    }*/


    //5 Init recyclerView & add data
    private void ConfigureRV() {
        //Livr
        movieRecyclerViewAdapter = new MovieRecyclerView( this);
        binding.recyclerView.setAdapter(movieRecyclerViewAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //loading next page
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)) {
                    //Here we need to display next searcg results on the next page of api
                    movieListViewModel.searchNextPage();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    //loading next page of api response


    @Override
    public void onMovieClick(int position) {
        Toast.makeText(this, "Clcik", Toast.LENGTH_SHORT).show();
        //we need id of the order to get all details
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);


    }

    @Override
    public void onCategoryClick(String category) {

    }
    //search $ query
    private void SetupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApp(
                        //The search hetting from searchView
                        query,
                        1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular =false;
            }
        });

    }


 /*   private void GetRetrofitResponse() {
        MovieApi movieApi = Service.getMovieApi();
        Call<MovieSearchResponse> responseCall = movieApi.searchMovie(
                Credentials.API_KEY,
                "Jack Reacher",
                1);
        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if (response.code() == 200) {
                    Log.d("Tag", "the response"+ response.body().toString());
                    List<Movie> movies = new ArrayList<>(response.body().getMovies());
                    for (Movie movie: movies) {
                        Log.v("Tag", "The List "+ movie.getRelease_date());

                    }

                }
                else {
                    try {
                        Log.v("Tag", "Error" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });
    }

    private void GetRetrofitResponseAccordingToID() {
        MovieApi movieApi = Service.getMovieApi();
        Call<Movie> responseCall = movieApi.getMovie(
                550, Credentials.API_KEY);
        responseCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response.code() == 200) {
                    Movie movie = response.body();
                    Log.d("Tag", "The Response"+ movie.getTitle());

                }
                else {
                    try {
                        Log.v("Tag", "Error"+ response.errorBody().string());
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });

    }*/


}