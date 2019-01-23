package com.example.android.popularmoviesstagetwo.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    /**
     * Your API key goes below in API_KEY
     */
    private static final String API_KEY = "Place your MovieDB API KEY here";

    private static final String BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w500";
    private static final String TAG = "NetworkUtils";
    private static final String MOVIE_QUERY_API = "api_key";
    private static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";


    /**
     * Builds the URL to fetch the Movie JSON
     * @param movieUrl of type String
     * @return url
     */
    public static URL urlBuilder(String movieUrl) {

        Uri uri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                .appendPath(movieUrl)
                .appendQueryParameter(MOVIE_QUERY_API, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Issue with creating url", e);
        }
        return url;
    }

    /**
     * Builds the URL to fetch the movie Trailers JSON
     * @param id of type String
     * @return url
     */
    public static URL urlTrailerBuilder(String id) {

        Uri uri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(VIDEOS)
                .appendQueryParameter(MOVIE_QUERY_API, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Issue with creating url", e);
        }
        return url;
    }

    /**
     * Builds the URL to fetch the movie Reviews JSON
     * @param id of type String
     * @return url
     */
    public static URL urlReviewsBuilder(String id) {

        Uri uri = Uri.parse(THEMOVIEDB_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(REVIEWS)
                .appendQueryParameter(MOVIE_QUERY_API, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Issue with creating url", e);
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    public static String buildPosterUrl(String poster) {

        return BASE_URL + POSTER_SIZE + "/" + poster;

    }
}
