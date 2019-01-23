package com.example.android.popularmoviesstagetwo.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MoviesDao {
    @Query("SELECT * FROM FavoriteMovies ORDER BY id")
    LiveData<List<FavoriteMovies>> loadAllMovies();

    @Insert
    void insertMovie(FavoriteMovies favMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavoriteMovies favMovie);

    @Delete
    void deleteMovie(FavoriteMovies favMovie);

    @Query("SELECT * FROM FavoriteMovies WHERE id = :id")
    FavoriteMovies loadMovieById(int id);
}