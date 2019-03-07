package com.mobley.contactdavid3.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    private Button mPhoneButton;
    private ContactDavid3App mApp;

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

        mPhoneButton = (Button) findViewById(R.id.phoneButton);

        mPhoneButton.setOnClickListener(this);

        /**
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         **/
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

    @Override
    public void onClick(View view) {
        if (view == mPhoneButton) {
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
