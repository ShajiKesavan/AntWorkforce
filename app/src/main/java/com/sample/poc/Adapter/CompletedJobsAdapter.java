package com.sample.poc.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sample.poc.Activities.AntApplication;
import com.sample.poc.Activities.DashboardActivity;
import com.sample.poc.Activities.InterestReceviedListActivity;
import com.sample.poc.Activities.LoginActivity;
import com.sample.poc.Activities.NewJobPostingActivity;
import com.sample.poc.Items.CompletedJobItem;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;
import com.sample.poc.Utilities.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class CompletedJobsAdapter extends RecyclerView.Adapter<CompletedJobsAdapter.MyViewHolder> {

    private List<CompletedJobItem> completedJobItems;
    Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDateTimeValue, roleValue, rateValue,durationValue,txtNameValue;
        Spinner spinnerStatus;

        public MyViewHolder(View view) {
            super(view);
            txtDateTimeValue = (TextView) view.findViewById(R.id.txtDateTimeValue);
            roleValue = (TextView) view.findViewById(R.id.roleValue);
            rateValue = (TextView) view.findViewById(R.id.rateValue);
            durationValue = (TextView) view.findViewById(R.id.durationValue);
            txtNameValue = (TextView) view.findViewById(R.id.txtNameValue);
            spinnerStatus=(Spinner)view.findViewById(R.id.spinnerStatus);
            String[] data= {"Select","Reject","Approve"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,data);
            spinnerStatus .setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }


    public CompletedJobsAdapter(List<CompletedJobItem> completedJobItems,Activity context) {
        this.completedJobItems = completedJobItems;
        this.context=context;
    }

    @Override
    public CompletedJobsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_jobs_row, parent, false);

        return new CompletedJobsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CompletedJobsAdapter.MyViewHolder holder, int position) {
        final CompletedJobItem completedJobItem= completedJobItems.get(position);
        holder.txtDateTimeValue.setText(completedJobItem.getStartDate());
        holder.roleValue.setText(completedJobItem.getRole());
        holder.rateValue.setText(completedJobItem.getRate());
        holder.durationValue.setText(completedJobItem.getDuration());
        holder.txtNameValue.setText(completedJobItem.getName());

        holder.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0) {
                    String status= adapterView.getItemAtPosition(i).toString();
                    ShowFeedbackDialog(completedJobItem.getStartTime(),status);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    AlertDialog dialog;
    ProgressBar pgBar;
    public void ShowFeedbackDialog(final String id, final String status) {
        LayoutInflater inflater = context.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.feedback_dialog, null);
        final RatingBar mRating;
        final EditText mFeedback, mNotes;
        Button mSubmit, mCancel;

        mFeedback = (EditText)alertLayout.findViewById(R.id.editTextFd);
        mNotes = (EditText)alertLayout.findViewById(R.id.editTextNotes);
        mRating = (RatingBar)alertLayout.findViewById(R.id.ratingBar);
        pgBar = (ProgressBar)alertLayout.findViewById(R.id.progressBar);
        mSubmit = (Button)alertLayout.findViewById(R.id.button1);
        mCancel = (Button)alertLayout.findViewById(R.id.button2);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getResources().getString(R.string.enter_feedback));
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);
        dialog = alert.create();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v= null;
                String feedback = mFeedback.getText().toString(),
                        notes = mNotes.getText().toString();
                double rating = mRating.getRating();
                if(status.equals("Reject")) {
                    if (TextUtils.isEmpty(mNotes.getText().toString())) {
                        mNotes.setError(AntApplication._appContext.getString(R.string.error_field_required));
                        v = mNotes;
                        v.requestFocus();
                    } else {
                        pgBar.setVisibility(View.VISIBLE);
                        FeedbackRequest(id, "REJECT", rating, feedback, notes);
                        //dialog.dismiss();
                    }
                } else {
                    pgBar.setVisibility(View.VISIBLE);
                    FeedbackRequest(id, "APPROVE", rating, feedback, notes);
                    //dialog.dismiss();
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return completedJobItems.size();
    }

    public void FeedbackRequest(String id,String status,double rate,String feedback,String notes){

        try {
            // Simulate network access.
            //Thread.sleep(2000);
            JSONObject js = new JSONObject();
            try {
                js.put("loggedInUserId", Integer.valueOf(PreferenceHelper.getUserId_PREF(AntApplication._appContext)));
                js.put("status", status);
                js.put("rating", rate);
                js.put("feedback", feedback);
                js.put("notes", notes);
                System.out.println("json obj feedbk: "+js.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Make request for JSONObject
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.PUT, Constants.BASE_URL+Constants.FEEDBACK_URL+id, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("responseFrom feedback:" + response.toString());
                            Intent in = new Intent(context, DashboardActivity.class);
                            in.putExtra("dashbdJs",response.toString());
                            context.startActivity(in);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error instanceof TimeoutError || error instanceof NoConnectionError){
                        Toast.makeText(AntApplication._appContext,
                                "Network Timeout Error or no internet, Please try again.",
                                Toast.LENGTH_LONG).show();

                    }else if(error.toString().contains("AuthFailureError")) {
                        Toast.makeText(AntApplication._appContext,
                                "Token Expired, Please try again.",
                                Toast.LENGTH_LONG).show();
                        Intent in = new Intent(context, LoginActivity.class);
                        context.startActivity(in);
                    }
                    else {
                        Toast.makeText(AntApplication._appContext,
                                "Server Error, Please try again.",
                                Toast.LENGTH_LONG).show();
                    }
                    pgBar.setVisibility(View.GONE);
                    System.out.println("responseFromLogin err"
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
                    headers.put("X-Auth-Token", PreferenceHelper.getUserToken_PREF(AntApplication._appContext));
                    headers.put("X-Auth-User", String.valueOf(PreferenceHelper.getUserId_PREF(AntApplication._appContext)));
                    return headers;
                }

            };

            /** avoids data duplication bug if network is slow */
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            AntApplication.getInstance().addToRequestQueue(
                    jsonObjReq, "json_req");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AntApplication._appContext,
                    "Error, Please try again.",
                    Toast.LENGTH_LONG).show();
            pgBar.setVisibility(View.GONE);
        }

    }
}
