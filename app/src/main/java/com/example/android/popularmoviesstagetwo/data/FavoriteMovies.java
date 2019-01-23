package com.example.android.popularmoviesstagetwo.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName="FavoriteMovies")
public class FavoriteMovies {

    @PrimaryKey
    private int id;
    private String title;
    private String releaseDate;
    private String vote;
    private String synopsis;
    private String image;

    public FavoriteMovies(int id, String title, String releaseDate, String vote, String synopsis, String image) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.vote = vote;
        this.synopsis = synopsis;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVote() {
        return vote;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

