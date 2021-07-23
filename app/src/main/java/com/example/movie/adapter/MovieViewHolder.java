package com.example.movie.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  //  TextView title, release_date, duration;
    ImageView  imageView;
    RatingBar ratingBar;

    //Click Listener
    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);
        this.onMovieListener = onMovieListener;
       /* title = itemView.findViewById(R.id.movie_title);
        release_date = itemView.findViewById(R.id.movie_category);
        duration = itemView.findViewById(R.id.movie_duration);*/
        imageView = itemView.findViewById(R.id.movie_img_pop);
        ratingBar = itemView.findViewById(R.id.rating_bar22);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
