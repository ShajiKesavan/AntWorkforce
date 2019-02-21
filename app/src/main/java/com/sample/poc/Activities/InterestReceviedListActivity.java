package com.sample.poc.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sample.poc.Adapter.InterestedListAdapter;
import com.sample.poc.Adapter.PostedJobsAdapter;
import com.sample.poc.Items.FeedbackListItem;
import com.sample.poc.Items.InterestListItem;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;
import com.sample.poc.Utilities.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InterestReceviedListActivity extends Activity {

    RecyclerView listInterestReceived;
    InterestedListAdapter interestedListAdapter;
    ArrayList<InterestListItem> interestListItems= new ArrayList<>();
    TextView title,role,rate,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_recevied_list);
        interestListItems= new ArrayList<>();
        title = (TextView)findViewById(R.id.title);
        role = (TextView)findViewById(R.id.roleValue);
        rate = (TextView)findViewById(R.id.rateValue);
        date = (TextView)findViewById(R.id.txtDateTimeValue);
        Intent in = getIntent();
        title.setText(in.getStringExtra("title"));
        role.setText(in.getStringExtra("role"));
        rate.setText(in.getStringExtra("rate"));
        date.setText(in.getStringExtra("date"));
        String jobId = in.getStringExtra("id");

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.list_of_interest));
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


        listInterestReceived=(RecyclerView)findViewById(R.id.listInterestReceived);

        interestedListAdapter = new InterestedListAdapter(interestListItems,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listInterestReceived.setLayoutManager(mLayoutManager);
        listInterestReceived.setItemAnimator(new DefaultItemAnimator());
        listInterestReceived.setAdapter(interestedListAdapter);
        String choice = title.getText().toString();
        if(choice.contains("Interest"))
            getInterestResponse(Constants.BASE_URL+Constants.INTEREST_URL,Integer.valueOf(jobId),
                Integer.valueOf(PreferenceHelper.getUserId_PREF(getApplicationContext())),
                PreferenceHelper.getUserToken_PREF(getApplicationContext()),"Interested");
        else if(choice.contains("Accepted"))
            getInterestResponse(Constants.BASE_URL+Constants.ACCEPTED_LIST_URL,Integer.valueOf(jobId),
                    Integer.valueOf(PreferenceHelper.getUserId_PREF(getApplicationContext())),
                    PreferenceHelper.getUserToken_PREF(getApplicationContext()),"Accepted");
        else
            getInterestResponse(Constants.BASE_URL+Constants.CONFIRMED_LIST_URL,Integer.valueOf(jobId),
                    Integer.valueOf(PreferenceHelper.getUserId_PREF(getApplicationContext())),
                    PreferenceHelper.getUserToken_PREF(getApplicationContext()),"Confirmed");
       // preparePostedJobData();
    }




   /* private void preparePostedJobData() {

        ArrayList<FeedbackListItem> feedbackListItems=new ArrayList<>();
        FeedbackListItem feedbackListItem=new FeedbackListItem("","Stephen Flemming",4,"Excellent Communication skills",R.drawable.unknown);
        feedbackListItems.add(feedbackListItem);
        feedbackListItem=new FeedbackListItem("","Jose Ricardo",1,"Poor performance",R.drawable.unknown);
        feedbackListItems.add(feedbackListItem);
        InterestListItem interestListItem=new InterestListItem("Thomas Mathew","£20.00/Hour","A123S23","5yr","",1,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Soniya Jacob","£20.00/Hour","A123S23","5yr","",3,R.drawable.female_1,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Paula Warker","£14.00/Hour","S123f45","1yr","",4,R.drawable.female_2,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Johny Dep","£20.00/Hour","A123S23","5yr","",2,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Dwayne Johnson","£20.00/Hour","A123S23","5yr","",5,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Adam Sandler","£20.00/Hour","A123S23","5yr","",4,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Christian Bale","£20.00/Hour","A123S23","5yr","",3,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Tom Hanks","£20.00/Hour","A123S23","5yr","",1,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Tom Hardy","£20.00/Hour","A123S23","5yr","",2,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("George Clooney","£20.00/Hour","A123S23","5yr","",3,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Hugh Jackman","£20.00/Hour","A123S23","5yr","",2,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Chris Pratt","£20.00/Hour","A123S23","5yr","",5,R.drawable.male,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Edward Norton","£20.00/Hour","A123S23","5yr","",5,R.drawable.female_2,feedbackListItems);
        interestListItems.add(interestListItem);
        interestListItem=new InterestListItem("Sandra Bullock","£20.00/Hour","A123S23","5yr","",5,R.drawable.female_1,feedbackListItems);
        interestListItems.add(interestListItem);


        interestedListAdapter.notifyDataSetChanged();
    }*/


    public void getInterestResponse(String Url,final int jobId, final int userId, final String token, final String status){

        try {
            // Make request for JSONObject
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET, Url+jobId, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("responseFromLogin2 interested response :" + response.toString());
                            try {
                                ArrayList<FeedbackListItem> feedbackListItems=new ArrayList<>();
                                FeedbackListItem feedbackListItem;
                                InterestListItem interestListItem;

                                String shifts = response.getString("shifts");
                                JSONArray jArray = new JSONArray(shifts);
                                JSONObject jObject = null;
                                for (int i = 0; i < jArray.length(); i++) {
                                    jObject = jArray.getJSONObject(i);
                                    String resource = jObject.getString("resource");
                                    JSONObject jObj = new JSONObject(resource);
                                    String name = jObj.getString("name");
                                    String preferredRate = jObj.getString("preferredRate");
                                    String id = jObj.getString("id");
                                    String experience = jObj.getString("experience");
                                    String imageUrl = jObj.getString("imageUrl");
                                    String averageRating = jObj.getString("averageRating");

                                    String feedback = jObj.getString("feedback");
                                    JSONArray jArray2Fb = new JSONArray(feedback);
                                    JSONObject jObjFb = null;
                                    for (int j = 0; j < jArray2Fb.length(); j++) {
                                        jObjFb = jArray2Fb.getJSONObject(j);
                                        String rating = jObjFb.getString("rating");
                                        String feedbackComment = jObjFb.getString("feedback");
                                        feedbackListItem = new FeedbackListItem("", "",
                                                Integer.valueOf(rating), feedbackComment, R.drawable.unknown);
                                        feedbackListItems.add(feedbackListItem);
                                    }
                                    interestListItem = new InterestListItem(name, "£"+preferredRate+"/Hour",
                                            id, experience, imageUrl, Integer.valueOf(averageRating), R.drawable.female_1,status,
                                            feedbackListItems);
                                    interestListItems.add(interestListItem);
                                }
                                interestedListAdapter.notifyDataSetChanged();
                                System.out.println("responseFromLoginPost id:" + userId);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(AntApplication._appContext,
                                        "Error, Please try again.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error instanceof TimeoutError || error instanceof NoConnectionError){
                        Toast.makeText(AntApplication._appContext,
                                "Network Timeout Error or No internet, Please try again.",
                                Toast.LENGTH_LONG).show();

                    }  else if(error.getMessage().contains("AuthFailureError")) {
                        Toast.makeText(AntApplication._appContext,
                                "Token Expired, Please try again.",
                                Toast.LENGTH_LONG).show();
                        Intent in = new Intent(InterestReceviedListActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                    }
                    else {
                        Toast.makeText(AntApplication._appContext,
                                "Error, Please try again.",
                                Toast.LENGTH_LONG).show();

                    }
                    System.out.println("responseFromLogin2 err"
                            + error);
                    error.printStackTrace();
                }
            }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Auth-Token", token);
                    headers.put("X-Auth-User", String.valueOf(userId));
                    return headers;
                }

            };

            // Adding request to request queue
            AntApplication.getInstance().addToRequestQueue(
                    jsonObjReq, "json_req");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AntApplication._appContext,
                    "Error, Please try again.",
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
        if(!InterestedListAdapter.response.equals("0"))
        in.putExtra("dashbdJs",InterestedListAdapter.response.toString());
        startActivity(in);
        //super.onBackPressed();
    }
}
