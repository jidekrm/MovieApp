package com.example.jidekareem.movieapp.data;

import android.arch.lifecycle.LiveData;

import com.example.jidekareem.movieapp.MovieExecutor;
import com.example.jidekareem.movieapp.data.database.MovieDao;
import com.example.jidekareem.movieapp.data.database.MovieEntry;
import com.example.jidekareem.movieapp.data.database.MovieListEntry;
import com.example.jidekareem.movieapp.data.network.MovieNetworkDataSource;
import com.example.jidekareem.movieapp.mainList.MainActivity;

import java.util.List;

public class MovieRepository {

    private static final Object LOCK = new Object();
    private static boolean mInitialized = false;
    private static MovieRepository mInstance;
    public final MovieDao movieDao;
    private final MovieExecutor movieExecutor;
    private final MovieNetworkDataSource movieNetworkDataSource;

    private MovieRepository(MovieDao movieDao, MovieNetworkDataSource movieNetworkDataSource, MovieExecutor movieExecutor) {
        this.movieDao = movieDao;
        this.movieNetworkDataSource = movieNetworkDataSource;
        this.movieExecutor = movieExecutor;

        LiveData<MovieEntry[]> networkData = movieNetworkDataSource.getCurrentMovieData();
        networkData.observeForever((MovieEntry[] newMovieFromNetwork) -> movieExecutor.diskIO().execute(() -> {

            MovieEntry[] movieEntryFav = getAllFavMovie(true);
            if (MainActivity.MENU_SELECTED == 1 || MainActivity.MENU_SELECTED == 2) {
                deleteOldData();
                movieDao.bulkInsert(newMovieFromNetwork);
                movieDao.bulkInsert(movieEntryFav);
                DataUpdates(movieDao, movieEntryFav);
            } else if (MainActivity.MENU_SELECTED == 3) {
                deleteOldData();
                movieDao.bulkInsert(movieEntryFav);
            }
        }));
    }

    public synchronized static MovieRepository getmInstance(MovieDao movieDao,
                                                            MovieNetworkDataSource movieNetworkDataSource,
                                                            MovieExecutor movieExecutor) {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new MovieRepository(movieDao, movieNetworkDataSource, movieExecutor);
            }
        }
        return mInstance;
    }

    public static MovieRepository getmInstance() {
        return mInstance;
    }

    private void DataUpdates(MovieDao movieDao, MovieEntry[] movieEntryFav) {
        MovieEntry[] movieEntryNew = getAllNewlyLoaded();


        if (movieEntryFav != null && movieEntryFav.length != 0 && movieEntryNew != null && movieEntryNew.length != 0) {
            for (int i = 0; i < movieEntryFav.length; i++) {
                for (MovieEntry aMovieEntryNew : movieEntryNew) {
                    if (movieEntryFav[i].getMovieId().equals(aMovieEntryNew.getMovieId())) {
                        MovieEntry movieEntryh = new MovieEntry(movieEntryFav[i].getTitle(), movieEntryFav[i].getReleaseDate(),
                                movieEntryFav[i].getSynopsis(), movieEntryFav[i].getRating(), movieEntryFav[i].getPoster(),
                                movieEntryFav[i].getMovieId(), true, false);
                        movieDao.bulkInsert(movieEntryh);
                    } else {
                        MovieEntry movieEntryhh = new MovieEntry(movieEntryFav[i].getTitle(), movieEntryFav[i].getReleaseDate(),
                                movieEntryFav[i].getSynopsis(), movieEntryFav[i].getRating(), movieEntryFav[i].getPoster(),
                                movieEntryFav[i].getMovieId(), true, true);

                        movieDao.bulkInsert(movieEntryhh);
                    }
                }
            }
        }
    }

    private synchronized void initializeData() {
        if (mInitialized) return;
        mInitialized = true;
        movieExecutor.diskIO().execute(this::startFetchMovieService);
    }

    public LiveData<List<MovieListEntry>> getNewMovies() {
        initializeData();
        return movieDao.loadMovies();

    }


    public LiveData<MovieEntry> getNewMoviesByMId(String mId) {
        initializeData();
        return movieDao.loadMovieByMId(mId);

    }

    public MovieEntry[] getAllFavMovie(boolean fav) {
        initializeData();
        return movieDao.loadMovieByFavorite(fav);
    }

    public String[] getFavMovieId(boolean fav) {
        initializeData();
        return movieDao.selectMovieIdByFav(fav);
    }

    private MovieEntry[] getAllNewlyLoaded() {
        initializeData();
        return movieDao.loadAllNewlyLoaded();

    }

    public void deleteOldData() {
        movieDao.deleteOldData(0.0);
    }

    public MovieExecutor getMovieExecutor() {
        return movieExecutor;
    }

    private void startFetchMovieService() {
        movieNetworkDataSource.startMovieFetchService();
    }


}
