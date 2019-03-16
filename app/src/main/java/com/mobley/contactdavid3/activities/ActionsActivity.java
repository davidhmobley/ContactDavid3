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
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mobley.contactdavid3.ContactDavid3App;
import com.mobley.contactdavid3.R;
import com.mobley.contactdavid3.adapters.CustomAdapter;
import com.mobley.contactdavid3.dialogs.WorkTimeDialog;
import com.mobley.contactdavid3.sql.SqlDataSource;
import com.mobley.contactdavid3.sql.tables.Actions;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ActionsActivity extends AppCompatActivity {
    protected static final String TAG = ActionsActivity.class.getSimpleName();

    private ContactDavid3App mApp;
    private SqlDataSource mSqlDataSource = null;
    private List<Actions> mActions = null;
    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (ContactDavid3App) getApplication();
        mSqlDataSource = new SqlDataSource(this);
        setContentView(R.layout.activity_actions);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle(getResources().getString(R.string.app_title));
        actionBar.setSubtitle(getResources().getString(R.string.actions_subtitle));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mSqlDataSource.open();
        mActions = mSqlDataSource.getAllActions();
        mSqlDataSource.close();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //mRecyclerView.setLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
        mAdapter = new CustomAdapter(mActions);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean bOK = false;

        switch(item.getItemId()) {
            case R.id.action_return:
                bOK = true; // processed

                onBackPressed();

                break;
            default:
                bOK = super.onOptionsItemSelected(item);
                break;
        }

        return bOK;
    }
}
