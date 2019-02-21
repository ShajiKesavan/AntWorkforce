package com.sample.poc.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sample.poc.Adapter.FeedbackListAdapter;
import com.sample.poc.Adapter.InterestedListAdapter;
import com.sample.poc.Items.FeedbackListItem;
import com.sample.poc.Items.InterestListItem;
import com.sample.poc.R;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {

    RecyclerView listFeedback;
    FeedbackListAdapter feedbackListAdapter;
    ArrayList<FeedbackListItem> feedbackListItems= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        feedbackListItems= new ArrayList<>();
        listFeedback=(RecyclerView)findViewById(R.id.listFeedback);

        feedbackListAdapter = new FeedbackListAdapter(feedbackListItems,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listFeedback.setLayoutManager(mLayoutManager);
        listFeedback.setItemAnimator(new DefaultItemAnimator());
        listFeedback.setAdapter(feedbackListAdapter);

        prepareFeedbackData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void preparePostedJobData()
    {

        FeedbackListItem feedbackListItem=new FeedbackListItem("","Stephen Flemming",4,
                "Excellent Communication skills",R.drawable.unknown);
        feedbackListItems.add(feedbackListItem);
        feedbackListItem=new FeedbackListItem("","Jose Ricardo",1,
                "Poor performance",R.drawable.unknown);
        feedbackListItems.add(feedbackListItem);

        feedbackListAdapter.notifyDataSetChanged();
    }

    void prepareFeedbackData()
    {

        int totalCnt=InterestedListAdapter.interestItem.getFeedbackListItems().size();
        for(int i=0;i<totalCnt;i++)
        {
            FeedbackListItem feedbackListItem=new FeedbackListItem("","",
                    InterestedListAdapter.interestItem.getFeedbackListItems().get(i).getRating(),
                    InterestedListAdapter.interestItem.getFeedbackListItems().get(i).getFeedbackData(),R.drawable.unknown);
            feedbackListItems.add(feedbackListItem);
        }

        feedbackListAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
