package com.example.jidekareem.movieapp.utilities;

import android.support.annotation.Nullable;

import com.example.jidekareem.movieapp.data.database.MovieEntry;
import com.example.jidekareem.movieapp.data.network.MovieResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class JsonUtil2 {

    private final static String POSTER_BASE = "http://image.tmdb.org/t/p/w185/";

    private static MovieEntry[] fromJson(JSONObject movieJson) throws JSONException {

        JSONArray results = movieJson.optJSONArray("results");
        MovieEntry[] movieEntries = new MovieEntry[results.length()];


        for (int i = 0; i < results.length(); i++) {
            JSONObject mMovie = results.getJSONObject(i);
            MovieEntry movieEntry = fromJson1(mMovie);
            movieEntries[i] = movieEntry;
        }
        return movieEntries;
    }

    private static MovieEntry fromJson1(final JSONObject mMovie) {
        String title = mMovie.optString("title");
        String releaseDate = mMovie.optString("release_date");
        String synopsis = mMovie.optString("overview");
        double rating = mMovie.optDouble("vote_average");
        String poster = mMovie.optString("poster_path");
        String mid = mMovie.optString("id");
        MovieEntry movieEntry = new MovieEntry(title, releaseDate, synopsis, rating,
                POSTER_BASE + poster, mid, false, false);


        return movieEntry;
    }

    public static ArrayList<String> parseReviewJson(String json) {
        ArrayList<String> reviews = new ArrayList<>();
        try {
            JSONObject reviewJson = new JSONObject(json);
            JSONArray results = reviewJson.optJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject mMovie = results.getJSONObject(i);
                String content = mMovie.optString("content");
                reviews.add(content);
            }
            return reviews;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> parseKeyJson(String json) {
        ArrayList<String> trailerKeys = new ArrayList<>();
        try {
            JSONObject trailerJson = new JSONObject(json);
            JSONArray results = trailerJson.optJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject mMovie = results.getJSONObject(i);
                String key = mMovie.optString("key");
                String link = "http://www.youtube.com/watch?v=" + key;
                trailerKeys.add(link);
            }
            return trailerKeys;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public MovieResponse parse(String movieJsonStr) throws JSONException {
        JSONObject movieJson = new JSONObject(movieJsonStr);
        MovieEntry[] movieEntries = fromJson(movieJson);
        return new MovieResponse(movieEntries);
    }


}
