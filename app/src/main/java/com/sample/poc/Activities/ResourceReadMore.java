package com.sample.poc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.sample.poc.R;
;

/**
 * Created by dell on 2/14/2019.
 */

public class ResourceReadMore extends AppCompatActivity {

    public TextView mDateTime,mRole,mRate,mDuration,mEmployerName,mEmployerAddress,mDistance,mVacancies,mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_readmore);
        mDateTime = (TextView)findViewById(R.id.txtDateTimeValue);
        mRole = (TextView)findViewById(R.id.title);
        mRate = (TextView)findViewById(R.id.rateValue);
        mDuration = (TextView)findViewById(R.id.durationValue);
        mEmployerName = (TextView)findViewById(R.id.employerValue);
        mEmployerAddress = (TextView)findViewById(R.id.addressValue);
        mDistance = (TextView)findViewById(R.id.distanceValue);
        mVacancies = (TextView)findViewById(R.id.vacanciesValue);
        mDescription = (TextView)findViewById(R.id.descValue);

        Intent in = getIntent();
        mRole.setText(in.getStringExtra("title"));
        mDateTime.setText(in.getStringExtra("date"));
        mRate.setText(in.getStringExtra("rate"));
        mDuration.setText(in.getStringExtra("duration"));
        mEmployerName.setText(in.getStringExtra("employer"));
        mEmployerAddress.setText(in.getStringExtra("address"));
        mDistance.setText(in.getStringExtra("distance"));
        mDescription.setText(in.getStringExtra("desc"));
        String vacancies= in.getStringExtra("vacancies");
        if(Integer.valueOf(vacancies)>0)
            mVacancies.setText(vacancies);
        else
            mVacancies.setVisibility(View.GONE);
    }
}
