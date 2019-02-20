package com.example.jidekareem.movieapp.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jidekareem.movieapp.R;
import com.example.jidekareem.movieapp.data.MovieRepository;
import com.example.jidekareem.movieapp.data.database.MovieEntry;
import com.example.jidekareem.movieapp.data.network.MovieNetworkDataSource;
import com.example.jidekareem.movieapp.data.network.NetworkUtils;
import com.example.jidekareem.movieapp.databinding.ActivityDetailBinding;
import com.example.jidekareem.movieapp.mAdapters.ReviewAdapter;
import com.example.jidekareem.movieapp.mAdapters.TrailerAdapter;
import com.example.jidekareem.movieapp.utilities.InjectorUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {
    public static final String MOVIE_ID_EXTRA = "MOVIE_ID_EXTRA";
    public static ReviewAdapter mReviewAdapter;
    public static TrailerAdapter mTrailerAdapter;
    public String title;
    public String releaseDate;
    public String synopsis;
    public double rating;
    public String poster;
    public String movieId;
    private ActivityDetailBinding activityDetailBinding;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        ViewSetUp();

        String movieIdIn = getIntent().getStringExtra(MOVIE_ID_EXTRA);
        NetworkUtils.ID = movieIdIn;
        DetailViewModelFactory factory = InjectorUtil.provideDetailViewModelFactory(this.getApplicationContext(), movieIdIn);
        DetailActivityViewModel detailActivityViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);
        detailActivityViewModel.getMovieEntryLiveData().observe(this, movieEntry -> {
            if (movieEntry != null) {

                populateDetails(movieEntry);
                FavButtonStateChecker(movieEntry);
            }
        });

        if (checkInternetAvailability()) {
            MovieNetworkDataSource.getsInstance(getApplicationContext(), MovieRepository.getmInstance()
                    .getMovieExecutor()).startReviewService();
            MovieNetworkDataSource.getsInstance(getApplicationContext(), MovieRepository.getmInstance()
                    .getMovieExecutor()).startTrailerService();
        }


        LiveData<List<String>> netData = MovieNetworkDataSource.getnReview();
        netData.observeForever((List<String> reviews) -> mReviewAdapter.setReviewData(reviews));

        LiveData<List<String>> netDataTrailer = MovieNetworkDataSource.getnKey();
        netDataTrailer.observeForever((List<String> trailerKeys) -> mTrailerAdapter.setTrailerData(trailerKeys));
        activityDetailBinding.favoriteIcon.setOnClickListener(v -> {
            if (activityDetailBinding.favoriteIcon.isChecked()) {
                activityDetailBinding.favoriteIcon.setBackground(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                updateFavoriteData(true);

            } else {
                activityDetailBinding.favoriteIcon.setBackground(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                updateFavoriteData(false);
            }
        });
    }

    private void ViewSetUp() {
        RecyclerView mRecyclerView = findViewById(R.id.review_recycler);
        RecyclerView mRecyclerView2 = findViewById(R.id.trailer_recycler);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mRecyclerView.setAdapter(mReviewAdapter);
        mRecyclerView2.setLayoutManager(layoutManager2);
        mRecyclerView2.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerView2.setAdapter(mTrailerAdapter);
    }

    private void populateDetails(MovieEntry movieEntry) {
        title = movieEntry.getTitle();
        synopsis = movieEntry.getSynopsis();
        releaseDate = movieEntry.getReleaseDate();
        rating = movieEntry.getRating();
        poster = movieEntry.getPoster();
        movieId = movieEntry.getMovieId();
        activityDetailBinding.synopsis.setText(movieEntry.getSynopsis());
        activityDetailBinding.releaseDate.setText(movieEntry.getReleaseDate());
        activityDetailBinding.ratings.setText(String.format("%s/10", String.valueOf(movieEntry.getRating())));
        Picasso.with(this)
                .load(movieEntry.getPoster())
                .into(activityDetailBinding.posterSmall);
        setTitle(movieEntry.getTitle());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void FavButtonStateChecker(MovieEntry movieEntry) {
        if (movieEntry.isFavorite()) {
            activityDetailBinding.favoriteIcon.setChecked(true);
            activityDetailBinding.favoriteIcon.setBackground(getResources().getDrawable(android.R.drawable.btn_star_big_on));
        } else {
            activityDetailBinding.favoriteIcon.setChecked(false);
            activityDetailBinding.favoriteIcon.setBackground(getResources().getDrawable(android.R.drawable.btn_star_big_off));
        }
    }

    private void updateFavoriteData(boolean b) {
        MovieNetworkDataSource.getsInstance(this, MovieRepository.getmInstance()
                .getMovieExecutor()).movieExecutor.diskIO().execute(() -> {
            MovieEntry favoriteEntry = new MovieEntry(title, releaseDate, synopsis, rating, poster, movieId, b, b);
            MovieRepository.getmInstance().movieDao.bulkInsert(favoriteEntry);
        });
    }

    @Override
    public void onClick(String nTrailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(nTrailer)));
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
