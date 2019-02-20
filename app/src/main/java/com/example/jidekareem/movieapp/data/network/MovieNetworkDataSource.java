package com.example.jidekareem.movieapp.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jidekareem.movieapp.MovieExecutor;
import com.example.jidekareem.movieapp.data.MovieRepository;
import com.example.jidekareem.movieapp.data.database.MovieEntry;
import com.example.jidekareem.movieapp.data.sync.MovieSyncIntentService;
import com.example.jidekareem.movieapp.data.sync.ReviewSyncIntentService;
import com.example.jidekareem.movieapp.data.sync.TrailerSyncIntentService;
import com.example.jidekareem.movieapp.utilities.JsonUtil2;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieNetworkDataSource {
    private static final Object LOCK = new Object();
    private static MutableLiveData<List<String>> nReview;
    private static MutableLiveData<List<String>> nKey;
    private static MovieNetworkDataSource sInstance;
    public final MovieExecutor movieExecutor;
    private final Context context;
    private final MutableLiveData<MovieEntry[]> mutableLiveData;

    private MovieNetworkDataSource(Context context, MovieExecutor movieExecutor) {
        this.context = context;
        this.movieExecutor = movieExecutor;
        this.mutableLiveData = new MutableLiveData<>();
        nReview = new MutableLiveData<>();
        nKey = new MutableLiveData<>();
    }

    public static MovieNetworkDataSource getsInstance(Context context, MovieExecutor executor) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieNetworkDataSource(context.getApplicationContext(), executor);
            }
        }
        return sInstance;
    }

    public static LiveData<List<String>> getnReview() {
        return nReview;
    }

    public static LiveData<List<String>> getnKey() {
        return nKey;
    }

    public LiveData<MovieEntry[]> getCurrentMovieData() {
        return mutableLiveData;
    }

    public void startMovieFetchService() {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        context.startService(intent);
    }


    public void startTrailerService() {
        Intent intent = new Intent(context, TrailerSyncIntentService.class);
        context.startService(intent);
    }

    public void startReviewService() {
        Intent intent = new Intent(context, ReviewSyncIntentService.class);
        context.startService(intent);
    }

    public synchronized void fetchMovie(String sorter) {
        movieExecutor.networkIO().execute(() -> {
            try {
                URL mSearchUrl = NetworkUtils.buildUrl(sorter);
                String searchResults = NetworkUtils.getResponseFromHttpURL(mSearchUrl);
                Log.i("FETCHMOVIE NDS" ,"" + searchResults);
                MovieResponse movieValues = new JsonUtil2().parse(searchResults);
                if (movieValues != null && movieValues.getMovies().length != 0) {
                    mutableLiveData.postValue(movieValues.getMovies());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void fetchFav() {
        movieExecutor.diskIO().execute(() -> {
            MovieEntry[] movieEntryFav = MovieRepository.getmInstance().getAllFavMovie(true);
            MovieRepository.getmInstance().deleteOldData();
            mutableLiveData.postValue(movieEntryFav);
        });
    }

    public synchronized void fetchReview(String sorter) {
        movieExecutor.networkIO().execute(() -> {
            try {
                URL mSearchUrl = NetworkUtils.buildUrlRT(sorter);
                String searchResults = NetworkUtils.getResponseFromHttpURL(mSearchUrl);
                Log.i("FETCHMOVIE NDS R" ,"" + searchResults);

                ArrayList<String> reviewValues = JsonUtil2.parseReviewJson(searchResults);
                if (reviewValues != null && reviewValues.size() != 0) {
                    nReview.postValue(reviewValues);
                } else {
                    ArrayList<String> reviewValuesForNull = new ArrayList<>();
                    reviewValuesForNull.add("No Reviews Yet");
                    nReview.postValue(reviewValuesForNull);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void fetchTrailer(String sorter) {
        movieExecutor.networkIO().execute(() -> {
            try {
                URL mSearchUrl = NetworkUtils.buildUrlRT(sorter);
                String searchResults = NetworkUtils.getResponseFromHttpURL(mSearchUrl);
                Log.i("FETCHMOVIE NDS T" ,"" + searchResults);
                ArrayList<String> keyValues = JsonUtil2.parseKeyJson(searchResults);
                if (keyValues != null && keyValues.size() != 0) {
                    nKey.postValue(keyValues);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
