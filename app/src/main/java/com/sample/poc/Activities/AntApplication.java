package com.sample.poc.Activities;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by dell on 2/6/2019.
 */

public class AntApplication extends Application {

    public static final String TAG = AntApplication.class.getSimpleName();
    public static Context _appContext;
    private RequestQueue mRequestQueue;
    public static AntApplication mInstance;


    @Override
    public void onCreate() {
        _appContext = getApplicationContext();
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AntApplication getInstance() {
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {

        /** set the default tag if tag is empty */
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
}
