package com.example.jidekareem.movieapp.data.sync;

import android.app.IntentService;
import android.content.Intent;

import com.example.jidekareem.movieapp.MovieExecutor;
import com.example.jidekareem.movieapp.data.network.MovieNetworkDataSource;
import com.example.jidekareem.movieapp.data.network.NetworkUtils;

public class ReviewSyncIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public ReviewSyncIntentService() {
        super("ReviewSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MovieNetworkDataSource.getsInstance(getApplicationContext(), MovieExecutor.getInstance()).fetchReview(NetworkUtils.REVIEWS);
    }
}
