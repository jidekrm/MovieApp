package com.example.jidekareem.movieapp.mainList;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jidekareem.movieapp.R;
import com.example.jidekareem.movieapp.data.MovieRepository;
import com.example.jidekareem.movieapp.data.database.MovieListEntry;
import com.example.jidekareem.movieapp.data.network.MovieNetworkDataSource;
import com.example.jidekareem.movieapp.details.DetailActivity;
import com.example.jidekareem.movieapp.mAdapters.MovieAdapter;
import com.example.jidekareem.movieapp.utilities.InjectorUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.GridItemClickListener {

    public static int MENU_SELECTED = 1;
    int posterWidth = 185;
    private MovieAdapter movieAdapter;
    private RecyclerView mRecyclerView;
    private TextView errorMessageView;
    private TextView favoriteMessageView;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.main_recycle);
        errorMessageView = findViewById(R.id.data_error);
        favoriteMessageView = findViewById(R.id.data_favorite);
        progressBar = findViewById(R.id.data_loading_indicator);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateBestPosterSpanCount(posterWidth));

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(movieAdapter);


        MovieViewModelFactory factory = InjectorUtil.provideMainActivityViewModelFactory(this.getApplicationContext());
        MainActivityViewModel mainActivityViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mainActivityViewModel.getMovie().observe(this, (List<MovieListEntry> movieEntries) -> {

            if (MENU_SELECTED == 1 || MENU_SELECTED == 2) {
                List<MovieListEntry> movieEntries1 = getMovieListEntries(movieEntries);
                dataView(movieEntries1);

            } else if (MENU_SELECTED == 3) {
                List<MovieListEntry> movieEntries2 = new ArrayList<>();
                for (int i = 0; i < movieEntries.size(); i++) {
                    if (movieEntries.get(i).isFavorite() == true) {
                        movieEntries2.add(movieEntries.get(i));
                    }
                }

                movieAdapter.setMoviesData(movieEntries2);
                if (movieEntries2 != null && movieEntries2.size() != 0) {
                    dataView(movieEntries2);

                } else {
                    favoriteMessageView.setVisibility(View.VISIBLE);
                }

            } else {

                List<MovieListEntry> movieEntries3 = getMovieListEntries(movieEntries);
                dataView(movieEntries3);
            }
        });
    }

    @NonNull
    private List<MovieListEntry> getMovieListEntries(List<MovieListEntry> movieEntries) {
        List<MovieListEntry> movieEntries3 = new ArrayList<>();

        for (int i = 0; i < movieEntries.size(); i++) {

            if (!movieEntries.get(i).isOld()) {
                movieEntries3.add(movieEntries.get(i));
            }
        }
        movieAdapter.setMoviesData(movieEntries3);
        return movieEntries3;
    }

    private void dataView(List<MovieListEntry> movieEntries) {
        if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (movieEntries != null && movieEntries.size() != 0) {
            showDataView();

        } else {
            showLoading();
        }
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showDataView() {
        progressBar.setVisibility(View.INVISIBLE);
        errorMessageView.setVisibility(View.INVISIBLE);
        favoriteMessageView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onGridItemClick(String mId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_ID_EXTRA, mId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuSelected = item.getItemId();
        if (menuSelected == R.id.popularity) {
            MENU_SELECTED = 1;
            if (checkInternetAvailability()) {
                MovieNetworkDataSource.getsInstance(this, MovieRepository.getmInstance()
                        .getMovieExecutor()).startMovieFetchService();
            }


        } else if (menuSelected == R.id.top_rated) {
            MENU_SELECTED = 2;
            if (checkInternetAvailability()) {
                MovieNetworkDataSource.getsInstance(this, MovieRepository.getmInstance()
                        .getMovieExecutor()).startMovieFetchService();
            }

        } else if (menuSelected == R.id.favorite) {
            MENU_SELECTED = 3;
            if (checkInternetAvailability()) {
                MovieNetworkDataSource.getsInstance(this, MovieRepository.getmInstance()
                        .getMovieExecutor()).startMovieFetchService();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private int calculateBestPosterSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    //Check if Internet is available
    public boolean checkInternetAvailability() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }

}
