package com.example.jidekareem.movieapp.data.network;

import android.net.Uri;
import android.util.Log;

import com.example.jidekareem.movieapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    public final static String SORT_BY_RATED = "top_rated";
    public final static String SORT_BY_POPULARITY = "popular";
    public final static String VIDEO_TRAILER = "videos";
    public final static String REVIEWS = "reviews";
    private final static String API_KEY = BuildConfig.M_API_KEY;
    private final static String PARAM_QUERY = "api_key";
    private final static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static String ID = "";

    public static URL buildUrl(String sorter) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sorter)
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.i("FETCHMOVIE NU" ,"" + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildUrlRT(String sorter) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(ID)
                .appendPath(sorter)
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
            Log.i("FETCHMOVIE NU 2" ,"" + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getResponseFromHttpURL(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
