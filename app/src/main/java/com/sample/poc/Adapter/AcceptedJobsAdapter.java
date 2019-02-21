package com.sample.poc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.sample.poc.Activities.LoginActivity;
import com.sample.poc.Fragments.MyShiftsFragment;
import com.sample.poc.Items.AcceptedJobsItem;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;
import com.sample.poc.Utilities.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class AcceptedJobsAdapter extends RecyclerView.Adapter<AcceptedJobsAdapter.MyViewHolder> {

    private List<AcceptedJobsItem> acceptedJobsItems;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDateTimeValue, roleValue, rateValue,durationValue,employerValue,more,less,status;
        public LinearLayout readMore,card,moreLn;
        public Button btnConfirm;

        public MyViewHolder(View view) {
            super(view);
            txtDateTimeValue = (TextView) view.findViewById(R.id.txtDateTimeValue);
            roleValue = (TextView) view.findViewById(R.id.roleValue);
            rateValue = (TextView) view.findViewById(R.id.rateValue);
            durationValue = (TextView) view.findViewById(R.id.durationValue);
            employerValue = (TextView) view.findViewById(R.id.employerValue);
            more = (TextView) view.findViewById(R.id.more);
            less = (TextView) view.findViewById(R.id.less);
            status = (TextView) view.findViewById(R.id.statustxt);
            readMore = (LinearLayout) view.findViewById(R.id.ln_more);
            moreLn = (LinearLayout) view.findViewById(R.id.more_ln);
            card = (LinearLayout) view.findViewById(R.id.ln_card);
            btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        }
    }


    public AcceptedJobsAdapter(List<AcceptedJobsItem> acceptedJobsItems,Context context) {
        this.acceptedJobsItems = acceptedJobsItems;
        this.context=context;
    }

    @Override
    public AcceptedJobsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_accepted_row, parent, false);

        return new AcceptedJobsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AcceptedJobsAdapter.MyViewHolder holder, int position) {
        final AcceptedJobsItem acceptedJobsItem= acceptedJobsItems.get(position);
        holder.txtDateTimeValue.setText(acceptedJobsItem.getStartDate());
        holder.roleValue.setText(acceptedJobsItem.getRole());
        holder.rateValue.setText(acceptedJobsItem.getRate());
        holder.durationValue.setText(acceptedJobsItem.getDuration());
        holder.employerValue.setText(acceptedJobsItem.getEmployer());
        holder.status.setText(acceptedJobsItem.getDescription());
        if(acceptedJobsItem.getDescription().equals("ACCEPTED") || acceptedJobsItem.getDescription().equals("Accepted")) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.accepted));
            holder.btnConfirm.setVisibility(View.VISIBLE);
        }
        else if(acceptedJobsItem.getDescription().equals("CONFIRMED") || acceptedJobsItem.getDescription().equals("Confirmed")) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.confirmed));
            holder.btnConfirm.setVisibility(View.GONE);
        }
        else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.interested));
            holder.btnConfirm.setVisibility(View.GONE);
        }

        String udata=context.getResources().getString(R.string.read_more);
        final SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        holder.more.setText(content);

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        holder.readMore.setVisibility(View.VISIBLE);
        holder.moreLn.setVisibility(View.GONE);

            }
        });

        String udata1=context.getResources().getString(R.string.read_less);
        final SpannableString content1 = new SpannableString(udata1);
        content1.setSpan(new UnderlineSpan(), 0, udata1.length(), 0);
        holder.less.setText(content1);

        holder.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.readMore.setVisibility(View.GONE);
                holder.moreLn.setVisibility(View.VISIBLE);

            }
        });

        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmedRequest(acceptedJobsItem.getStartTime().split("@@")[0]);
            }
        });

    }

    @Override
    public int getItemCount() {
        return acceptedJobsItems.size();
    }

    public void ConfirmedRequest(String id){

        try {
            // Simulate network access.
            //Thread.sleep(2000);
            JSONObject js = new JSONObject();
            try {
                js.put("loggedInUserId", Integer.valueOf(PreferenceHelper.getUserId_PREF(AntApplication._appContext)));
                js.put("status", "CONFIRMED");
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
                            System.out.println("responseFrom Accepted jobss:" + response.toString());
                            /*Intent in = new Intent(context, DashboardActivity.class);
                            in.putExtra("dashbdJs",response.toString());
                            context.startActivity(in);*/
                            try {
                                JSONObject jObject = null;
                                try {
                                    String res = response.getString("resource");
                                    jObject = new JSONObject(res);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    DashboardActivity.shiftsJs = jObject.getString("shifts");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                MyShiftsFragment.mRefreshNow = true;
                                MyShiftsFragment.refreshAll();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
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
        }

    }
}

