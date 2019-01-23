package com.example.android.popularmoviesstagetwo.model;

import java.io.Serializable;

public class Movies implements Serializable {
    private final String mTitle;
    private final String mMoviePoster;
    private final String mPlot;
    private final String mRating;
    private final String mReleaseDate;
    private final String mId;

    public Movies(String mId, String mTitle, String mMoviePoster, String mPlot, String mRating, String mReleaseDate) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mMoviePoster = mMoviePoster;
        this.mPlot = mPlot;
        this.mRating = mRating;
        this.mReleaseDate = mReleaseDate;
    }


    public String getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmMoviePoster() {
        return mMoviePoster;
    }

    public String getmPlot() {
        return mPlot;
    }

    public String getmRating() {
        return mRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }
}
