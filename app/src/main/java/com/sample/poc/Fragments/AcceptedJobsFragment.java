package com.sample.poc.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.poc.Activities.DashboardActivity;
import com.sample.poc.Adapter.AcceptedJobsAdapter;
import com.sample.poc.Adapter.ListJobsAdapter;
import com.sample.poc.Items.AcceptedJobsItem;
import com.sample.poc.Items.CompletedJobItem;
import com.sample.poc.Items.ListJobsItem;
import com.sample.poc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1013373 on 7/31/2018.
 */

public class AcceptedJobsFragment extends Fragment {


    private List<AcceptedJobsItem> acceptedJobsItems = new ArrayList<>();

    RecyclerView listAcceptedJobs;
    AcceptedJobsAdapter acceptedJobsAdapter;
    public AcceptedJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_accepted_jobs, container, false);
        listAcceptedJobs = (RecyclerView) parentView.findViewById(R.id.listAcceptedJobs);
        acceptedJobsItems = new ArrayList<>();

        acceptedJobsAdapter = new AcceptedJobsAdapter(acceptedJobsItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listAcceptedJobs.setLayoutManager(mLayoutManager);
        listAcceptedJobs.setItemAnimator(new DefaultItemAnimator());
        listAcceptedJobs.setAdapter(acceptedJobsAdapter);

        // inflater.inflate(R.layout.fragment_posted_jobs, container, false);

        //preparePostedJobData();
        getAcceptedJobs(DashboardActivity.shiftsJs);
        return parentView;
    }


    private void preparePostedJobData() {
        AcceptedJobsItem acceptedJobsItem= new AcceptedJobsItem("28/07/2018", "08:00", "Registered Mental Health Nurse", "£20.50/Hour", "8 Hours", "0.2 miles", "BlueBell", "");
        acceptedJobsItems.add(acceptedJobsItem);

        acceptedJobsItem = new AcceptedJobsItem("24/08/2018", "08:30", "Plumber", "£30.75/Hour", "4 Hours", "2.7 miles", "Horizon", "");
        acceptedJobsItems.add(acceptedJobsItem);

        acceptedJobsItem = new AcceptedJobsItem("22/07/2018", "09:30", "Electrician", "£18.75/Hour", "3 Hours", "1.4 miles", "Amazon", "");
        acceptedJobsItems.add(acceptedJobsItem);

        acceptedJobsItem = new AcceptedJobsItem("29/07/2018", "12:30", "Driver", "£25.00/Hour", "8 Hours", "7.6 miles","Google","");
        acceptedJobsItems.add(acceptedJobsItem);

        acceptedJobsAdapter.notifyDataSetChanged();
    }

    public void getAcceptedJobs(final String postResponse) {

        try {
            System.out.println("responseFromcmpltd2 :" + postResponse.toString());
            AcceptedJobsItem acceptedJobsItem;
            JSONArray jArray = new JSONArray(postResponse);
            JSONObject jObject = null;
            for (int i = 0; i < jArray.length(); i++) {
                jObject = jArray.getJSONObject(i);
                String id = jObject.getString("id");
                String jobPostId = jObject.getString("jobPostId");
                String roleName = jObject.getString("roleName");
                String rate = jObject.getString("rate");
                String duration = jObject.getString("duration");
                String date = jObject.getString("startDateTime");
                String employerName = jObject.getString("employerName");
                String employerAddress = jObject.getString("employerAddress");

                acceptedJobsItem = new AcceptedJobsItem(date, id + "@@" + jobPostId + "@@" + employerAddress, roleName,
                        "£" + rate + "/Hour", duration + " Hours", "7.6 miles", employerName, "");
                acceptedJobsItems.add(acceptedJobsItem);
            }
            acceptedJobsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
