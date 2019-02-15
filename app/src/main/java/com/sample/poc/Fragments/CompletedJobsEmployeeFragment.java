package com.sample.poc.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.poc.Adapter.AcceptedJobsAdapter;
import com.sample.poc.Adapter.CompletedJobsAdapter;
import com.sample.poc.Adapter.CompletedJobsEmployeeAdapter;
import com.sample.poc.Items.AcceptedJobsItem;
import com.sample.poc.Items.CompletedEmployeeJobItem;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class CompletedJobsEmployeeFragment extends Fragment {


    private List<CompletedEmployeeJobItem> completedEmployeeJobItems = new ArrayList<>();

    RecyclerView listCompletedJobs;
    CompletedJobsEmployeeAdapter completedJobsEmployeeAdapter;

    public CompletedJobsEmployeeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView=inflater.inflate(R.layout.fragment_completed_jobs_employee, container, false);

        listCompletedJobs = (RecyclerView) parentView.findViewById(R.id.listCompletedJobs);
        completedEmployeeJobItems = new ArrayList<>();

        completedJobsEmployeeAdapter = new CompletedJobsEmployeeAdapter(completedEmployeeJobItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listCompletedJobs.setLayoutManager(mLayoutManager);
        listCompletedJobs.setItemAnimator(new DefaultItemAnimator());
        listCompletedJobs.setAdapter(completedJobsEmployeeAdapter);

        // inflater.inflate(R.layout.fragment_posted_jobs, container, false);

        preparePostedJobData();
        return parentView;
    }


    private void preparePostedJobData() {
        CompletedEmployeeJobItem completedEmployeeJobItem= new CompletedEmployeeJobItem("28/07/2018", "08:00", "Registered Mental Health Nurse", "£20.50/Hour", "8 Hours", "0.2 miles", "BlueBell", "", Constants.TIMESHEET_STATUS_APPROVED);
        completedEmployeeJobItems.add(completedEmployeeJobItem);

        completedEmployeeJobItem = new CompletedEmployeeJobItem("24/08/2018", "08:30", "Plumber", "£30.75/Hour", "4 Hours", "2.7 miles", "Horizon", "",Constants.TIMESHEET_STATUS_SUBMIT);
        completedEmployeeJobItems.add(completedEmployeeJobItem);

        completedEmployeeJobItem = new CompletedEmployeeJobItem("22/07/2018", "09:30", "Electrician", "£18.75/Hour", "3 Hours", "1.4 miles", "Amazon", "",Constants.TIMESHEET_STATUS_SUBMIT);
        completedEmployeeJobItems.add(completedEmployeeJobItem);

        completedEmployeeJobItem = new CompletedEmployeeJobItem("29/07/2018", "12:30", "Driver", "£25.00/Hour", "8 Hours", "7.6 miles","Google","",Constants.TIMESHEET_STATUS_APPROVED);
        completedEmployeeJobItems.add(completedEmployeeJobItem);

        completedJobsEmployeeAdapter .notifyDataSetChanged();
    }

}
