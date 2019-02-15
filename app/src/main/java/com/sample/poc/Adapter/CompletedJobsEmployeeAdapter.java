package com.sample.poc.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sample.poc.Items.AcceptedJobsItem;
import com.sample.poc.Items.CompletedEmployeeJobItem;
import com.sample.poc.Items.CompletedJobItem;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;
import com.sample.poc.Utilities.SpannableTextViewer;

import java.util.List;

/**
 * Created by 1013373 on 8/8/2018.
 */

public class CompletedJobsEmployeeAdapter extends RecyclerView.Adapter<CompletedJobsEmployeeAdapter.MyViewHolder> {

    private List<CompletedEmployeeJobItem> completedEmployeeJobItems;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDateTimeValue, roleValue, rateValue,durationValue,employerValue,more,distanceValue,timesheet_status_value;
        public Button btnSubmitTimesheet;

        public MyViewHolder(View view) {
            super(view);
            txtDateTimeValue = (TextView) view.findViewById(R.id.txtDateTimeValue);
            roleValue = (TextView) view.findViewById(R.id.roleValue);
            rateValue = (TextView) view.findViewById(R.id.rateValue);
            durationValue = (TextView) view.findViewById(R.id.durationValue);
            employerValue = (TextView) view.findViewById(R.id.employerValue);
            more = (TextView) view.findViewById(R.id.more);
            distanceValue = (TextView) view.findViewById(R.id.distanceValue);
            timesheet_status_value = (TextView) view.findViewById(R.id.timesheet_status_value);
            btnSubmitTimesheet = (Button) view.findViewById(R.id.btnSubmitTimesheet);
        }
    }


    public CompletedJobsEmployeeAdapter(List<CompletedEmployeeJobItem> completedEmployeeJobItems,Context context) {
        this.completedEmployeeJobItems = completedEmployeeJobItems;
        this.context=context;
    }

    @Override
    public CompletedJobsEmployeeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_completed_employee_row, parent, false);

        return new CompletedJobsEmployeeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CompletedJobsEmployeeAdapter.MyViewHolder holder, int position) {
        CompletedEmployeeJobItem completedEmployeeJobItem= completedEmployeeJobItems.get(position);
        holder.txtDateTimeValue.setText(completedEmployeeJobItem.getStartDate()+" "+completedEmployeeJobItem.getStartTime());
        holder.roleValue.setText(completedEmployeeJobItem.getRole());
        holder.rateValue.setText(completedEmployeeJobItem.getRate());
        holder.durationValue.setText(completedEmployeeJobItem.getDuration());
        holder.employerValue.setText(completedEmployeeJobItem.getEmployer());
        holder.distanceValue.setText(completedEmployeeJobItem.getDistance());

        if(completedEmployeeJobItem.getApprovalStatus().equals(Constants.TIMESHEET_STATUS_APPROVED))
        {
            holder.timesheet_status_value.setVisibility(View.VISIBLE);
            holder.btnSubmitTimesheet.setVisibility(View.GONE);
        }
        else
        {
            holder.timesheet_status_value.setVisibility(View.GONE);
            holder.btnSubmitTimesheet.setVisibility(View.VISIBLE);
        }

        SpannableTextViewer.displaySpannableText(context.getResources().getString(R.string.read_more), holder.more);

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Intent intent=new Intent(context, InterestReceviedListActivity.class);
                // context.startActivity(intent);

            }
        });
        //holder.interest_received.setText("#INTEREST RECEIVED("+postedJobItem.getConfirmedCount()+")");
    }

    @Override
    public int getItemCount() {
        return completedEmployeeJobItems.size();
    }
}
