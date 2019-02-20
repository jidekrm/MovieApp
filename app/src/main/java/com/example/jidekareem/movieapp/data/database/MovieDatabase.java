package com.example.jidekareem.movieapp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MovieEntry.class}, version = 13, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movieDb7";
    private static MovieDatabase mInstance;

    public static MovieDatabase getmInstance(Context context) {

        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }

        }
        return mInstance;
    }

    public abstract MovieDao movieDao();

}
