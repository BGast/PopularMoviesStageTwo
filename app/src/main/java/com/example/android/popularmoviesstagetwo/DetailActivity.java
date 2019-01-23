package com.example.android.popularmoviesstagetwo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.popularmoviesstagetwo.data.FavoriteMovies;
import com.example.android.popularmoviesstagetwo.data.MoviesDatabase;
import com.example.android.popularmoviesstagetwo.model.Movies;
import com.example.android.popularmoviesstagetwo.model.Reviews;
import com.example.android.popularmoviesstagetwo.model.Trailers;
import com.example.android.popularmoviesstagetwo.utils.JsonUtils;
import com.example.android.popularmoviesstagetwo.utils.NetworkUtils;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private Trailers[] mTrailer;
    private Reviews[] mReview;

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mPlot;
    private TextView mRating;
    private TextView mRelease;
    private Button mFavorites;

    private LinearLayout mTrailerList;
    private LinearLayout mReviewList;

    private static String id;
    private static String poster;
    private static String title;
    private static String rate;
    private static String release;
    private static String overview;

    private int reviewCounter;

    private Boolean isFav = false;

    private Movies movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mPoster = findViewById(R.id.iv_poster_detail);
        mTitle = findViewById(R.id.tv_title);
        mPlot = findViewById(R.id.tv_plot);
        mRating = findViewById(R.id.tv_rating);
        mRelease = findViewById(R.id.tv_release_date);
        mTrailerList = findViewById(R.id.trailer_list);
        mReviewList = findViewById(R.id.review_list);
        mFavorites = findViewById(R.id.favorite_button);

        reviewCounter = 0;

        movie = (Movies) getIntent().getSerializableExtra("movieItem");

        getIntentExtra();

        new FetchTrailers().execute();
        new FetchReviews().execute();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final FavoriteMovies fmov = MoviesDatabase.getInstance(DetailActivity.this)
                        .moviesDao().loadMovieById(Integer.parseInt(id));
                setFavorite(fmov != null);
            }
        });
    }

    private void getIntentExtra() {
        id = movie.getmId();
        poster = movie.getmMoviePoster();
        title = movie.getmTitle();
        rate = movie.getmRating();
        release = movie.getmReleaseDate();
        overview = movie.getmPlot();

        createDetailView();
    }

    /**
     * Decides on how the favorites button will look based on if
     * you've added it to your favorites or unfavorited it
     * @param fav of type Boolean
     */
    private void setFavorite(Boolean fav) {
        if (fav) {
            isFav = true;
            mFavorites.setTextColor(getResources().getColor(R.color.added_to_favorite));
            mFavorites.setText(getString(R.string.favorite_marked));
        } else {
            isFav = false;
            mFavorites.setTextColor(getResources().getColor(R.color.button_text_color));
            mFavorites.setText(getString(R.string.mark_favorite));
        }
    }

    /**
     * Sets up the view for the movie you clicked on
     */
    private void createDetailView() {
        mTitle.setText(title);
        mPlot.setText(overview);
        mRating.setText(rate.concat("/10"));
        mRelease.setText(release);
        setTitle(title);

        String bindMovie = NetworkUtils.buildPosterUrl(poster);
        Glide.with(this)
                .load(bindMovie)
                .into(mPoster);

        mFavorites.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {       // creates the onClick for the favorites button
                final FavoriteMovies mov = new FavoriteMovies(
                        Integer.parseInt(movie.getmId()),
                        movie.getmTitle(),
                        movie.getmReleaseDate(),
                        movie.getmRating(),
                        movie.getmPlot(),
                        movie.getmMoviePoster()
                );
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() { // Adds/removes movie to/from database
                        if (isFav) {
                            MoviesDatabase.getInstance(DetailActivity.this).moviesDao().deleteMovie(mov);
                        } else {
                            MoviesDatabase.getInstance(DetailActivity.this).moviesDao().insertMovie(mov);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setFavorite(!isFav);
                            }
                        });
                    }
                });
            }
        });
    }

    class FetchTrailers extends AsyncTask<String, Void, Trailers[]> {
        @Override
        protected Trailers[] doInBackground(String... strings) {
            URL trailersUrl = NetworkUtils.urlTrailerBuilder(id);

            try {
                String trailerResponse = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
                mTrailer = JsonUtils.getTrailerFromJson(trailerResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return mTrailer;
        }

        @Override
        protected void onPostExecute(Trailers[] trailers) {
            super.onPostExecute(trailers);
            loadTrailersIntoView();
        }
    }

    /**
     * Adds the movie trailers to the detail_activity
     */
    private void loadTrailersIntoView() {
        int numberOfTrailers = mTrailer.length;

        if (numberOfTrailers == 0) {
            TextView noTrailers = new TextView(this);
            noTrailers.setText(R.string.no_trailers);
            noTrailers.setPadding(0, 0, 0, 50);
            noTrailers.setTextSize(16);
            mTrailerList.addView(noTrailers);
        } else {
            for (Trailers aTrailer : mTrailer) {
                Button trailerItem = new Button(this);
                trailerItem.setText(aTrailer.getmTrailerTitle());
                trailerItem.setPadding(0, 30, 0, 30);
                trailerItem.setTextSize(15);
                String TRAILER_BASE_URL = "http://youtube.com/watch?v=";
                final String trailerUrl = TRAILER_BASE_URL + aTrailer.getmTrailerKey();
                trailerItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri youtubeLink = Uri.parse(trailerUrl);
                        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, youtubeLink);
                        if (youtubeIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(youtubeIntent);
                        }
                    }
                });
                mTrailerList.addView(trailerItem);
            }
        }
    }

    class FetchReviews extends AsyncTask<String, Void, Reviews[]> {
        @Override
        protected Reviews[] doInBackground(String... strings) {
            URL reviewsUrl = NetworkUtils.urlReviewsBuilder(id);

            try {
                String reviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);
                mReview = JsonUtils.getMovieReviewsFromJson(reviewResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return mReview;
        }

        @Override
        protected void onPostExecute(Reviews[] reviews) {
            super.onPostExecute(reviews);
            loadReviewsIntoView();
        }
    }

    /**
     * Adds the movie reviews to the detail_activity
     */
    private void loadReviewsIntoView() {
        final int numberOfReviews = mReview.length;

        if (numberOfReviews == 0) {
            findViewById(R.id.author_text).setVisibility(View.GONE);
            findViewById(R.id.content_text).setVisibility(View.GONE);
            findViewById(R.id.next_review_button).setVisibility(View.GONE);

            TextView noReviewsAvailable = new TextView(this);
            noReviewsAvailable.setText(R.string.no_reviews);
            noReviewsAvailable.setPadding(0, 0, 0, 50);
            noReviewsAvailable.setTextSize(16);
            noReviewsAvailable.setTextColor(Color.WHITE);
            mReviewList.addView(noReviewsAvailable);
        } else {
            if (numberOfReviews == 1) {
                findViewById(R.id.next_review_button).setVisibility(View.GONE);
            }
            String authorHeader = mReview[reviewCounter].getmReviewAuthor() + ":";
            ((TextView) findViewById(R.id.author_text)).setText(authorHeader);
            ((TextView) findViewById(R.id.content_text)).setText(mReview[reviewCounter].getmReview());
            findViewById(R.id.next_review_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // Sets the onClick to click through the reviews
                    if (reviewCounter < numberOfReviews - 1) {
                        reviewCounter++;
                    } else {
                        reviewCounter = 0;
                    }
                    loadReviewsIntoView();
                }
            });
        }
    }
}