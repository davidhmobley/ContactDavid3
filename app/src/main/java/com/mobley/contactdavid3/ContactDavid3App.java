package com.mobley.contactdavid3;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ContactDavid3App extends Application {

    public static String PREF_NAME_KEY;
    public static String PREF_CELL_PHONE_KEY;
    public static String PREF_WORK_PHONE_KEY;

    private SharedPreferences mAppPrefs = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppPrefs = PreferenceManager.getDefaultSharedPreferences(this);

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
    }

    public SharedPreferences getAppPrefs() {
        return mAppPrefs;
    }
}
