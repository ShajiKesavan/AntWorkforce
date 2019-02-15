package com.sample.poc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sample.poc.Activities.InterestReceviedListActivity;
import com.sample.poc.Items.FeedbackListItem;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;

import java.util.List;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.MyViewHolder> {

    private List<FeedbackListItem> feedbackListItems;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView feedback_name, feedback_value;
        public ImageView img_pic;
        public RatingBar ratingBar;

        public MyViewHolder(View view) {
            super(view);
            feedback_name=view.findViewById(R.id.feedback_name);
            img_pic=view.findViewById(R.id.img_pic);
            ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);
            feedback_value=view.findViewById(R.id.feedback_value);
        }
    }


    public FeedbackListAdapter(List<FeedbackListItem> feedbackListItems,Context context) {
        this.feedbackListItems = feedbackListItems;
        this.context=context;
    }

    @Override
    public FeedbackListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_row, parent, false);

        return new FeedbackListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedbackListAdapter.MyViewHolder holder, int position) {
        FeedbackListItem feedbackListItem = feedbackListItems.get(position);

        holder.feedback_name.setText(feedbackListItem.getUserName());
        holder.feedback_value.setText(feedbackListItem.getFeedbackData());
        holder.img_pic.setImageResource(feedbackListItem.getImageResource());
        holder.ratingBar.setRating(feedbackListItem.getRating());
    }

    @Override
    public int getItemCount() {
        return feedbackListItems.size();
    }
}
