package com.sample.poc.Activities;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.sample.poc.R;
import com.sample.poc.Utilities.ConnectionDetector;
import com.sample.poc.Utilities.Constants;
import com.sample.poc.Utilities.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    String mRole;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
       // populateAutoComplete();
        cd = new ConnectionDetector(getApplicationContext());
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        ImageView mEmailSignInButton = (ImageView) findViewById(R.id.logipage_login_label);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            incorrectMsg(getString(R.string.error_email_required),mPasswordView,0);
        }
        // Check for a valid email address.
        else if (TextUtils.isEmpty(email)) {
            incorrectMsg(getString(R.string.error_pwd_required),mEmailView,0);
        } else if (!isEmailValid(email)) {
            incorrectMsg(getString(R.string.error_invalid_email),mEmailView,0);
        }
        // Check if Internet present
        else if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            openAlertMaterialNetwork();
        }
        else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            System.out.println("password1:"+password);
            showProgress(true);
            loginRequest(email,password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
       // return true;//email.equals("employee")||email.equals("employer");
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//           // mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
        public void loginRequest(final String mEmail, String mPassword){

            try {
                // Simulate network access.
                //Thread.sleep(2000);
                JSONObject js = new JSONObject();
                try {
                    js.put("userName", mEmail);
                    js.put("password", mPassword);
                    System.out.println("json obj: "+js.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Make request for JSONObject
                final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, Constants.BASE_URL+Constants.LOGIN_URL, js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("responseFromLogin" + response.toString());
                                try {
                                    String mUserId;
                                    String mToken;
                                    mUserId = String.valueOf(response.getInt("userId"));
                                    System.out.println("responseFromLogin id:" + mUserId);
                                    mToken = response.getString("token");
                                    getDashboardStatus(Integer.valueOf(mUserId),mToken,mEmail);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showToastMsg("Login Error, Please try again.",0);
                                    showProgress(false);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError || error instanceof NoConnectionError){
                            showToastMsg("Network Timeout Error or no internet, Please try again.",0);
                            showProgress(false);
                        } else {
                            incorrectLoginMsg("Incorrect username or password, Please try again.",0);
                            showProgress(false);
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
                showProgress(false);
            }

    }

    public void getDashboardStatus(final int userId, final String token, final String email){

        try {
            // Make request for JSONObject
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET, Constants.BASE_URL+Constants.DASHBOARD_URL+userId, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("responseFromLogin2 :" + response.toString());
                            try {
                                mRole = response.getString("userRole");
                                PreferenceHelper.setUserData_PREF(getApplicationContext(), email, String.valueOf(userId), token, mRole);
                                System.out.println("responseFromLoginPost id:" + userId);
                                String name = "";
                                if(mRole.equals(Constants.EMPLOYER)){
                                    String res = response.getString("employers");
                                    JSONArray jArray = new JSONArray(res);
                                    JSONObject jObject = jArray.getJSONObject(0);
                                    name = jObject.getString("name");
                                } else {
                                    name = "User Id: "+userId;
                                }
                                PreferenceHelper.setName_PREF(getApplicationContext(), name);
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                intent.putExtra("dashbdJs",response.toString());
                                startActivity(intent);
                                PreferenceHelper.setUserLogin_PREF(getApplicationContext(),true);
                                finish();
                                showProgress(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showToastMsg("Error, Please try again.",0);
                                showProgress(false);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error instanceof TimeoutError || error instanceof NoConnectionError){
                        showToastMsg("Network Timeout Error or No internet, Please try again.",0);
                    } else {
                        showToastMsg("Error, Please try again.",0);
                    }
                    System.out.println("responseFromLogin2 err"
                            + error);
                    error.printStackTrace();
                    showProgress(false);
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
            showProgress(false);
            showToastMsg("Error, Please try again.",0);
        }
    }
    /** AlertDialog for network check login */
    private void openAlertMaterialNetwork() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LoginActivity.this,R.style.AlertDialogStyle);

        alertDialogBuilder.setMessage(R.string.networkcheckdialog);

        alertDialogBuilder.setPositiveButton(R.string.materialDialogok,
                new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showToastMsg(String msg,int status){
            if(status == 0)
                Toasty.error(LoginActivity.this, msg,
                        Toast.LENGTH_LONG, true).show();
            else
                Toasty.success(LoginActivity.this, msg,
                        Toast.LENGTH_LONG, true).show();
    }

    public void incorrectMsg(String msg,EditText edt,int status) {
        //Toast.makeText(NewJobPostingActivity.this,msg,Toast.LENGTH_LONG).show();
        showToastMsg(msg,status);
        Animation shake = AnimationUtils.loadAnimation(this,
                R.anim.animation_shake);
        edt.startAnimation(shake);
    }
    public void incorrectLoginMsg(String msg,int status) {
        LinearLayout ln = (LinearLayout)findViewById(R.id.email_login_form);
        showToastMsg(msg,status);
        Animation shake = AnimationUtils.loadAnimation(this,
                R.anim.animation_shake);
        ln.startAnimation(shake);
    }
}

