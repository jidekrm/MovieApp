package com.example.jidekareem.movieapp.mAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jidekareem.movieapp.R;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final TrailerAdapterOnClickHandler mClickHandler;
    private List<String> mTrailerData;

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_links;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        trailerAdapterViewHolder.trailerButtonImage.getDrawable();
        trailerAdapterViewHolder.trailerNumberView.setText(String.valueOf(position + 1));


    }

    @Override
    public int getItemCount() {
        if (null == mTrailerData) return 0;
        return mTrailerData.size();
    }

    public void setTrailerData(List<String> reviewData) {
        mTrailerData = reviewData;
        notifyDataSetChanged();
    }


    public interface TrailerAdapterOnClickHandler {
        void onClick(String nTrailer);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView trailerNumberView;

        public final ImageView trailerButtonImage;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            trailerNumberView = itemView.findViewById(R.id.trailer_number);
            trailerButtonImage = itemView.findViewById(R.id.trailer_image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String nReview = mTrailerData.get(adapterPosition);
            mClickHandler.onClick(nReview);
        }
    }

}
