package com.example.jidekareem.movieapp.data.network;

import android.support.annotation.NonNull;

import com.example.jidekareem.movieapp.data.database.MovieEntry;

public class MovieResponse {

    @NonNull
    private final MovieEntry[] mMovies;

    public MovieResponse(@NonNull final MovieEntry[] movies) {
        mMovies = movies;
    }

    public MovieEntry[] getMovies() {
        return mMovies;
    }
}
