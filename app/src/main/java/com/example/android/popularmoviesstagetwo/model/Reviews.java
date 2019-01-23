package com.example.android.popularmoviesstagetwo.model;

public class Reviews {

    private final String mReviewAuthor;
    private final String mReview;

    public Reviews(String mReviewAuthor, String mReview) {
        this.mReviewAuthor = mReviewAuthor;
        this.mReview = mReview;
    }

    public String getmReviewAuthor() {
        return mReviewAuthor;
    }

    public String getmReview() {
        return mReview;
    }
}
