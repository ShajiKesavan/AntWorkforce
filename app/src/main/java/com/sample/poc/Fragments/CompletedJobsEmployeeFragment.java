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

import com.sample.poc.Activities.DashboardActivity;
import com.sample.poc.Adapter.AcceptedJobsAdapter;
import com.sample.poc.Adapter.CompletedJobsAdapter;
import com.sample.poc.Adapter.CompletedJobsEmployeeAdapter;
import com.sample.poc.Adapter.PostedJobsAdapter;
import com.sample.poc.Items.AcceptedJobsItem;
import com.sample.poc.Items.CompletedEmployeeJobItem;
import com.sample.poc.Items.CompletedJobItem;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class CompletedJobsEmployeeFragment extends Fragment {


    private List<CompletedJobItem> completedEmployeeJobItems = new ArrayList<>();
    private List<PostedJobItems> postedEmployeeJobItems = new ArrayList<>();

    RecyclerView listCompletedJobs;
    RecyclerView listpostedJobs;
    CompletedJobsAdapter completedJobsEmployeeAdapter;
    PostedJobsAdapter postedJobsEmployeeAdapter;
    LinearLayout emptyCompleted,emptyPosted;

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
        listpostedJobs = (RecyclerView) parentView.findViewById(R.id.listpostedJobs);
        emptyCompleted = (LinearLayout) parentView.findViewById(R.id.empty_completed);
        emptyPosted = (LinearLayout) parentView.findViewById(R.id.empty_posted);
        completedEmployeeJobItems = new ArrayList<>();
        postedEmployeeJobItems = new ArrayList<>();

        completedJobsEmployeeAdapter = new CompletedJobsAdapter(completedEmployeeJobItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listCompletedJobs.setLayoutManager(mLayoutManager);
        listCompletedJobs.setItemAnimator(new DefaultItemAnimator());
        listCompletedJobs.setAdapter(completedJobsEmployeeAdapter);

        postedJobsEmployeeAdapter = new PostedJobsAdapter(postedEmployeeJobItems, getActivity());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        listpostedJobs.setLayoutManager(mLayoutManager1);
        listpostedJobs.setItemAnimator(new DefaultItemAnimator());
        listpostedJobs.setAdapter(postedJobsEmployeeAdapter);

        //preparePostedJobData();
        getCompletedJobs(DashboardActivity.shiftsJs);
        getPostedJobs(DashboardActivity.postsJs);
        return parentView;
    }


    public void getPostedJobs(final String postResponse){
        try {
            emptyPosted.setVisibility(View.GONE);
            listpostedJobs.setVisibility(View.VISIBLE);
            System.out.println("responseFrompostjb2 :" + postResponse);
            PostedJobItems postedJobItem;
            JSONArray jArray = new JSONArray(postResponse);
            JSONObject jObject = null;
            if(jArray.length()>0) {
                for (int i = 0; i < jArray.length(); i++) {
                    jObject = jArray.getJSONObject(i);
                    String interests = "0", accepted = "0", confirmed = "0";
                    String id = jObject.getString("id");
                    String roleName = jObject.getString("roleName");
                    String rate = jObject.getString("rate");
                    String duration = jObject.getString("duration");
                    String date = jObject.getString("startDateTime");
                    String vacancies = jObject.getString("vacancies");
                    if (jObject.toString().contains("interests"))
                        interests = jObject.getString("interests");
                    if (jObject.toString().contains("accepted"))
                        accepted = jObject.getString("accepted");
                    if (jObject.toString().contains("confirmed"))
                        confirmed = jObject.getString("confirmed");
                    postedJobItem = new PostedJobItems(date, id, roleName, "£" + rate + "/Hour",
                            duration + " Hours", Integer.valueOf(vacancies), Integer.valueOf(accepted),
                            Integer.valueOf(confirmed), Integer.valueOf(interests));
                    postedEmployeeJobItems.add(postedJobItem);
                }
                postedJobsEmployeeAdapter.notifyDataSetChanged();
            } else {
                emptyPosted.setVisibility(View.VISIBLE);
                listpostedJobs.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            emptyPosted.setVisibility(View.VISIBLE);
            listpostedJobs.setVisibility(View.GONE);
        }

    }

    public void getCompletedJobs(final String postResponse){

        try {
            emptyCompleted.setVisibility(View.GONE);
            listCompletedJobs.setVisibility(View.VISIBLE);
            System.out.println("responseFromcmpltd2 :" + postResponse.toString());
            CompletedJobItem completedJobItem;
            JSONArray jArray = new JSONArray(postResponse);
            JSONObject jObject = null;
            if(jArray.length()>0) {

                for (int i = 0; i < jArray.length(); i++) {
                    jObject = jArray.getJSONObject(i);
                    String id = jObject.getString("id");
                    String roleName = jObject.getString("roleName");
                    String rate = jObject.getString("rate");
                    String duration = jObject.getString("duration");
                    String date = jObject.getString("startDateTime");
                    String resource = jObject.getString("resource");
                    JSONObject jObj = new JSONObject(resource);
                    String name = jObj.getString("name");
                    String shiftStatus = jObject.getString("shiftStatus");
                    completedJobItem = new CompletedJobItem(date, id, roleName, "£" + rate + "/Hour",
                            duration + " Hours", name, shiftStatus);
                    completedEmployeeJobItems.add(completedJobItem);
                }
                completedJobsEmployeeAdapter.notifyDataSetChanged();
            } else {
                emptyCompleted.setVisibility(View.VISIBLE);
                listCompletedJobs.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            emptyCompleted.setVisibility(View.VISIBLE);
            listCompletedJobs.setVisibility(View.GONE);
        }

    }

}
