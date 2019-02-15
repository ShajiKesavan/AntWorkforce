package com.sample.poc.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sample.poc.Activities.AntApplication;
import com.sample.poc.Activities.DashboardActivity;
import com.sample.poc.Adapter.PostedJobsAdapter;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;
import com.sample.poc.Utilities.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1013373 on 7/31/2018.
 */

public class PostedJobsFragment extends Fragment {

    private List<PostedJobItems> postedJobItems = new ArrayList<>();
    RecyclerView listPostedJobs;
    PostedJobsAdapter postedJobsAdapter;

    public PostedJobsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View parentView=inflater.inflate(R.layout.fragment_posted_jobs, container, false);
        postedJobItems = new ArrayList<>();

        listPostedJobs = (RecyclerView) parentView.findViewById(R.id.listPostedJobs);

        postedJobsAdapter = new PostedJobsAdapter(postedJobItems,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        listPostedJobs.setLayoutManager(mLayoutManager);
        listPostedJobs.setItemAnimator(new DefaultItemAnimator());
        listPostedJobs.setAdapter(postedJobsAdapter);
        //preparePostedJobData();
        getPostedJobs(DashboardActivity.postsJs);
        return parentView;
    }


    private void preparePostedJobData() {
        PostedJobItems postedJobItem = new PostedJobItems("28/07/2018","08:00",
                "Registered Mental Health Nurse","£20.50/Hour","8 Hours",4,2,
                1,10);
        postedJobItems.add(postedJobItem);

        postedJobItem = new PostedJobItems("24/08/2018","08:30","Plumber","£30.75/Hour","4 Hours",3,1,2,4);
        postedJobItems.add(postedJobItem);

        postedJobItem = new PostedJobItems("22/07/2018","09:30","Electrician","£18.75/Hour","3 Hours",2,1,2,4);
        postedJobItems.add(postedJobItem);

        postedJobItem = new PostedJobItems("29/07/2018","12:30","Driver","£25.00/Hour","8 Hours",4,2,1,10);
        postedJobItems.add(postedJobItem);

        postedJobsAdapter.notifyDataSetChanged();
    }

            public void getPostedJobs(final String postResponse){
                            try {
                                System.out.println("responseFrompostjb2 :" + postResponse);
                                PostedJobItems postedJobItem;
                                JSONArray jArray = new JSONArray(postResponse);
                                JSONObject jObject = null;
                                for (int i = 0; i < jArray.length(); i++) {
                                    jObject = jArray.getJSONObject(i);
                                    String interests="0",accepted="0";

                                    String id = jObject.getString("id");
                                    String roleName = jObject.getString("roleName");
                                    String rate = jObject.getString("rate");
                                    String duration = jObject.getString("duration");
                                    String date = jObject.getString("startDateTime");
                                    String vacancies = jObject.getString("vacancies");
                                    if(jObject.toString().contains("interests"))
                                    interests = jObject.getString("interests");
                                    if(jObject.toString().contains("accepted"))
                                    accepted = jObject.getString("accepted");
                                    postedJobItem = new PostedJobItems(date,id, roleName,"£"+rate+"/Hour",
                                            duration+" Hours",Integer.valueOf(vacancies),Integer.valueOf(accepted),
                                            1,Integer.valueOf(interests));
                                    postedJobItems.add(postedJobItem);
                                }
                                postedJobsAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

    }
}