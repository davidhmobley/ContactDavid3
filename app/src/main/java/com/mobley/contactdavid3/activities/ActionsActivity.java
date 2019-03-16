package com.mobley.contactdavid3.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.mobley.contactdavid3.ContactDavid3App;
import com.mobley.contactdavid3.R;
import com.mobley.contactdavid3.adapters.CustomAdapter;
import com.mobley.contactdavid3.sql.SqlDataSource;
import com.mobley.contactdavid3.sql.tables.Actions;

import java.util.List;

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
        //actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mSqlDataSource.open();
        mActions = mSqlDataSource.getAllActions();
        mSqlDataSource.close();

        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapter(mActions, mApp);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
            case android.R.id.home:
                bOK = true; // processed

                onBackPressed();

                break;
            case R.id.action_clear_db:
                bOK = true; // processed

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true)
                        .setTitle(getString(R.string.alert_verify_delete))
                        .setMessage(getString(R.string.alert_delete_msg))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                mSqlDataSource.open();
                                mSqlDataSource.deleteActions();
                                mSqlDataSource.close();

                                dialog.cancel(); // get out!
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel(); // get out!
                            }
                        });

                AlertDialog confirm = builder.create();
                confirm.show();

                break;
            default:
                bOK = super.onOptionsItemSelected(item);
                break;
        }

        return bOK;
    }
}
