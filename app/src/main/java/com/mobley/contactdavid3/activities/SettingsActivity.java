package com.mobley.contactdavid3.activities;

import java.util.List;

import android.os.Bundle;
import android.preference.EditTextPreference;
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
        return (fragmentName.equals("com.mobley.contactdavid3.activities.SettingsActivity$ContactPreferences") ||
				fragmentName.equals("com.mobley.contactdavid3.activities.SettingsActivity$TimePreferences") ||
				fragmentName.equals("com.mobley.contactdavid3.activities.SettingsActivity$MiscPreferences"));
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
	 * Misc Preferences Fragment
	 * @author mobleyd
	 *
	 */
	public static class MiscPreferences extends PreferenceFragment {
		/** Logging tag */
		protected static final String TAG = MiscPreferences.class.getSimpleName();

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			ContactDavid3App app = (ContactDavid3App) getActivity().getApplication();

			addPreferencesFromResource(R.xml.misc_prefs);

			// Version
			EditTextPreference versionPref = (EditTextPreference)findPreference(ContactDavid3App.PREF_VERSION_KEY);
			String version = app.getAppPrefs().getString(ContactDavid3App.PREF_VERSION_KEY, "???");
			versionPref.setTitle(getString(R.string.pref_version_key) + " " + version);
		}
	}

	/**
	 * Contact Preferences Fragment
	 * @author mobleyd
	 *
	 */
	public static class ContactPreferences extends PreferenceFragment {
		/** Logging tag */
		protected static final String TAG = ContactPreferences.class.getSimpleName();

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.contact_prefs);
		}
	}

	/**
	 * Work Times Preferences Fragment
	 * @author mobleyd
	 *
	 */
	public static class TimePreferences extends PreferenceFragment {
		protected static final String TAG = TimePreferences.class.getSimpleName();

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			addPreferencesFromResource(R.xml.time_prefs);
		}
	}
}