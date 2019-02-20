package com.example.jidekareem.movieapp.mainList;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.jidekareem.movieapp.data.MovieRepository;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieRepository repository;

    public MovieViewModelFactory(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainActivityViewModel(repository);
    }
}
