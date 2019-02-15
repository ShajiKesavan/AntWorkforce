package com.sample.poc.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dell on 2/9/2019.
 */

public class PreferenceHelper {

    public static String USERLOGIN_PREF = "user_login_pref";
    public static String USERNAME_PREF = "user_name_pref";
    public static String NAME_PREF = "name_pref";
    public static String USERID_PREF = "user_id_pref";
    public static String USERTOKEN_PREF = "user_token_pref";
    public static String USERROLE_PREF = "user_role_pref";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("PreferencesAcrossApplication",
                Context.MODE_PRIVATE);
    }

    public static void setUserLogin_PREF(Context context, boolean state) {
        getPrefs(context).edit().putBoolean(USERLOGIN_PREF, state).apply();
    }

    public static boolean getUserLogin_PREF(Context context) {
        return getPrefs(context).getBoolean(USERLOGIN_PREF, false);
    }

    public static void setUserData_PREF(Context context, String name, String id, String token, String role) {
        getPrefs(context).edit().putString(USERNAME_PREF, name).apply();
        getPrefs(context).edit().putString(USERID_PREF, id).apply();
        getPrefs(context).edit().putString(USERTOKEN_PREF, token).apply();
        getPrefs(context).edit().putString(USERROLE_PREF, role).apply();
    }

    public static void setName_PREF(Context context, String name) {
        getPrefs(context).edit().putString(NAME_PREF, name).apply();
    }

    public static String getUserName_PREF(Context context) {
        return getPrefs(context).getString(USERNAME_PREF, "");
    }

    public static String getName_PREF(Context context) {
        return getPrefs(context).getString(NAME_PREF, "");
    }


    public static String getUserId_PREF(Context context) {
        return getPrefs(context).getString(USERID_PREF, "0");
    }

    public static String getUserToken_PREF(Context context) {
        return getPrefs(context).getString(USERTOKEN_PREF, "");
    }

    public static String getUserRole_PREF(Context context) {
        return getPrefs(context).getString(USERROLE_PREF, "");
    }


}
