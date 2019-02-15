package com.sample.poc.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sample.poc.Items.PostedJobItems;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;
import com.sample.poc.Utilities.PreferenceHelper;
import com.sample.poc.Utilities.SpannableTextViewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewJobPostingActivity extends AppCompatActivity {

    LinearLayout lnrNumberOfDays;
    TextView edtDate;
    SwitchCompat switchRepeat;
    TextView edtTime;
    Spinner spinnerRole;
    String[] data,id,rate,duration, role;
    String jobId,jobDescription,jobRate,jobDuration,jobRole;
    EditText mHours,mRate,mDescription,mNoOfRoles;
    Button postBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_posting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getEmployerRoles(DashboardActivity.employersJs);
        initialiseControls();
    }


    void initialiseControls()
    {
        lnrNumberOfDays=findViewById(R.id.lnrNumberOfDays);
        switchRepeat=findViewById(R.id.switchRepeat);
        edtDate=findViewById(R.id.edtDate);
        edtTime=findViewById(R.id.edtTime);
        spinnerRole=findViewById(R.id.spinnerRole);
        mRate=(EditText)findViewById(R.id.edtRate);
        mDescription=(EditText)findViewById(R.id.edtDesc);
        mHours=(EditText)findViewById(R.id.edtHours);
        mNoOfRoles=(EditText)findViewById(R.id.edtNOR);
        postBtn=(Button)findViewById(R.id.btnAccept);

        //String[] data= {"Select","Registered General Nurse","Registered Mental Nurse","Sr Carer"};

        final Calendar c = Calendar.getInstance();

        SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

        String date=formatDate.format(c.getTime());
        String time=formatTime.format(c.getTime());
        SpannableTextViewer.displaySpannableText(date,edtDate);
        SpannableTextViewer.displaySpannableText(time,edtTime);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,role);

        spinnerRole .setAdapter(adapter);
        adapter.notifyDataSetChanged();

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jobId = id[i];
                jobDescription = data[i];
                jobRate = rate[i];
                jobDuration = duration[i];
                jobRole = role[i];
                mHours.setText(jobDuration);
                mRate.setText(jobRate);
                mDescription.setText(jobDescription);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePicker();
            }
        });

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        switchRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked)
                    lnrNumberOfDays.setVisibility(View.VISIBLE);
                else
                    lnrNumberOfDays.setVisibility(View.GONE);
            }
        });
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hours = mHours.getText().toString();
                String rate = mRate.getText().toString();
                String desc = mDescription.getText().toString();
                String noOfRoles = mNoOfRoles.getText().toString();
                View focusView = null;
                if(TextUtils.isEmpty(hours)){
                    mHours.setError(getString(R.string.error_field_required));
                    focusView = mHours;
                    focusView.requestFocus();
                } else if(TextUtils.isEmpty(rate)){
                    mRate.setError(getString(R.string.error_field_required));
                    focusView = mRate;
                    focusView.requestFocus();
                } else if(TextUtils.isEmpty(desc)){
                    mDescription.setError(getString(R.string.error_field_required));
                    focusView = mDescription;
                    focusView.requestFocus();
                } else if(TextUtils.isEmpty(noOfRoles)){
                    mNoOfRoles.setError(getString(R.string.error_field_required));
                    focusView = mNoOfRoles;
                    focusView.requestFocus();
                } else {
                    NewJobPostRequest(hours,rate,desc,noOfRoles);
                }
            }
        });

        lnrNumberOfDays.setVisibility(View.GONE);
    }




    void showDatePicker()
    {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                        String date = format.format(calendar.getTime());
                        SpannableTextViewer.displaySpannableText(date,edtDate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    void showTimePicker()
    {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String time;
                        if(minute<10)
                            time=hourOfDay + ":0" + minute;
                        else
                            time=hourOfDay + ":" + minute;
                        SpannableTextViewer.displaySpannableText(time,edtTime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void getEmployerRoles(final String empResponse){
        try {
            if(empResponse!=null) {
                System.out.println("responseFromemproles2 :" + empResponse.toString());
                //JSONArray jArrayEmp = new JSONArray(empResponse);
                //JSONObject jObject = jArrayEmp.getJSONObject(0);
                //String roles = jObject.getString("roles");
                JSONArray jArrayEmpRoles = new JSONArray(empResponse);
                JSONObject jObject1 = null;
                data=new String[jArrayEmpRoles.length()];
                id=new String[jArrayEmpRoles.length()];
                rate=new String[jArrayEmpRoles.length()];
                duration=new String[jArrayEmpRoles.length()];
                role= new String[jArrayEmpRoles.length()];
                for (int i = 0; i < jArrayEmpRoles.length(); i++) {
                    jObject1 = jArrayEmpRoles.getJSONObject(i);
                    String idEmp = jObject1.getString("id");
                    id[i] = idEmp;
                    String description = jObject1.getString("description");
                    data[i] = description;
                    String rateEmp = jObject1.getString("rate");
                    rate[i] = rateEmp;
                    String durationEmp = jObject1.getString("duration");
                    duration[i] = durationEmp;
                    String roleEmp = jObject1.getString("name");
                    role[i] = roleEmp;
                }
            } else {
                data=new String[1];
                id=new String[1];
                rate=new String[1];
                duration=new String[1];
                role= new String[1];
                role[0] = "No roles";
                id[0] = "0";
                data[0] = "";
                rate[0] = "";
                duration[0] = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void NewJobPostRequest(String hours,String rate,String desc,String noOfRoles){

        try {
            // Simulate network access.
            //Thread.sleep(2000);
            JSONObject js = new JSONObject();
            try {
                js.put("roleId", Integer.valueOf(jobId));
                js.put("description", desc);
                js.put("rate", Double.valueOf(rate));
                js.put("duration", Integer.valueOf(hours));
                js.put("startDateTime", edtDate.getText().toString()+" "+edtTime.getText().toString());
                js.put("vacancies", Integer.valueOf(noOfRoles));
                js.put("requestingUser", Integer.valueOf(PreferenceHelper.getUserId_PREF(getApplicationContext())));
                System.out.println("json obj: "+js.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Make request for JSONObject
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.POST, Constants.BASE_URL+Constants.NEW_JOB_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("responseFrompostjobnewwwww:" + response.toString());
                            Toast.makeText(getApplicationContext(),"Successfully Posted new job!",Toast.LENGTH_LONG).show();
                            Intent in = new Intent(NewJobPostingActivity.this,DashboardActivity.class);
                            in.putExtra("dashbdJs",response.toString());
                            startActivity(in);
                            finish();
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
                        Intent in = new Intent(NewJobPostingActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
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
                    headers.put("X-Auth-Token", PreferenceHelper.getUserToken_PREF(getApplicationContext()));
                    headers.put("X-Auth-User", String.valueOf(PreferenceHelper.getUserId_PREF(getApplicationContext())));
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
