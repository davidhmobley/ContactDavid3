package com.mobley.contactdavid3.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mobley.contactdavid3.ContactDavid3App;
import com.mobley.contactdavid3.R;
import com.mobley.contactdavid3.dialogs.WorkTimeDialog;
import com.mobley.contactdavid3.sql.SqlDataSource;
import com.mobley.contactdavid3.sql.tables.Actions;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_CALL_PERMISSION = 1;

    private Button mTextButton, mEmailButton, mPhoneButton, mActionsButton;
    private ContactDavid3App mApp;
    private boolean mCallGranted = false;
    private SqlDataSource mSqlDataSource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (ContactDavid3App) getApplication();
        mSqlDataSource = new SqlDataSource(this);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle(getResources().getString(R.string.app_title));
        actionBar.setSubtitle(getResources().getString(R.string.main_subtitle));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mTextButton = (Button) findViewById(R.id.textButton);
        mTextButton.setOnClickListener(this);

        mEmailButton = (Button) findViewById(R.id.emailButton);
        mEmailButton.setOnClickListener(this);

        mPhoneButton = (Button) findViewById(R.id.phoneButton);
        mPhoneButton.setOnClickListener(this);

        mActionsButton = (Button) findViewById(R.id.actionsButton);
        mActionsButton.setOnClickListener(this);

        verifyPermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTextButton.setText(String.format(getString(R.string.text_button_label), mApp.getAppPrefs().getString(ContactDavid3App.PREF_NAME_KEY, getString(R.string.default_name))));
        mEmailButton.setText(String.format(getString(R.string.email_button_label), mApp.getAppPrefs().getString(ContactDavid3App.PREF_NAME_KEY, getString(R.string.default_name))));
        mPhoneButton.setText(String.format(getString(R.string.phone_button_label), mApp.getAppPrefs().getString(ContactDavid3App.PREF_NAME_KEY, getString(R.string.default_name))));

        mCallGranted = mApp.getAppPrefs().getBoolean(ContactDavid3App.PREF_PHONE_PERMISSION_KEY, false);
        mPhoneButton.setEnabled(mCallGranted);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean bOK = false;

        switch(item.getItemId()) {
            case R.id.action_settings:
                bOK = true; // processed

                Intent preferencesIntent = new Intent(this, SettingsActivity.class);
                startActivity(preferencesIntent);

                break;
            case R.id.action_settimes:
                bOK = true; // processed

                WorkTimeDialog dlg = (WorkTimeDialog) WorkTimeDialog.newInstance();
                dlg.setStartTime(mApp.getAppPrefs().getString(ContactDavid3App.PREF_TIME_START_KEY, getString(R.string.default_time_start)));
                dlg.setEndTime(mApp.getAppPrefs().getString(ContactDavid3App.PREF_TIME_END_KEY, getString(R.string.default_time_end)));
                dlg.show(getSupportFragmentManager(), "WorkTime");

                break;
            case R.id.action_exit:
                bOK = true; // processed

                onBackPressed();

                break;
            default:
                bOK = super.onOptionsItemSelected(item);
                break;
        }

        return bOK;
    }

    private void verifyPermissions(Activity context) {
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,
                    new String[] { Manifest.permission.CALL_PHONE },
                    REQUEST_CALL_PERMISSION);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        switch(requestCode) {
            case(REQUEST_CALL_PERMISSION):
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    // got it!
                    mCallGranted = true;
                } else {
                    mCallGranted = false;
                }

                SharedPreferences.Editor editor = mApp.getAppPrefs().edit();
                editor.putBoolean(ContactDavid3App.PREF_PHONE_PERMISSION_KEY, mCallGranted);
                editor.commit();

                break;
        }
    }

    @Override
    public void onClick(View view) {
        Calendar cal = Calendar.getInstance();

        if (view == mTextButton) {
            String cell = mApp.getAppPrefs().getString(ContactDavid3App.PREF_CELL_PHONE_KEY,
                    getString(R.string.default_cell_phone));

            mApp.mySnackbar(view, String.format(getString(R.string.textbar_msg), cell), true);

            // make a note of this action
            mSqlDataSource.open();
            mSqlDataSource.insertActions(getString(R.string.action_type_text), cal.getTimeInMillis(), cell);
            mSqlDataSource.close();

            Uri uri = Uri.parse("smsto:" + cell);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(sendIntent);

        } else if (view == mEmailButton) {
            String email = mApp.getAppPrefs().getString(ContactDavid3App.PREF_EMAIL_KEY, getString(R.string.default_email));

            mApp.mySnackbar(view, String.format(getString(R.string.emailbar_msg), email), true);

            // make a note of this action
            mSqlDataSource.open();
            mSqlDataSource.insertActions(getString(R.string.action_type_email), cal.getTimeInMillis(), email);
            mSqlDataSource.close();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
            intent.putExtra(Intent.EXTRA_SUBJECT, "From ContactMe App");
            //intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

            startActivity(Intent.createChooser(intent, "Send Email"));

        } else if (view == mPhoneButton) {
            doPhoneCall(view);
        } else if (view == mActionsButton) {
            Intent intent = new Intent(this, ActionsActivity.class);
            startActivity(intent);
        }
    }

    private void doPhoneCall(View view) {
        String cell = mApp.getAppPrefs().getString(ContactDavid3App.PREF_CELL_PHONE_KEY,
                getString(R.string.default_cell_phone));
        String work = mApp.getAppPrefs().getString(ContactDavid3App.PREF_WORK_PHONE_KEY,
                getString(R.string.default_work_phone));
        String cellUri = "tel:" + cell;
        String workUri = "tel:" + work;
        String uri = null;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

        if (isWeekend(cal)) {
            uri = cellUri;
        } else {
            if (isDuringWorkHours(cal)) {
                uri = workUri;
            } else {
                uri = cellUri;
            }
        }

        mApp.mySnackbar(view, String.format(getString(R.string.phonebar_msg), uri), true);

        // make a note of this action
        mSqlDataSource.open();
        mSqlDataSource.insertActions(getString(R.string.action_type_call), cal.getTimeInMillis(), uri);
        mSqlDataSource.close();

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));

        if (checkPermission()) {
            startActivity(intent);
        }
    }

    private boolean checkPermission() {
        int res = checkCallingOrSelfPermission("android.permission.CALL_PHONE");

        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private boolean isWeekend(Calendar cal) {
        int today = cal.get(Calendar.DAY_OF_WEEK);
        if (today == Calendar.SATURDAY || today == Calendar.SUNDAY) {
            return true;
        }

        return false;
    }

    private boolean isDuringWorkHours(Calendar cal) {
        String hourStr, minuteStr, startStr, endStr;
        int colon, startHour, startMinutes, endHour, endMinutes;

        // Start Work Time
        startStr = mApp.getAppPrefs().getString(ContactDavid3App.PREF_TIME_START_KEY, getString(R.string.default_time_start));
        // returns something like 9:15 or just 9
        colon = startStr.indexOf(':');
        if (colon == -1) {
            hourStr = startStr;
            minuteStr = "0";
        } else {
            hourStr = startStr.substring(0, colon);
            minuteStr = startStr.substring(colon+1);
        }
        startHour = Integer.valueOf(hourStr);
        startMinutes = Integer.valueOf(minuteStr);

        // End Work Time
        endStr = mApp.getAppPrefs().getString(ContactDavid3App.PREF_TIME_END_KEY, getString(R.string.default_time_end));
        // returns something like 2:15 or just 2
        colon = endStr.indexOf(':');
        if (colon == -1) {
            hourStr = endStr;
            minuteStr = "0";
        } else {
            hourStr = endStr.substring(0, colon);
            minuteStr = endStr.substring(colon+1);
        }
        endHour = Integer.valueOf(hourStr);
        if (endHour < startHour) {
            endHour += 12; // 24 hour clock!
        }
        endMinutes = Integer.valueOf(minuteStr);

        // current values
        int nowHour = cal.get(Calendar.HOUR_OF_DAY);
        int nowMinute = cal.get(Calendar.MINUTE);

        // convert to minutes past midnight
        nowHour = (nowHour * 60) + nowMinute;
        startHour = (startHour * 60) + startMinutes;
        endHour = (endHour * 60) + endMinutes;

        if (nowHour >= startHour && nowHour <= endHour) {
            return true;
        }

        return false;
    }
}
