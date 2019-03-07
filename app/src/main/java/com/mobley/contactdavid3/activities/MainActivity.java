package com.mobley.contactdavid3.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mobley.contactdavid3.ContactDavid3App;
import com.mobley.contactdavid3.R;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_CALL_PERMISSION = 1;

    private Button mTextButton, mEmailButton, mPhoneButton;
    private ContactDavid3App mApp;
    private boolean mCallGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (ContactDavid3App) getApplication();
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

        verifyPermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTextButton.setText(String.format(getString(R.string.text_button_label), mApp.getAppPrefs().getString(ContactDavid3App.PREF_NAME_KEY, getString(R.string.default_name))));
        mEmailButton.setText(String.format(getString(R.string.email_button_label), mApp.getAppPrefs().getString(ContactDavid3App.PREF_NAME_KEY, getString(R.string.default_name))));
        mPhoneButton.setText(String.format(getString(R.string.phone_button_label), mApp.getAppPrefs().getString(ContactDavid3App.PREF_NAME_KEY, getString(R.string.default_name))));
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
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mTextButton) {
            String cell = mApp.getAppPrefs().getString(ContactDavid3App.PREF_CELL_PHONE_KEY,
                    getString(R.string.default_cell_phone));

            Uri uri = Uri.parse("smsto:" + cell);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(sendIntent);

        } else if (view == mEmailButton) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { getString(R.string.default_email) });
            intent.putExtra(Intent.EXTRA_SUBJECT, "From ContactDavid3 App");
            //intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

            startActivity(Intent.createChooser(intent, "Send Email"));

        } else if (view == mPhoneButton) {
            doPhoneCall();
        }
    }

    private void doPhoneCall() {
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
        int start = 6;
        //mApp.getAppPrefs().getInt(ContactDavid3App.PREF_START_HOUR_KEY, getResources().getInteger(R.integer.default_start_hour));
        int stop = 14;
        //mApp.getAppPrefs().getInt(ContactDavid3App.PREF_STOP_HOUR_KEY, getResources().getInteger(R.integer.default_stop_hour));

        int now = cal.get(Calendar.HOUR_OF_DAY);
        if (now >= start && now <= stop) {
            return true;
        }

        return false;
    }
}
