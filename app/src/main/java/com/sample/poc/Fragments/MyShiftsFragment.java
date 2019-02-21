package com.sample.poc.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class MyShiftsFragment extends Fragment {


    private List<AcceptedJobsItem> acceptedJobsItems = new ArrayList<>();
    private List<AcceptedJobsItem> interestedItems = new ArrayList<>();
    private List<AcceptedJobsItem> confirmedItems = new ArrayList<>();

    RecyclerView listAcceptedJobs;
    public static AcceptedJobsAdapter acceptedJobsAdapter;
    TextView empty;
    public static MyShiftsFragment instance;
    public static boolean mRefreshNow = false;
    public MyShiftsFragment() {
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
        empty = (TextView) parentView.findViewById(R.id.empty);
        instance = this;
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

    public static MyShiftsFragment getinstance() {
        return instance;
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
            listAcceptedJobs.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            AcceptedJobsItem acceptedJobsItem;
            JSONArray jArray = new JSONArray(postResponse);
            JSONObject jObject = null;
            String employerName = "";
            acceptedJobsItems.clear();
            confirmedItems.clear();
            interestedItems.clear();
            if (jArray.length() > 0){
                for (int i = 0; i < jArray.length(); i++) {
                    jObject = jArray.getJSONObject(i);
                    String id = jObject.getString("id");
                    String jobPostId = jObject.getString("jobPostId");
                    String roleName = jObject.getString("roleName");
                    String rate = jObject.getString("rate");
                    String duration = jObject.getString("duration");
                    String date = jObject.getString("startDateTime");
                    if (jObject.toString().contains("employerName"))
                        employerName = jObject.getString("employerName");
                    String employerAddress = jObject.getString("employerAddress");
                    String status = jObject.getString("shiftStatus");

                    acceptedJobsItem = new AcceptedJobsItem(date, id + "@@" + jobPostId, roleName,
                            "£" + rate + "/Hour", duration + " Hours", "7.6 miles",
                            employerName + "," + employerAddress, status);
                    if(status.equals("ACCEPTED"))
                        acceptedJobsItems.add(acceptedJobsItem);
                    else if(status.equals("CONFIRMED"))
                        confirmedItems.add(acceptedJobsItem);
                    else
                        interestedItems.add(acceptedJobsItem);
                }
                acceptedJobsItems.addAll(confirmedItems);
                acceptedJobsItems.addAll(interestedItems);
            acceptedJobsAdapter.notifyDataSetChanged();
        } else {
                listAcceptedJobs.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listAcceptedJobs.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        if(mRefreshNow) {
            System.out.println("cmplt onResume:"+mRefreshNow);
            mRefreshNow = false;
            getAcceptedJobs(DashboardActivity.shiftsJs);
        }
        super.onResume();
    }

    public static void refreshAll(){
        try {
            getinstance().onResume();
            acceptedJobsAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
