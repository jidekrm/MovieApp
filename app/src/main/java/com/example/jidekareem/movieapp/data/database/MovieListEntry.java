package com.example.jidekareem.movieapp.data.database;

public class MovieListEntry {

    public int id;
    private String title;
    private String releaseDate;
    private String synopsis;
    private double rating;
    private String poster;
    private String movieId;
    private boolean favorite;
    private boolean old;

    public MovieListEntry(int id, String title, String releaseDate, String synopsis, double rating, String poster, String movieId, boolean favorite, boolean old) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.synopsis = synopsis;
        this.rating = rating;
        this.poster = poster;
        this.movieId = movieId;
        this.favorite = favorite;
        this.old = old;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getRating() {
        return rating;
    }

    public String getPoster() {
        return poster;
    }

    public String getMovieId() {
        return movieId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isOld() {
        return old;
    }
}
