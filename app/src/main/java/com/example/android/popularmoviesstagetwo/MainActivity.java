package com.example.android.popularmoviesstagetwo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmoviesstagetwo.data.FavoriteMovies;
import com.example.android.popularmoviesstagetwo.model.Movies;
import com.example.android.popularmoviesstagetwo.utils.JsonUtils;
import com.example.android.popularmoviesstagetwo.utils.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ListItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String queryMovie = "popular";
    private String nameSort = "Popular Movies";
    private String lastQueryMovie = "popular";
    private String lastNameSort = "Popular Movies";
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;

    private List<FavoriteMovies> mFavoriteMovies;
    private ArrayList<Movies> mMovie;

    private Bundle mSavedInstanceState;
    private RecyclerViewAdapter mMoviesAdapter;
    private GridLayoutManager layoutManager;
    private Parcelable savedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.pb_loading_indicator);

        setTitle(nameSort);

        layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mMoviesAdapter = new RecyclerViewAdapter(MainActivity.this, mMovie, MainActivity.this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mFavoriteMovies = new ArrayList<>();

        if (savedInstanceState == null) {
            setupViewModel();
        } else {
            setupViewModel();

            ArrayList<Movies> result = new ArrayList<>();

            String ids[] = savedInstanceState.getStringArray("id");
            String titles[] = savedInstanceState.getStringArray("title");
            String posters[] = savedInstanceState.getStringArray("poster");
            String plots[] = savedInstanceState.getStringArray("plot");
            String ratings[] = savedInstanceState.getStringArray("rating");
            String releaseDates[] = savedInstanceState.getStringArray("release");
            queryMovie = savedInstanceState.getString("query");

            for (int i = 0; i < ids.length; i++) {
                Movies movie = new Movies(ids[i], titles[i], posters[i], plots[i], ratings[i], releaseDates[i]);
                result.add(movie);
            }

            mMovie = result;

            mMoviesAdapter.setMovieData(mMovie);
            mMoviesAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);

            savedRecyclerLayoutState = savedInstanceState.getParcelable("position");
            if (mMovie != null) {
                layoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
            }
        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * This is for setting your view preferences so you may
     * see what movies you'd like from the options menu
     * @param item of type MenuItem
     * @return super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.popularity:
                lastQueryMovie = "popular";
                lastNameSort = "Popular Movies";
                queryMovie = "popular";
                movieSearch();
                nameSort = "Popular Movies";
                setTitle(nameSort);
                break;
            case R.id.top_rated:
                lastQueryMovie = "top_rated";
                lastNameSort = "Top Rated Movies";
                queryMovie = "top_rated";
                movieSearch();
                nameSort = "Top Rated Movies";
                setTitle(nameSort);
                break;
            case (R.id.sort_favorites):
                queryMovie = "favorites";
                nameSort = "Favorite Movies";
                setTitle(nameSort);
                movieSearch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void OnListItemClick(Movies movieItem) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movieItem", movieItem);
        startActivity(intent);

    }

    class FetchMovie extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mRecyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieResponse = null;

            try {
                movieResponse = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return movieResponse;
        }

        protected void onPostExecute(String movieResponse) {
            new FetchMovie().cancel(true);
            if ((movieResponse != null && !movieResponse.equals(""))) {
                hideProgress();

                try {
                    mMovie = JsonUtils.getMovieFromJson(movieResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mMoviesAdapter.setMovieData(mMovie);
                mMoviesAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                Log.e(LOG_TAG, "Problems with adapter");
            }
        }
    }

    /**
     * Clears the movie list so that it can be filled with your favorites
     */
    private void ClearMovieItemList() {
        if (mMovie != null) {
            mMovie.clear();
        } else {
            mMovie = new ArrayList<>();
        }
    }

    /**
     * Decides whether or not to show your favorite movies depending on
     * if you have selected any yet or to go back to last movie query
     */
    private void movieSearch() {
        if (queryMovie.equals("favorites") && !mFavoriteMovies.isEmpty()) {

                ClearMovieItemList();
                for (int i = 0; i < mFavoriteMovies.size(); i++) {
                    Movies movies = new Movies(
                            String.valueOf(mFavoriteMovies.get(i).getId()),
                            mFavoriteMovies.get(i).getTitle(),
                            mFavoriteMovies.get(i).getImage(),
                            mFavoriteMovies.get(i).getSynopsis(),
                            mFavoriteMovies.get(i).getVote(),
                            mFavoriteMovies.get(i).getReleaseDate()
                    );
                    mMovie.add(movies);
                }
                mMoviesAdapter.setMovieData(mMovie);

        } else if (queryMovie.equals("favorites") && mFavoriteMovies.isEmpty()) {
            queryMovie = lastQueryMovie;
            setTitle(lastNameSort);
            movieSearch();
            Toast.makeText(this,
                    "No movies are in your Favorites!", Toast.LENGTH_SHORT).show();
        } else {
            URL movieUrl = NetworkUtils.urlBuilder(queryMovie);
            new FetchMovie().execute(movieUrl);
        }
    }

    /**
     * Sets the view model up and decides what kind of movieSearch() will be done
     */
    private void setupViewModel() {
        ViewModel viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovies>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovies> favoriteMovies) {
                if (favoriteMovies.size() > 0 && mSavedInstanceState == null) {
                    mFavoriteMovies.clear();
                    mFavoriteMovies = favoriteMovies;
                    movieSearch();
                } else if (favoriteMovies.size() > 0 && mSavedInstanceState != null) {
                    mFavoriteMovies.clear();
                    mFavoriteMovies = favoriteMovies;
                    movieSearch();
                } else if (favoriteMovies.size() == 0 && mSavedInstanceState != null) {
                    mFavoriteMovies.clear();
                    mFavoriteMovies = favoriteMovies;
                    mSavedInstanceState = null;
                    movieSearch();
                } else if (favoriteMovies.size() == 0) {
                    mFavoriteMovies.clear();
                    mFavoriteMovies = favoriteMovies;
                    movieSearch();
                }
                for (int i = 0; i < mFavoriteMovies.size(); i++) {
                    Log.d(LOG_TAG, mFavoriteMovies.get(i).getTitle());
                }
            }
        });
    }

    /**
     * Saves your position upon screen rotation
     * @param outState of type Bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savedRecyclerLayoutState = layoutManager.onSaveInstanceState();

        mMovie = mMoviesAdapter.getMovieData();
        String[] moviesId = new String[mMovie.size()];
        String[] movieTitle = new String[mMovie.size()];
        String[] moviesPosters = new String[mMovie.size()];
        String[] moviePlot = new String[mMovie.size()];
        String[] movieRatings = new String[mMovie.size()];
        String[] movieReleaseDate = new String[mMovie.size()];
        String query = queryMovie;
        for (int i = 0; i < mMovie.size(); i++) {
            moviesId[i] = mMovie.get(i).getmId();
            movieTitle[i] = mMovie.get(i).getmTitle();
            moviesPosters[i] = mMovie.get(i).getmMoviePoster();
            moviePlot[i] = mMovie.get(i).getmPlot();
            movieRatings[i] = mMovie.get(i).getmRating();
            movieReleaseDate[i] = mMovie.get(i).getmReleaseDate();
        }
        outState.putParcelable("position", savedRecyclerLayoutState);
        outState.putStringArray("id", moviesId);
        outState.putStringArray("title", movieTitle);
        outState.putStringArray("poster", moviesPosters);
        outState.putStringArray("plot", moviePlot);
        outState.putStringArray("rating", movieRatings);
        outState.putStringArray("release", movieReleaseDate);

        outState.putString("query", query);
    }
}