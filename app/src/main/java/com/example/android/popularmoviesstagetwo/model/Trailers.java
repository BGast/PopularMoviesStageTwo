package com.example.android.popularmoviesstagetwo.model;

public class Trailers {

    private final String mTrailerKey;
    private final String mTrailerTitle;

    public Trailers(String mTrailerKey, String mTrailerTitle) {
        this.mTrailerKey = mTrailerKey;
        this.mTrailerTitle = mTrailerTitle;
    }

    public String getmTrailerKey() {
        return mTrailerKey;
    }

    public String getmTrailerTitle() {
        return mTrailerTitle;
    }
}
