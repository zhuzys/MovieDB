package com.example.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.movie.databinding.ActivityMovieDetailsBinding;
import com.example.movie.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
   ActivityMovieDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GetDataFromIntent();
    }

    private void GetDataFromIntent() {
        if(getIntent().hasExtra("movie")){
            Movie movie = getIntent().getParcelableExtra("movie");

            binding.titleDetails.setText(movie.getTitle());
            binding.desc.setText(movie.getMovie_overview());
            binding.ratingBar.setRating(movie.getVote_average()/2);

            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+ movie.getPoster_path()).into(binding.imageDetails);




        }
    }
}

//