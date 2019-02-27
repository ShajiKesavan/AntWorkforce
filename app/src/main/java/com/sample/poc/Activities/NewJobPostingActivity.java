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
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class NewJobPostingActivity extends AppCompatActivity {

    LinearLayout lnrNumberOfDays;
    EditText edtDate;
    SwitchCompat switchRepeat;
    EditText edtTime;
    Spinner spinnerRole;
    String[] data,id,rate,duration, role;
    String jobId,jobDescription,jobRate,jobDuration,jobRole;
    EditText mHours,mRate1,mRate2,mDescription,mNoOfRoles;
    Button postBtn;
    String response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_posting);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        mRate1=(EditText)findViewById(R.id.edtRate);
        mRate2=(EditText)findViewById(R.id.edtPreRate);
        mDescription=(EditText)findViewById(R.id.edtDesc);
        mHours=(EditText)findViewById(R.id.edtHours);
        mNoOfRoles=(EditText)findViewById(R.id.edtNOR);
        postBtn=(Button)findViewById(R.id.btnAccept);

        edtDate.setFocusable(false);
        edtDate.setClickable(true);
        edtTime.setFocusable(false);
        edtTime.setClickable(true);
        //String[] data= {"Select","Registered General Nurse","Registered Mental Nurse","Sr Carer"};

       /* final Calendar c = Calendar.getInstance();

        SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

        String date=formatDate.format(c.getTime());
        String time=formatTime.format(c.getTime());
        SpannableTextViewer.displaySpannableText(date,edtDate);
        SpannableTextViewer.displaySpannableText(time,edtTime);*/


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
                System.out.println("Jrateee:"+jobRate);
                if(jobRate.contains(".")){
                    int indexOfDecimal = jobRate.indexOf(".");
                    mRate1.setText(jobRate.substring(0, indexOfDecimal));
                    mRate2.setText(jobRate.substring(indexOfDecimal+1));
                } else {
                    mRate1.setText(jobRate);
                    mRate2.setText("0");
                }
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
                String date = edtDate.getText().toString().trim();
                String time = edtTime.getText().toString().trim();
                String hours = mHours.getText().toString().trim();
                String rate1 = mRate1.getText().toString().trim();
                String rate2 = mRate2.getText().toString().trim();
                String desc = mDescription.getText().toString().trim();
                String noOfRoles = mNoOfRoles.getText().toString().trim();
                if(TextUtils.isEmpty(date)){
                    //edtDate.setError(getString(R.string.error_field_required));
                    TextView txt = (TextView)findViewById(R.id.datetxt);
                    incorrectMsg("Please select Date",edtDate,txt);
                }
                else if(compareDate(date)){
                    TextView txt = (TextView)findViewById(R.id.datetxt);
                    incorrectMsg("Please select valid Date",edtDate,txt);
                }
                else if(TextUtils.isEmpty(time)){
                    //edtTime.setError(getString(R.string.error_field_required));
                    TextView txt = (TextView)findViewById(R.id.timetxt);
                    incorrectMsg("Please select Time",edtTime,txt);
                }
                else if(compareTime(time)){
                    //edtTime.setError(getString(R.string.error_field_required));
                    TextView txt = (TextView)findViewById(R.id.timetxt);
                    incorrectMsg("Please select valid Time",edtTime,txt);
                }
                else if(TextUtils.isEmpty(hours) || Integer.valueOf(hours)<0){
                    //mHours.setError(getString(R.string.error_field_required));
                    TextView txt = (TextView)findViewById(R.id.hourstxt);
                    incorrectMsg("Please enter hours",mHours,txt);
                } else if(TextUtils.isEmpty(rate1) || TextUtils.isEmpty(rate2) || Integer.valueOf(rate1)<0){
                    //mRate1.setError(getString(R.string.error_field_required));
                    TextView txt = (TextView)findViewById(R.id.ratetxt);
                    incorrectMsg("Please enter rate",mRate1,txt);
                } else if(TextUtils.isEmpty(desc)){
                    //mDescription.setError(getString(R.string.error_field_required));
                    TextView txt = (TextView)findViewById(R.id.desctxt);
                    incorrectMsg("Please enter description",mDescription,txt);
                } else if(TextUtils.isEmpty(noOfRoles)|| Integer.valueOf(noOfRoles)<0){
                    //mNoOfRoles.setError(getString(R.string.error_field_required));
                    TextView txt = (TextView)findViewById(R.id.noofroletxt);
                    incorrectMsg("Please enter No of Roles",mNoOfRoles,txt);
                } else {
                    NewJobPostRequest(hours,rate1+"."+rate2,desc,noOfRoles);
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
        //final Calendar c = Calendar.getInstance();
        //int mHour = c.get(Calendar.HOUR_OF_DAY);
        //int mMinute = c.get(Calendar.MINUTE);
        int mHour = 8;
        int mMinute = 0;

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
                        public void onResponse(JSONObject res) {
                            response = res.toString();
                            System.out.println("responseFrompostjobnewwwww:" + res.toString());
                            try {
                                Toasty.success(NewJobPostingActivity.this, "Successfully Posted new job!",
                                        Toast.LENGTH_SHORT, true).show();
                                refreshAll();
                            }catch(Exception e){
                                e.printStackTrace();
                                System.out.println("error toast");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error instanceof TimeoutError || error instanceof NoConnectionError){
                        showToastMsg("Network Timeout Error or no internet, Please try again.",0);

                    }else if(error.toString().contains("AuthFailureError")) {
                        showToastMsg("Token Expired, Please try again.",0);
                        PreferenceHelper.setUserLogin_PREF(AntApplication._appContext,false);
                        Intent in = new Intent(NewJobPostingActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                    }
                    else {
                        showToastMsg("Server Error, Please try again.",0);
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
            showToastMsg("Error, Please try again.",0);

        }

    }

    public void incorrectMsg(String msg,EditText edt,TextView txt) {
        //Toast.makeText(NewJobPostingActivity.this,msg,Toast.LENGTH_LONG).show();
        showToastMsg(msg,0);
        Animation shake = AnimationUtils.loadAnimation(this,
                R.anim.animation_shake);
        edt.startAnimation(shake);
        Animation shake1 = AnimationUtils.loadAnimation(this,
                R.anim.animation_shake);
        txt.startAnimation(shake1);

    }
    //return true if lower
    public boolean compareTime(String time){
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        int timeHour = Integer.valueOf(time.substring(0,time.indexOf(":")));
        int timeMinute = Integer.valueOf(time.substring(time.indexOf(":")+1));
        System.out.println("comparetime:: hour:"+hour+";;"+minute+";;"+timeHour+";;"+timeMinute);
        if(isSameDate(edtDate.getText().toString())) {
            if (hour > timeHour) {
                System.out.println("comparetime1:: hour:" + time);
                return true;
            } else if (hour == timeHour) {
                if (minute > timeMinute)
                    System.out.println("comparetime2:: hour:" + time);
                return true;
            }
        }
        return false;
    }

    public boolean isSameDate(String date){
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        String currentDate = format.format(c.getTime());
        if(!date.equals(currentDate))
            return false;
        return true;
    }
    //return true if lower
    public boolean compareDate(String date){
        Calendar c = Calendar.getInstance();

// set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

// and get that as a Date
        Date today = c.getTime();

// or as a timestamp in milliseconds
        long todayInMillis = c.getTimeInMillis();

// user-specified date which you are testing
// let's say the components come from a form or something
        SimpleDateFormat spf=new SimpleDateFormat("dd MMM yyyy");
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd MM yyyy");
        date = spf.format(newDate);
        System.out.println("dateeee"+date);

        int year = Integer.valueOf(date.substring(6));
        int month = Integer.valueOf(date.substring(3,5))-1;
        int dayOfMonth = Integer.valueOf(date.substring(0,2));
        System.out.println("dateeee"+date+";;"+year+";;"+month+";;"+dayOfMonth);
// reuse the calendar to set user specified date
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

// and get that as a Date
        Date dateSpecified = c.getTime();

// test your condition
        if (dateSpecified.before(today)) {
            System.err.println("Date specified [" + dateSpecified + "] is before today [" + today + "]");
            return true;
        } else {
            System.err.println("Date specified [" + dateSpecified + "] is NOT before today [" + today + "]");
        }
        return false;
    }

    public void showToastMsg(String msg,int status){
        if(status == 0)
            Toasty.error(NewJobPostingActivity.this, msg,
                    Toast.LENGTH_LONG, true).show();
        else
            Toasty.success(NewJobPostingActivity.this, msg,
                    Toast.LENGTH_LONG, true).show();
    }

    public void refreshAll(){
        edtTime.setText("");
        edtDate.setText("");
        int i = 0;
        spinnerRole.setSelection(i);
        jobId = id[i];
        jobDescription = data[i];
        jobRate = rate[i];
        jobDuration = duration[i];
        jobRole = role[i];
        mHours.setText(jobDuration);
        System.out.println("Jrateee:"+jobRate);
        if(jobRate.contains(".")){
            int indexOfDecimal = jobRate.indexOf(".");
            mRate1.setText(jobRate.substring(0, indexOfDecimal));
            mRate2.setText(jobRate.substring(indexOfDecimal+1));
        } else {
            mRate1.setText(jobRate);
            mRate2.setText("0");
        }
        mDescription.setText(jobDescription);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent in = new Intent(NewJobPostingActivity.this,DashboardActivity.class);
        if(response!=null)
        in.putExtra("dashbdJs",response);
        startActivity(in);
        finish();
    }
}
