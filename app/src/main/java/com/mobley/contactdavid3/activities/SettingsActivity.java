package com.mobley.contactdavid3.activities;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;

import com.mobley.contactdavid3.ContactDavid3App;
import com.mobley.contactdavid3.R;

/**
 * Preference Activity for this application
 * @author mobleyd
 */
public class SettingsActivity extends PreferenceActivity {
	/** Logging tag */
	protected static final String TAG = SettingsActivity.class.getSimpleName();

	private ContactDavid3App mApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApp = (ContactDavid3App) getApplication();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
       	getActionBar().setTitle(getString(R.string.app_name));
       	getActionBar().setSubtitle(getResources().getString(R.string.pref_subtitle));
       	
       	setTheme(android.R.style.Theme_Holo);
	}

    @Override
    protected boolean isValidFragment(String fragmentName) {
        //return super.isValidFragment(fragmentName);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	boolean bOK = false;
    	
    	switch(item.getItemId()) {
    	case R.id.action_exit:
    	case android.R.id.home:
    		bOK = true; // processed
    		
			onBackPressed();

    		break;
    	default:
    		bOK = super.onOptionsItemSelected(item);
    		break;
    	}
    	
    	return bOK;
    }
	
    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.prefs, target);
    }

	/**
	 * Phone Preferences Fragment
	 * @author mobleyd
	 *
	 */
	public static class PhonePreferences extends PreferenceFragment {
		/** Logging tag */
		protected static final String TAG = PhonePreferences.class.getSimpleName();

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.phone_prefs);
		}
	}

	/**
	 * Name Preferences Fragment
	 * @author mobleyd
	 *
	 */
	public static class NamePreferences extends PreferenceFragment {
		/** Logging tag */
		protected static final String TAG = NamePreferences.class.getSimpleName();

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.name_prefs);
		}
	}

}