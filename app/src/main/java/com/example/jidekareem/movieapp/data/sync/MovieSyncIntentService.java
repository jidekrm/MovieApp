package com.example.jidekareem.movieapp.data.sync;

import android.app.IntentService;
import android.content.Intent;

import com.example.jidekareem.movieapp.data.network.MovieNetworkDataSource;
import com.example.jidekareem.movieapp.data.network.NetworkUtils;
import com.example.jidekareem.movieapp.mainList.MainActivity;
import com.example.jidekareem.movieapp.utilities.InjectorUtil;

public class MovieSyncIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * //     * @param name Used to name the worker thread, important only for debugging.
     */
    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        MovieNetworkDataSource networkDataSource =
                InjectorUtil.provideNetworkDataSource(this.getApplicationContext());

        if (MainActivity.MENU_SELECTED == 1){
            networkDataSource.fetchMovie(NetworkUtils.SORT_BY_POPULARITY);
        }else if (MainActivity.MENU_SELECTED == 2){
            networkDataSource.fetchMovie(NetworkUtils.SORT_BY_RATED);
        }
        else if (MainActivity.MENU_SELECTED == 3){
            networkDataSource.fetchMovie(NetworkUtils.SORT_BY_POPULARITY);
        }
    }
}
