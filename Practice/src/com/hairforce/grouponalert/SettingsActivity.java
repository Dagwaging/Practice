package com.hairforce.grouponalert;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	
	
	private ActivityUpdater activityUpdater;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
		
		SwitchPreference notifications = (SwitchPreference) findPreference("notifications");
		
		notifications.setOnPreferenceChangeListener(this);
		
		activityUpdater = new ActivityUpdater(this, ActivityService.class);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if((Boolean) newValue) {
			activityUpdater.start(2000);
		}
		else {
			activityUpdater.stop();
		}
		
		
		return true;
	}

}
