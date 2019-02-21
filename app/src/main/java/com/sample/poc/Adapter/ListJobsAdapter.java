package com.sample.poc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.sample.poc.Activities.NewJobPostingActivity;
import com.sample.poc.Items.ListJobsItem;
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

public class ListJobsAdapter extends RecyclerView.Adapter<ListJobsAdapter.MyViewHolder> {

    private List<ListJobsItem> listJobsItems;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDateTimeValue, roleValue, rateValue,durationValue,employerValue,more,
                distanceValue,less,description;
        public Button btnAccept;
        LinearLayout readMore;
        EditText rate1,rate2;

        public MyViewHolder(View view) {
            super(view);
            txtDateTimeValue = (TextView) view.findViewById(R.id.txtDateTimeValue);
            roleValue = (TextView) view.findViewById(R.id.roleValue);
            rateValue = (TextView) view.findViewById(R.id.rateValue);
            durationValue = (TextView) view.findViewById(R.id.durationValue);
            employerValue = (TextView) view.findViewById(R.id.employerValue);
            more = (TextView) view.findViewById(R.id.more);
            distanceValue = (TextView) view.findViewById(R.id.distanceValue);
            btnAccept = (Button) view.findViewById(R.id.btnAccept);
            less = (TextView) view.findViewById(R.id.less);
            description = (TextView) view.findViewById(R.id.roleDesc);
            readMore = (LinearLayout) view.findViewById(R.id.readmore);
            rate1 = (EditText)view.findViewById(R.id.edtPreRateInt);
            rate2 = (EditText)view.findViewById(R.id.edtPreRateDec);
        }
    }


    public ListJobsAdapter(List<ListJobsItem> listJobsItems,Context context) {
        this.listJobsItems = listJobsItems;
        this.context=context;
    }

    @Override
    public ListJobsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_jobs_row, parent, false);

        return new ListJobsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListJobsAdapter.MyViewHolder holder, int position) {
        final ListJobsItem listJobsItem= listJobsItems.get(position);
        holder.txtDateTimeValue.setText(listJobsItem.getStartDate());
        holder.roleValue.setText(listJobsItem.getRole());
        holder.rateValue.setText(listJobsItem.getRate());
        holder.durationValue.setText(listJobsItem.getDuration());
        holder.employerValue.setText(listJobsItem.getEmployer()+" ,"+(listJobsItem.getStartTime()).split("@@")[1]);
        holder.distanceValue.setText(listJobsItem.getDistance());
        holder.description.setText(listJobsItem.getDescription());

        final String udata=context.getResources().getString(R.string.read_more);
        final SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        holder.more.setText(content);

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               holder.more.setVisibility(View.GONE);
               holder.readMore.setVisibility(View.VISIBLE);

            }
        });

        final String udata1=context.getResources().getString(R.string.read_less);
        final SpannableString content1 = new SpannableString(udata1);
        content1.setSpan(new UnderlineSpan(), 0, udata1.length(), 0);
        holder.less.setText(content1);

        holder.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.more.setVisibility(View.VISIBLE);
                holder.readMore.setVisibility(View.GONE);

            }
        });
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rateInt,rateDec;
                rateInt = holder.rate1.getText().toString();
                rateDec = holder.rate2.getText().toString();
                if(TextUtils.isEmpty(rateInt) || Integer.valueOf(rateInt)==0){
                    //holder.rate1.setError(context.getString(R.string.error_field_required));
                    Toast.makeText(context,"Please enter rate",Toast.LENGTH_LONG).show();
                    Animation shake = AnimationUtils.loadAnimation(context,
                            R.anim.animation_shake);
                    holder.rate1.startAnimation(shake);
                } else if(TextUtils.isEmpty(rateDec)){
                    //holder.rate2.setError(context.getString(R.string.error_field_required));
                    Toast.makeText(context,"Please enter rate",Toast.LENGTH_LONG).show();
                    Animation shake = AnimationUtils.loadAnimation(context,
                            R.anim.animation_shake);
                    holder.rate2.startAnimation(shake);
                } else {
                    InterestedRequest(DashboardActivity.resourceId,listJobsItem.getStartTime().split("@@")[0],
                            Double.valueOf(rateInt+"."+rateDec));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return listJobsItems.size();
    }


    public void InterestedRequest(String resourceId,String jobId,double rate){

        try {
            // Simulate network access.
            //Thread.sleep(2000);
            JSONObject js = new JSONObject();
            try {
                js.put("jobPostId", jobId);
                js.put("resourceId", resourceId);
                js.put("rate", rate);
                js.put("loggedInUserId", Integer.valueOf(PreferenceHelper.getUserId_PREF(AntApplication._appContext)));
                System.out.println("json obj feedbk: "+js.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Make request for JSONObject
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, Constants.BASE_URL+Constants.FEEDBACK_URL, js,
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
