package com.example.jidekareem.movieapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM mymovies")
    LiveData<List<MovieListEntry>> loadMovies();

    @Query("SELECT * FROM mymovies")
    MovieEntry[] loadAllNewlyLoaded();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(MovieEntry... movieEntry);

    @Query("DELETE FROM mymovies WHERE rating > :rating")
    void deleteOldData(double rating);


    @Query("SELECT * FROM mymovies WHERE movieId = :movieId")
    LiveData<MovieEntry> loadMovieByMId(String movieId);


    @Query("SELECT * FROM mymovies WHERE favorite = :favorite")
    MovieEntry[] loadMovieByFavorite(boolean favorite);


    @Query("SELECT movieId FROM mymovies WHERE favorite = :favorite")
    String[] selectMovieIdByFav(boolean favorite);


}
