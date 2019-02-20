package com.example.jidekareem.movieapp.data.sync;

import android.app.IntentService;
import android.content.Intent;

import com.example.jidekareem.movieapp.MovieExecutor;
import com.example.jidekareem.movieapp.data.network.MovieNetworkDataSource;
import com.example.jidekareem.movieapp.data.network.NetworkUtils;

public class TrailerSyncIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TrailerSyncIntentService() {
        super("TrailerSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        MovieNetworkDataSource.getsInstance(getApplicationContext(), MovieExecutor.getInstance()).fetchTrailer(NetworkUtils.VIDEO_TRAILER);
    }
}
