package com.example.jidekareem.movieapp.mAdapters;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jidekareem.movieapp.R;
import com.example.jidekareem.movieapp.data.database.MovieListEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    final private GridItemClickListener mOnClickListener;
    private List<MovieListEntry> lMovies;

    public MovieAdapter(GridItemClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {

        String posterUrl = lMovies.get(position).getPoster();
        Picasso.with(movieAdapterViewHolder.itemView.getContext())
                .load(posterUrl)
                .into(movieAdapterViewHolder.posterView);
    }

    @Override
    public int getItemCount() {
        if (null == lMovies) return 0;
        return lMovies.size();
    }


    public void setMoviesData(List<MovieListEntry> newMovies) {
        if (lMovies == null) {
            lMovies = newMovies;
            notifyDataSetChanged();
        } else {

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return lMovies.size();
                }

                @Override
                public int getNewListSize() {
                    return newMovies.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return lMovies.get(oldItemPosition).getId() ==
                            newMovies.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    MovieListEntry newMovie = newMovies.get(newItemPosition);
                    MovieListEntry oldMovie = lMovies.get(oldItemPosition);
                    return newMovie.getId() == oldMovie.getId()
                            && newMovie.getMovieId().equals(oldMovie.getMovieId());
                }
            });
            lMovies = newMovies;
            result.dispatchUpdatesTo(this);
        }

    }

    public interface GridItemClickListener {
        void onGridItemClick(String clickedItemIndex);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView posterView;


        MovieAdapterViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.poster_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String mId = lMovies.get(clickedPosition).getMovieId();
            mOnClickListener.onGridItemClick(mId);

        }
    }
}

