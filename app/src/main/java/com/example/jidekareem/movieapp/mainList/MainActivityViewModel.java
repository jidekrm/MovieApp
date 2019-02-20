package com.example.jidekareem.movieapp.mainList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.jidekareem.movieapp.data.MovieRepository;
import com.example.jidekareem.movieapp.data.database.MovieListEntry;

import java.util.List;

 class MainActivityViewModel extends ViewModel {
    private final MovieRepository repository;
    private final LiveData<List<MovieListEntry>>  movie;

    public MainActivityViewModel(MovieRepository repository ) {
        this.repository = repository;
        this.movie = repository.getNewMovies();
    }

    public LiveData<List<MovieListEntry>> getMovie() {
        return movie;
    }

}
