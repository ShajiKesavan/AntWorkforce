package com.sample.poc.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.poc.Activities.AntApplication;
import com.sample.poc.Activities.DashboardActivity;
import com.sample.poc.Adapter.CompletedJobsAdapter;
import com.sample.poc.Adapter.ListJobsAdapter;
import com.sample.poc.Adapter.PostedJobsAdapter;
import com.sample.poc.Items.ListJobsItem;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1013373 on 7/31/2018.
 */

public class AvailableShiftsFragment extends Fragment {

    private List<ListJobsItem> listJobsItems = new ArrayList<>();
    RecyclerView listJobs;
    public static ListJobsAdapter listJobsAdapter;
    TextView empty;
    public static AvailableShiftsFragment instance;
    public static boolean mRefreshNow = false;

    public AvailableShiftsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_list_jobs, container, false);
        listJobs = (RecyclerView) parentView.findViewById(R.id.listJobs);
        empty = (TextView) parentView.findViewById(R.id.empty);
        instance = this;
        listJobsItems = new ArrayList<>();

        listJobsAdapter = new ListJobsAdapter(listJobsItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listJobs.setLayoutManager(mLayoutManager);
        listJobs.setItemAnimator(new DefaultItemAnimator());
        listJobs.setAdapter(listJobsAdapter);
        //preparePostedJobData();
        getListJobs(DashboardActivity.postsJs);
        return parentView;
    }

    public static AvailableShiftsFragment getinstance() {
        return instance;
    }

    private void preparePostedJobData() {
        ListJobsItem listJobsItem = new ListJobsItem("28/07/2018", "08:00", "Registered Mental Health Nurse",
                "£20.50/Hour", "8 Hours", "0.2 miles", "BlueBell", "",20,50);
        listJobsItems.add(listJobsItem);

        listJobsItem = new ListJobsItem("24/08/2018", "08:30", "Plumber", "£30.75/Hour", "4 Hours", "2.7 miles", "Horizon", "",30,75);
        listJobsItems.add(listJobsItem);

        listJobsItem = new ListJobsItem("22/07/2018", "09:30", "Electrician", "£18.75/Hour", "3 Hours", "1.4 miles", "Amazon", "",18,75);
        listJobsItems.add(listJobsItem);

        listJobsItem = new ListJobsItem("29/07/2018", "12:30", "Driver", "£25.00/Hour", "8 Hours", "7.6 miles","Google","",25,00);
        listJobsItems.add(listJobsItem);

        listJobsAdapter.notifyDataSetChanged();
    }

    public void getListJobs(final String postResponse){
        try {
            System.out.println("responseFrompostjb2 :" + postResponse);
            listJobs.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            ListJobsItem listJobsItem;
            JSONArray jArray = new JSONArray(postResponse);
            JSONObject jObject = null;
            String employerName = "";
            if(jArray.length() > 0) {
                for (int i = 0; i < jArray.length(); i++) {
                    jObject = jArray.getJSONObject(i);

                    String id = jObject.getString("id");
                    String roleName = jObject.getString("roleName");
                    String rate = jObject.getString("rate");
                    String duration = jObject.getString("duration");
                    String date = jObject.getString("startDateTime");
                    String vacancies = jObject.getString("vacancies");
                    String distance = jObject.getString("distance");
                    if (jObject.toString().contains("employerName"))
                        employerName = jObject.getString("employerName");
                    String roleDescription = jObject.getString("roleDescription");
                    String employerAddress = jObject.getString("employerAddress");
                    listJobsItem = new ListJobsItem(date, id + "@@" + employerAddress + "@@" + vacancies, roleName,
                            "£" + rate + "/Hour", duration + " Hours", distance, employerName,
                            roleDescription, 0, 0);
                    listJobsItems.add(listJobsItem);
                }
                listJobsAdapter.notifyDataSetChanged();
            } else {
                listJobs.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listJobs.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }

    }

    public static void refreshAll(){
        try {
            getinstance().onResume();
            listJobsAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        if(mRefreshNow) {
            System.out.println("cmplt onResume:"+mRefreshNow);
            mRefreshNow = false;
            getListJobs(DashboardActivity.postsJs);
        }
        super.onResume();
    }
}
