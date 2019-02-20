package com.example.jidekareem.movieapp.mAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jidekareem.movieapp.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<String> mReviewData;

    public ReviewAdapter() {

    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_details;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {

        String review = mReviewData.get(position);
        reviewAdapterViewHolder.reviewTextView.setText(review);


    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.size();
    }

    public void setReviewData(List<String> reviewData) {
           if (mReviewData == null){
               mReviewData = reviewData;
               notifyDataSetChanged();
           }else {
               DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                   @Override
                   public int getOldListSize() {
                       return mReviewData.size();
                   }

                   @Override
                   public int getNewListSize() {
                       return mReviewData.size();
                   }

                   @Override
                   public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                       return false;
                   }

                   @Override
                   public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                       return mReviewData.get(oldItemPosition).equals(reviewData.get(newItemPosition));
                   }
               });
               mReviewData = reviewData;
               result.dispatchUpdatesTo(this);
           }

    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView reviewTextView;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            reviewTextView = view.findViewById(R.id.review_data);
        }

    }
}
