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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.sample.poc.Activities.FeedbackActivity;
import com.sample.poc.Activities.LoginActivity;
import com.sample.poc.Activities.NewJobPostingActivity;
import com.sample.poc.Items.InterestListItem;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;
import com.sample.poc.Utilities.PreferenceHelper;
import com.sample.poc.Utilities.SpannableTextViewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sample.poc.Utilities.Constants.FEEDBACK_LIST_COUNT;

/**
 * Created by 1013373 on 7/31/2018.
 */

public class InterestedListAdapter extends RecyclerView.Adapter<InterestedListAdapter.MyViewHolder> {

    private List<InterestListItem> interestListItems;
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameValue, rateValue, regnoValue,experienceValue,readmore,readless,morefeedback,feedback;
        public ImageView profile_user_img;
        public RatingBar ratingBar;
        public Button btnAccept;
        public LinearLayout lnrContentMore,lnrFeedbackList;

        public MyViewHolder(View view) {
            super(view);
            txtNameValue = view.findViewById(R.id.txtNameValue);
            rateValue = (TextView) view.findViewById(R.id.rateValue);
            regnoValue = (TextView) view.findViewById(R.id.regnoValue);
            experienceValue = (TextView) view.findViewById(R.id.experienceValue);
            readmore = (TextView) view.findViewById(R.id.readmore);
            profile_user_img = (ImageView) view.findViewById(R.id.profile_user_img);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            btnAccept = (Button) view.findViewById(R.id.btnAccept);
            lnrContentMore = view.findViewById(R.id.lnrContentMore);
            lnrFeedbackList = view.findViewById(R.id.lnrFeedbackList);
            readless = view.findViewById(R.id.readless);
            morefeedback = view.findViewById(R.id.morefeedback);
            feedback = view.findViewById(R.id.feedback);

        }
    }


    public InterestedListAdapter(List<InterestListItem> interestListItems,Context context) {
        this.interestListItems = interestListItems;
        this.context=context;
    }

    @Override
    public InterestedListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.interest_list_row, parent, false);

        return new InterestedListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InterestedListAdapter.MyViewHolder holder, int position) {
        final InterestListItem interestListItem = interestListItems.get(position);
        holder.txtNameValue.setText(interestListItem.getName());
        holder.lnrContentMore.setVisibility(View.GONE);
        holder.rateValue.setText(interestListItem.getAcceptableRate());
        holder.regnoValue.setText(interestListItem.getRegNo());
        holder.experienceValue.setText(interestListItem.getExperience());
        holder.ratingBar.setRating(interestListItem.getRating());
        holder.profile_user_img.setImageResource(interestListItem.getImageResource());
        int cnt=interestListItem.getFeedbackListItems().size();
        holder.feedback.setText(context.getResources().getString(R.string.feedback)+"("+cnt+")");

        holder.lnrFeedbackList.removeAllViews();
        int totalCnt=FEEDBACK_LIST_COUNT;
        if(FEEDBACK_LIST_COUNT>interestListItem.getFeedbackListItems().size())
            totalCnt=interestListItem.getFeedbackListItems().size();

        for(int i=0;i<totalCnt;i++)
        {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.feedback_row, holder.lnrFeedbackList, false);

            TextView feedback_name=itemView.findViewById(R.id.feedback_name);
            ImageView img_pic=itemView.findViewById(R.id.img_pic);
            RatingBar ratingBar=(RatingBar)itemView.findViewById(R.id.ratingBar);
            TextView feedback_value=itemView.findViewById(R.id.feedback_value);

            feedback_name.setText(interestListItem.getFeedbackListItems().get(i).getUserName());
            feedback_value.setText(interestListItem.getFeedbackListItems().get(i).getFeedbackData());
            img_pic.setImageResource(interestListItem.getFeedbackListItems().get(i).getImageResource());
            ratingBar.setRating(interestListItem.getFeedbackListItems().get(i).getRating());

            holder.lnrFeedbackList.addView(itemView);
        }

        SpannableTextViewer.displaySpannableText(context.getResources().getString(R.string.read_more),holder.readmore);
        SpannableTextViewer.displaySpannableText(context.getResources().getString(R.string.read_less),holder.readless);
        SpannableTextViewer.displaySpannableText(context.getResources().getString(R.string.more_feedback),holder.morefeedback);

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AcceptRequest(interestListItem.getRegNo(),holder);

            }
        });

        holder.readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.lnrContentMore.setVisibility(View.VISIBLE);
                holder.readmore.setVisibility(View.GONE);
            }
        });


        holder.readless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.lnrContentMore.setVisibility(View.GONE);
                holder.readmore.setVisibility(View.VISIBLE);
            }
        });


        holder.morefeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // holder.lnrContentMore.setVisibility(View.VISIBLE);
                Intent intent=new Intent(context, FeedbackActivity.class);
                context.startActivity(intent);
            }
        });


        //holder.interest_received.setText("#INTEREST RECEIVED("+postedJobItem.getConfirmedCount()+")");
    }

    @Override
    public int getItemCount() {
        return interestListItems.size();
    }

    public void AcceptRequest(String id, final InterestedListAdapter.MyViewHolder holder){

        try {
            JSONObject js = new JSONObject();
            try {
                js.put("loggedInUserId", Integer.valueOf(PreferenceHelper.getUserId_PREF(AntApplication._appContext)));
                js.put("status", "ACCEPTED");
                System.out.println("json obj: "+js.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Make request for JSONObject
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.PUT, Constants.BASE_URL+Constants.ACCEPTED_URL+id, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("responseFrompostjobnewwwww:" + response.toString());
                            holder.btnAccept.setVisibility(View.GONE);
                            Toast.makeText(context,context.getResources().getString(R.string.successfully_accepted),Toast.LENGTH_SHORT).show();
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