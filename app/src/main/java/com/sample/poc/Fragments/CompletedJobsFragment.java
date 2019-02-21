package com.sample.poc.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sample.poc.Activities.AntApplication;
import com.sample.poc.Activities.DashboardActivity;
import com.sample.poc.Adapter.CompletedJobsAdapter;
import com.sample.poc.Adapter.PostedJobsAdapter;
import com.sample.poc.Items.CompletedJobItem;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1013373 on 7/31/2018.
 */

public class CompletedJobsFragment extends Fragment {


    private List<CompletedJobItem> completedJobItems = new ArrayList<>();

    RecyclerView listCompletedJobs;
    CompletedJobsAdapter completedJobsAdapter;

    public CompletedJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView=inflater.inflate(R.layout.fragment_completed_jobs, container, false);
        completedJobItems = new ArrayList<>();
        listCompletedJobs = (RecyclerView) parentView.findViewById(R.id.listCompletedJobs);

        completedJobsAdapter = new CompletedJobsAdapter(completedJobItems,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listCompletedJobs.setLayoutManager(mLayoutManager);
        listCompletedJobs.setItemAnimator(new DefaultItemAnimator());
        listCompletedJobs.setAdapter(completedJobsAdapter);
        preparePostedJobData();
        return parentView;
    }


    void preparePostedJobData()
    {
        CompletedJobItem completedJobItem = new CompletedJobItem("28/07/2018","08:00",
                "Registered Mental Health Nurse","£20.50/Hour","8 Hours",
                "Soniya Jacob", Constants.PENDING);
        completedJobItems.add(completedJobItem);

        completedJobItem = new CompletedJobItem("24/08/2018","08:30","Plumber",
                "£30.75/Hour","4 Hours","Johny Dep",Constants.APPROVED);
        completedJobItems.add(completedJobItem);

        completedJobItem = new CompletedJobItem("22/07/2018","09:30","Electrician",
                "£18.75/Hour","3 Hours","Dwayne Johnson",Constants.PENDING);
        completedJobItems.add(completedJobItem);

        completedJobItem = new CompletedJobItem("29/07/2018","12:30","Driver",
                "£25.00/Hour","8 Hours","Tom Hardy",Constants.PENDING);
        completedJobItems.add(completedJobItem);

        completedJobsAdapter.notifyDataSetChanged();
    }

}
