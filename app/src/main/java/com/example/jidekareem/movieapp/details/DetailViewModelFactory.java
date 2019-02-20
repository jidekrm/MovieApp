package com.example.jidekareem.movieapp.details;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.jidekareem.movieapp.data.MovieRepository;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository repository;
    private final String movieId;

    public DetailViewModelFactory(MovieRepository repository, String movieId) {
        this.repository = repository;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(repository, movieId);
    }
}
