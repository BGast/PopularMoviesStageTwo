package com.example.android.popularmoviesstagetwo.utils;

import com.example.android.popularmoviesstagetwo.model.Movies;
import com.example.android.popularmoviesstagetwo.model.Reviews;
import com.example.android.popularmoviesstagetwo.model.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class JsonUtils {

    private static final String MOVIE_RESULTS = "results";

    private static final String PARAM_KEY = "key";
    private static final String PARAM_NAME = "name";

    private static final String PARAM_AUTHOR = "author";
    private static final String PARAM_CONTENT = "content";

    private JsonUtils() {
        throw new AssertionError();
    }

    /**
     * Gathers movie data from JSON to fill the Movie model with its needed data
     * @param json of type String
     * @return result
     */
    public static ArrayList<Movies> getMovieFromJson(String json) {
        try {
            Movies movie;

            JSONObject json_object = new JSONObject(json);

            JSONArray movieArray = new JSONArray(json_object.optString(MOVIE_RESULTS, "[\"\"]"));

            ArrayList<Movies> result = new ArrayList<>();

            for (int i = 0; i < movieArray.length(); i++) {
                String item = movieArray.optString(i, "");
                JSONObject movieJson = new JSONObject(item);

                movie = new Movies(
                        movieJson.optString("id", "Not Available"),
                        movieJson.optString("original_title", "Not Available"),
                        movieJson.optString("poster_path", "Not Available"),
                        movieJson.optString("overview", "Not Available"),
                        movieJson.optString("vote_average", "Not Available"),
                        movieJson.optString("release_date", "Not Available")

                );

                result.add(movie);
            }
            return result;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gathers movie data from JSON to fill the Trailers model with its needed data
     * @param json of type String
     * @return result
     */
    public static Trailers[] getTrailerFromJson(String json) throws JSONException {

        JSONObject trailerJson = new JSONObject(json);

        JSONArray trailersArray = trailerJson.getJSONArray(MOVIE_RESULTS);

        Trailers[] result = new Trailers[trailersArray.length()];

        for (int i = 0; i < trailersArray.length(); i++) {
            String trailer_key, trailer_title;

            trailer_key = trailersArray.getJSONObject(i).optString(PARAM_KEY);
            trailer_title = trailersArray.getJSONObject(i).optString(PARAM_NAME);

            Trailers movieTrailer = new Trailers(trailer_key, trailer_title);

            result[i] = movieTrailer;
        }
        return result;
    }

    /**
     * Gathers movie data from JSON to fill the Reviews model with its needed data
     * @param json of type String
     * @return result
     */
    public static Reviews[] getMovieReviewsFromJson(String json) throws JSONException {

        JSONObject reviewJson = new JSONObject(json);

        JSONArray reviewsArray = reviewJson.getJSONArray(MOVIE_RESULTS);

        Reviews[] result = new Reviews[reviewsArray.length()];

        for (int i = 0; i < reviewsArray.length(); i++) {
            String authors, reviews;

            authors = reviewsArray.getJSONObject(i).optString(PARAM_AUTHOR);
            reviews = reviewsArray.getJSONObject(i).optString(PARAM_CONTENT);

            Reviews movieReviews = new Reviews(authors, reviews);

            result[i] = movieReviews;
        }
        return result;
    }
}