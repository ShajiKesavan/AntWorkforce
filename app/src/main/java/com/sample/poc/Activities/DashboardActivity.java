package com.sample.poc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sample.poc.Fragments.CompletedJobsFragment;
import com.sample.poc.Fragments.MyShiftsFragment;
import com.sample.poc.Fragments.CompletedJobsEmployeeFragment;
import com.sample.poc.Fragments.AvailableShiftsFragment;
import com.sample.poc.R;
import com.sample.poc.Utilities.Constants;
import com.sample.poc.Utilities.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sample.poc.Utilities.Constants.EMPLOYEE;
import static com.sample.poc.Utilities.Constants.EMPLOYER;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public String currentRole = EMPLOYEE;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String postsJs, shiftsJs ,employersJs,resourceId;
    TextView name, userName;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_list_24px);

        String response;
        Intent intent = getIntent();

        currentRole = PreferenceHelper.getUserRole_PREF(getApplicationContext());
        //currentRole = EMPLOYER;
        System.out.println("dashbd role:"+currentRole);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add);
        if (currentRole.equals(EMPLOYER)) {
            fab.setVisibility(View.VISIBLE);
            if (intent.hasExtra("dashbdJs")) {
                response = intent.getStringExtra("dashbdJs");
                getDashbdData(response);
            } else {
                getDashbdResponse(Integer.valueOf(PreferenceHelper.getUserId_PREF(getApplicationContext())),
                        PreferenceHelper.getUserToken_PREF(getApplicationContext()));
            }
        }
        else {
            fab.setVisibility(View.GONE);
            if (intent.hasExtra("dashbdJs")) {
                response = intent.getStringExtra("dashbdJs");
                getDashbdResourceData(response);
            } else {
                getDashbdResourceResponse(Integer.valueOf(PreferenceHelper.getUserId_PREF(getApplicationContext())),
                        PreferenceHelper.getUserToken_PREF(getApplicationContext()));
            }
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashboardActivity.this, NewJobPostingActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // drawer.addDrawerListener(toggle);
        //toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if(!currentRole.equals(EMPLOYER)) {
            tabLayout.setupWithViewPager(viewPager);
        } else {
            tabLayout.setVisibility(View.GONE);
        }

        View headerView = navigationView.getHeaderView(0);
        name = (TextView)headerView.findViewById(R.id.txtName);
        userName = (TextView)headerView.findViewById(R.id.txtUsername);
        userName.setText(PreferenceHelper.getUserName_PREF(getApplicationContext()));
        name.setText(PreferenceHelper.getName_PREF(getApplicationContext()));
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (currentRole.equals(EMPLOYER)) {
            adapter.addFragment(new CompletedJobsEmployeeFragment(), "");
        } else {
            adapter.addFragment(new AvailableShiftsFragment(), "Available shifts");
            adapter.addFragment(new MyShiftsFragment(), "My Shifts");
            adapter.addFragment(new CompletedJobsFragment(), "Completed Shifts");
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_logout){
            PreferenceHelper.setUserLogin_PREF(getApplicationContext(),false);
            Intent in = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(in);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getDashbdResponse(final int userId, final String token){

        try {
            // Make request for JSONObject
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET, Constants.BASE_URL+Constants.DASHBOARD_URL+userId, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("responseFromDashbdd :" + response.toString());
                            JSONArray jArrayEmp = null;
                            JSONObject jObject = null;
                            try {
                                jArrayEmp = new JSONArray(response.getString("employers"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                jObject = jArrayEmp.getJSONObject(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                postsJs = jObject.getString("posts");
                                System.out.println("postJs:"+postsJs);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try{
                                shiftsJs = jObject.getString("shifts");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try{
                                employersJs = jObject.getString("roles");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setupViewPager(viewPager);
                            tabLayout.setupWithViewPager(viewPager);
                                System.out.println("responseFromDashbdddPost id:" + userId);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error instanceof TimeoutError || error instanceof NoConnectionError){
                        Toast.makeText(AntApplication._appContext,
                                "Network Timeout Error or No internet, Please try again.",
                                Toast.LENGTH_LONG).show();

                    } else if(error.toString().contains("AuthFailureError")) {
                        Toast.makeText(AntApplication._appContext,
                                "Token Expired, Please try again.",
                                Toast.LENGTH_LONG).show();
                        Intent in = new Intent(DashboardActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                    }
                    else {
                        Toast.makeText(AntApplication._appContext,
                                "Error, Please try again.",
                                Toast.LENGTH_LONG).show();

                    }
                    System.out.println("responseFromLogin2 Dashbddd err"
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
                    headers.put("X-Auth-Token", token);
                    headers.put("X-Auth-User", String.valueOf(userId));
                    return headers;
                }

            };

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

            public void getDashbdData(String respons){

                System.out.println("responseFromDashbddd Data2 :" + respons.toString());
                JSONArray jArrayEmp = null;
                JSONObject jObject = null;
                JSONObject obj = null;
                try {
                    obj = new JSONObject(respons);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jArrayEmp = new JSONArray(obj.getString("employers"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jObject = jArrayEmp.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    postsJs = jObject.getString("posts");
                    JSONArray jArray = new JSONArray(postsJs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try{
                    shiftsJs = jObject.getString("shifts");
                    JSONArray jArray1 = new JSONArray(shiftsJs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try{
                    employersJs = jObject.getString("roles");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
    }

    public void getDashbdResourceResponse(final int userId, final String token){

        try {
            // Make request for JSONObject
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET, Constants.BASE_URL+Constants.DASHBOARD_URL+userId, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("responseFromDashbdd res :" + response.toString());
                            JSONObject jObject = null;

                            try {
                                String res = response.getString("resource");
                                jObject = new JSONObject(res);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                resourceId = jObject.getString("resourceId");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                postsJs = jObject.getString("posts");
                                JSONArray jArray = new JSONArray(postsJs);
                                System.out.println("postJs res:"+postsJs);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try{
                                shiftsJs = jObject.getString("shifts");
                                JSONArray jArray1 = new JSONArray(shiftsJs);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            setupViewPager(viewPager);
                            tabLayout.setupWithViewPager(viewPager);
                            System.out.println("responseFromDashbdddPost res id:" + userId);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error instanceof TimeoutError || error instanceof NoConnectionError){
                        Toast.makeText(AntApplication._appContext,
                                "Network Timeout Error or No internet, Please try again.",
                                Toast.LENGTH_LONG).show();

                    } else if(error.toString().contains("AuthFailureError")) {
                        Toast.makeText(AntApplication._appContext,
                                "Token Expired, Please try again.",
                                Toast.LENGTH_LONG).show();
                        Intent in = new Intent(DashboardActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                    }
                    else {
                        Toast.makeText(AntApplication._appContext,
                                "Error, Please try again.",
                                Toast.LENGTH_LONG).show();

                    }
                    System.out.println("responseFromLogin3 Dashbddd err"
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
                    headers.put("X-Auth-Token", token);
                    headers.put("X-Auth-User", String.valueOf(userId));
                    return headers;
                }

            };

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

    public void getDashbdResourceData(String respons){

        System.out.println("responseFromDashbddd Data2 res:" + respons.toString());
        JSONObject jObject = null;
        JSONObject obj = null;
        try {
            obj = new JSONObject(respons);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String res = obj.getString("resource");
            jObject = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            resourceId = jObject.getString("resourceId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            postsJs = jObject.getString("posts");
            JSONArray jArray = new JSONArray(postsJs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            shiftsJs = jObject.getString("shifts");
            JSONArray jArray1 = new JSONArray(shiftsJs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

/*responseFromDashbddd Data2 :{"userLogonId":6,"userLogonName":"vjvisakh100@gmail.com","userRole":"EMPLOYER",
        "employers":[{"id":6,"name":"visakh","displayName":"vj visakh","roles":[{"id":10,"name":"Registered General Nurse"
        ,"description":"RGN With at least 3 yrs experience","rate":22.5,"duration":10},
        {"id":11,"name":"Junior Registered General Nurse","description":"RGN With at least 1 yrs experience",
        "rate":20,"duration":10},{"id":12,"name":"Registered Mental Nurse",
        "description":"With at least 2 yrs experience","rate":21.5,"duration":10}],
        "posts":[{"id":2,"roleName":"Registered General Nurse","roleDescription":"RGN With at least 3 yrs experience",
        "rate":23.5,"duration":10,"startDateTime":"22 Feb 2019 08:00","vacancies":1,"employerName":"vj visakh"},
        {"id":1,"roleName":"Junior Registered General Nurse","roleDescription":"RGN With at least 1 yrs experience",
        "rate":21.5,"duration":10,"startDateTime":"21 Feb 2019 08:00","vacancies":1,"employerName":"vj visakh"}],
        "shifts":[{"id":1,"jobPostId":3,"roleName":"Registered General Nurse","rate":23,"duration":10,
        "startDateTime":"09 Feb 2019 08:00","shiftStatus":"CONFIRMED","statuses":["REJECT","APPROVE"],
        "resource":{"id":11,"name":"mr anttest anttest","imageUrl":"\/api\/resource\/11\/image","registration":null,
        "experience":"2 years","miniCV":"cv","preferredRate":23,"averageRating":0,"feedback":[]}}]}]}*/


