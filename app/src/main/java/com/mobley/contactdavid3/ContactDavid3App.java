package com.mobley.contactdavid3;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class ContactDavid3App extends Application {

    public static String PREF_VERSION_KEY;
    public static String PREF_EMAIL_KEY;
    public static String PREF_NAME_KEY;
    public static String PREF_CELL_PHONE_KEY;
    public static String PREF_WORK_PHONE_KEY;
    public static String PREF_TIME_START_KEY;
    public static String PREF_TIME_END_KEY;
    public static String PREF_PHONE_PERMISSION_KEY;

    private SharedPreferences mAppPrefs = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        PREF_VERSION_KEY = getResources().getString(R.string.pref_version_key);
        {
            SharedPreferences.Editor editor = mAppPrefs.edit();
            String ver = null;
            try {
                ver = "v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                ver = "???";
                e.printStackTrace();
            }

            editor.putString(PREF_VERSION_KEY, ver);
            editor.commit();
        }

        PREF_TIME_START_KEY = getResources().getString(R.string.pref_time_start_key);
        if (!mAppPrefs.contains(PREF_TIME_START_KEY)) {
            SharedPreferences.Editor editor = mAppPrefs.edit();
            editor.putString(PREF_TIME_START_KEY, getString(R.string.default_time_start));
            editor.commit();
        }

        PREF_TIME_END_KEY = getResources().getString(R.string.pref_time_end_key);
        if (!mAppPrefs.contains(PREF_TIME_END_KEY)) {
            SharedPreferences.Editor editor = mAppPrefs.edit();
            editor.putString(PREF_TIME_END_KEY, getString(R.string.default_time_end));
            editor.commit();
        }

        PREF_CELL_PHONE_KEY = getResources().getString(R.string.pref_cell_phone_key);
        if (!mAppPrefs.contains(PREF_CELL_PHONE_KEY)) {
            SharedPreferences.Editor editor = mAppPrefs.edit();
            editor.putString(PREF_CELL_PHONE_KEY, getString(R.string.default_cell_phone));
            editor.commit();
        }

        PREF_WORK_PHONE_KEY = getResources().getString(R.string.pref_work_phone_key);
        if (!mAppPrefs.contains(PREF_WORK_PHONE_KEY)) {
            SharedPreferences.Editor editor = mAppPrefs.edit();
            editor.putString(PREF_WORK_PHONE_KEY, getString(R.string.default_work_phone));
            editor.commit();
        }

        PREF_NAME_KEY = getResources().getString(R.string.pref_name_key);
        if (!mAppPrefs.contains(PREF_NAME_KEY)) {
            SharedPreferences.Editor editor = mAppPrefs.edit();
            editor.putString(PREF_NAME_KEY, getString(R.string.default_name));
            editor.commit();
        }

        PREF_EMAIL_KEY = getResources().getString(R.string.pref_email_key);
        if (!mAppPrefs.contains(PREF_EMAIL_KEY)) {
            SharedPreferences.Editor editor = mAppPrefs.edit();
            editor.putString(PREF_EMAIL_KEY, getString(R.string.default_email));
            editor.commit();
        }

        PREF_PHONE_PERMISSION_KEY = getResources().getString(R.string.pref_phone_permission_key);
        if (!mAppPrefs.contains(PREF_PHONE_PERMISSION_KEY)) {
            SharedPreferences.Editor editor = mAppPrefs.edit();
            editor.putBoolean(PREF_PHONE_PERMISSION_KEY, false);
            editor.commit();
        }
    }

    public void mySnackbar(View view, String msg, boolean bLong) {
        Snackbar snackbar;
        if (bLong) {
            snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        } else {
            snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        }

        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        TextView tv1 = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
        tv1.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        snackbar.show();
    }

    public SharedPreferences getAppPrefs() {
        return mAppPrefs;
    }
}
