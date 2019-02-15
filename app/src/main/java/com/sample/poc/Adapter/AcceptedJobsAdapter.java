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
import android.widget.TextView;

import com.sample.poc.Activities.ResourceReadMore;
import com.sample.poc.Items.AcceptedJobsItem;
import com.sample.poc.Items.ListJobsItem;
import com.sample.poc.R;

import java.util.List;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class AcceptedJobsAdapter extends RecyclerView.Adapter<AcceptedJobsAdapter.MyViewHolder> {

    private List<AcceptedJobsItem> acceptedJobsItems;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDateTimeValue, roleValue, rateValue,durationValue,employerValue,more,distanceValue;
        public Button btnConfirm;

        public MyViewHolder(View view) {
            super(view);
            txtDateTimeValue = (TextView) view.findViewById(R.id.txtDateTimeValue);
            roleValue = (TextView) view.findViewById(R.id.roleValue);
            rateValue = (TextView) view.findViewById(R.id.rateValue);
            durationValue = (TextView) view.findViewById(R.id.durationValue);
            employerValue = (TextView) view.findViewById(R.id.employerValue);
            more = (TextView) view.findViewById(R.id.more);
            distanceValue = (TextView) view.findViewById(R.id.distanceValue);
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
    public void onBindViewHolder(AcceptedJobsAdapter.MyViewHolder holder, int position) {
        final AcceptedJobsItem acceptedJobsItem= acceptedJobsItems.get(position);
        holder.txtDateTimeValue.setText(acceptedJobsItem.getStartDate());
        holder.roleValue.setText(acceptedJobsItem.getRole());
        holder.rateValue.setText(acceptedJobsItem.getRate());
        holder.durationValue.setText(acceptedJobsItem.getDuration());
        holder.employerValue.setText(acceptedJobsItem.getEmployer());
        holder.distanceValue.setText(acceptedJobsItem.getDistance());

        String udata=context.getResources().getString(R.string.read_more);
        final SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        holder.more.setText(content);

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, ResourceReadMore.class);
                intent.putExtra("duration", acceptedJobsItem.getDuration());
                intent.putExtra("desc", acceptedJobsItem.getDescription());
                intent.putExtra("title", acceptedJobsItem.getRole());
                intent.putExtra("rate", acceptedJobsItem.getRate());
                intent.putExtra("date", acceptedJobsItem.getStartDate());
                intent.putExtra("distance", acceptedJobsItem.getDistance());
                intent.putExtra("employer", acceptedJobsItem.getEmployer());
                String data[] = (acceptedJobsItem.getStartTime()).split("@@");
                String vacancies = "0";
                String address = data[2];
                intent.putExtra("vacancies", vacancies);
                intent.putExtra("address", address);
                context.startActivity(intent);

            }
        });
        //holder.interest_received.setText("#INTEREST RECEIVED("+postedJobItem.getConfirmedCount()+")");
    }

    @Override
    public int getItemCount() {
        return acceptedJobsItems.size();
    }
}

