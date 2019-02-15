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
import android.widget.EditText;
import android.widget.TextView;

import com.sample.poc.Activities.InterestReceviedListActivity;
import com.sample.poc.Activities.ResourceReadMore;
import com.sample.poc.Items.ListJobsItem;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;

import java.util.List;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class ListJobsAdapter extends RecyclerView.Adapter<ListJobsAdapter.MyViewHolder> {

    private List<ListJobsItem> listJobsItems;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDateTimeValue, roleValue, rateValue,durationValue,employerValue,more,distanceValue,edtPreRateInt,edtPreRateDec;
        public Button btnAccept;

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
            edtPreRateInt = (EditText) view.findViewById(R.id.edtPreRateInt);
            edtPreRateDec = (EditText) view.findViewById(R.id.edtPreRateDec);
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
    public void onBindViewHolder(ListJobsAdapter.MyViewHolder holder, int position) {
        final ListJobsItem listJobsItem= listJobsItems.get(position);
        holder.txtDateTimeValue.setText(listJobsItem.getStartDate()+" "+listJobsItem.getStartTime());
        holder.roleValue.setText(listJobsItem.getRole());
        holder.rateValue.setText(listJobsItem.getRate());
        holder.durationValue.setText(listJobsItem.getDuration());
        holder.employerValue.setText(listJobsItem.getEmployer());
        holder.distanceValue.setText(listJobsItem.getDistance());
        holder.edtPreRateDec.setText(Integer.toString(listJobsItem.getRateDec()));
        holder.edtPreRateInt.setText(Integer.toString(listJobsItem.getRateInt()));

        final String udata=context.getResources().getString(R.string.read_more);
        final SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        holder.more.setText(content);

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent=new Intent(context, ResourceReadMore.class);
                intent.putExtra("duration", listJobsItem.getDuration());
                intent.putExtra("desc", listJobsItem.getDescription());
                intent.putExtra("title", listJobsItem.getRole());
                intent.putExtra("rate", listJobsItem.getRate());
                intent.putExtra("date", listJobsItem.getStartDate());
                intent.putExtra("distance", listJobsItem.getDistance());
                intent.putExtra("employer", listJobsItem.getEmployer());
                String data[] = (listJobsItem.getStartTime()).split("@@");
                String vacancies = data[2];
                String address = data[1];
                intent.putExtra("vacancies", vacancies);
                intent.putExtra("address", address);
               context.startActivity(intent);

            }
        });
        //holder.interest_received.setText("#INTEREST RECEIVED("+postedJobItem.getConfirmedCount()+")");
    }

    @Override
    public int getItemCount() {
        return listJobsItems.size();
    }
}
