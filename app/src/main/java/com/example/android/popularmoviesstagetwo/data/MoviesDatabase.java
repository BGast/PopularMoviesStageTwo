package com.example.android.popularmoviesstagetwo.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FavoriteMovies.class}, version = 3, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase{
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movieslist";
    private static MoviesDatabase sInstance;

    /**
     * @param context of type Context
     * @return sInstance
     */
    public static MoviesDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MoviesDatabase.class, MoviesDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MoviesDao moviesDao();
}