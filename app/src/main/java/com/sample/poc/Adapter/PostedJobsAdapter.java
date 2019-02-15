package com.sample.poc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.poc.Activities.InterestReceviedListActivity;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;

import java.util.List;

/**
 * Created by 1013373 on 7/31/2018.
 */

public class PostedJobsAdapter extends RecyclerView.Adapter<PostedJobsAdapter.MyViewHolder> {

    private List<PostedJobItems> postedJobItems;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDateTimeValue, roleValue, rateValue,durationValue,posts,accepted,confirmed,interest_received;

        public MyViewHolder(View view) {
            super(view);
            txtDateTimeValue = (TextView) view.findViewById(R.id.txtDateTimeValue);
            roleValue = (TextView) view.findViewById(R.id.roleValue);
            rateValue = (TextView) view.findViewById(R.id.rateValue);
            durationValue = (TextView) view.findViewById(R.id.durationValue);
            posts = (TextView) view.findViewById(R.id.posts);
            accepted = (TextView) view.findViewById(R.id.accepted);
            confirmed = (TextView) view.findViewById(R.id.confirmed);
            interest_received = (TextView) view.findViewById(R.id.interest_received);
        }
    }


    public PostedJobsAdapter(List<PostedJobItems> postedJobItems,Context context) {
        this.postedJobItems = postedJobItems;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posted_jobs_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final PostedJobItems postedJobItem = postedJobItems.get(position);
        holder.txtDateTimeValue.setText(postedJobItem.getStartDate());
        holder.roleValue.setText(postedJobItem.getRole());
        holder.rateValue.setText(postedJobItem.getRate());
        holder.durationValue.setText(postedJobItem.getDuration());
        holder.posts.setText(context.getResources().getString(R.string.posts)+"("+postedJobItem.getPostsCount()+")");
        holder.accepted.setText(context.getResources().getString(R.string.accepted)+"("+postedJobItem.getAcceptedCount()+")");
        holder.confirmed.setText(context.getResources().getString(R.string.confirmed)+"("+postedJobItem.getConfirmedCount()+")");

        final String udata=context.getResources().getString(R.string.interest_received)+"("+postedJobItem.getInterestReceivedCount()+")";
        final SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        holder.interest_received.setText(content);

        holder.interest_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(postedJobItem.getInterestReceivedCount()>0) {
                    Intent intent = new Intent(context, InterestReceviedListActivity.class);
                    intent.putExtra("id", postedJobItem.getStartTime());
                    intent.putExtra("role", postedJobItem.getRole());
                    intent.putExtra("title", udata);
                    intent.putExtra("rate", postedJobItem.getRate());
                    intent.putExtra("date", postedJobItem.getStartDate());
                    context.startActivity(intent);
                }

            }
        });
        //holder.interest_received.setText("#INTEREST RECEIVED("+postedJobItem.getConfirmedCount()+")");
    }

    @Override
    public int getItemCount() {
        return postedJobItems.size();
    }
}