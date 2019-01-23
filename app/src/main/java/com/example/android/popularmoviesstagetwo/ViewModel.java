package com.example.android.popularmoviesstagetwo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.popularmoviesstagetwo.data.FavoriteMovies;
import com.example.android.popularmoviesstagetwo.data.MoviesDatabase;

import java.util.List;

class ViewModel extends AndroidViewModel {

    private static final String TAG = ViewModel.class.getSimpleName();

    private final LiveData<List<FavoriteMovies>> movies;

    public ViewModel(Application application) {
        super(application);
        MoviesDatabase database = MoviesDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving favorite movies from the DataBase");
        movies = database.moviesDao().loadAllMovies();
    }

    public LiveData<List<FavoriteMovies>> getMovies() {
        return movies;
    }
}