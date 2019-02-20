package com.example.jidekareem.movieapp.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.jidekareem.movieapp.data.MovieRepository;
import com.example.jidekareem.movieapp.data.database.MovieEntry;

public class DetailActivityViewModel extends ViewModel {

    private final LiveData<MovieEntry> movieEntryLiveData;
    private final MovieRepository repository;
    private final String movieID;

    public DetailActivityViewModel(MovieRepository repository, String movieID) {
        this.repository = repository;
        this.movieID = movieID;
        this.movieEntryLiveData = repository.getNewMoviesByMId(movieID);
    }

    public LiveData<MovieEntry> getMovieEntryLiveData() {
        return movieEntryLiveData;
    }
}
