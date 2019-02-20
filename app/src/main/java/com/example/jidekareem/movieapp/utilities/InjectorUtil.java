package com.example.jidekareem.movieapp.utilities;

import android.content.Context;

import com.example.jidekareem.movieapp.MovieExecutor;
import com.example.jidekareem.movieapp.data.MovieRepository;
import com.example.jidekareem.movieapp.data.database.MovieDatabase;
import com.example.jidekareem.movieapp.data.network.MovieNetworkDataSource;
import com.example.jidekareem.movieapp.details.DetailViewModelFactory;
import com.example.jidekareem.movieapp.mainList.MovieViewModelFactory;


public class InjectorUtil {

    public static MovieRepository provideRepository(Context context) {
        MovieDatabase database = MovieDatabase.getmInstance(context.getApplicationContext());
        MovieExecutor executor = MovieExecutor.getInstance();
        MovieNetworkDataSource networkDataSource =
                MovieNetworkDataSource.getsInstance(context.getApplicationContext(), executor);
        return MovieRepository.getmInstance(database.movieDao(), networkDataSource, executor);
    }

    public static MovieNetworkDataSource provideNetworkDataSource(Context context) {
        provideRepository(context.getApplicationContext());
        MovieExecutor executor = MovieExecutor.getInstance();
        return MovieNetworkDataSource.getsInstance(context.getApplicationContext(), executor);
    }

    public static MovieViewModelFactory provideMainActivityViewModelFactory(Context context) {
        MovieRepository repository = provideRepository(context.getApplicationContext());
        return new MovieViewModelFactory(repository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context applicationContext, String movieIdIn) {
        MovieRepository movieRepository = provideRepository(applicationContext.getApplicationContext());
        return new DetailViewModelFactory(movieRepository, movieIdIn);
    }
}
