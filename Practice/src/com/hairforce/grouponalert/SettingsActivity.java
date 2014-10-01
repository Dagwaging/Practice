package com.hairforce.grouponalert;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * TODO Put here a description of what this class does.
 *
 * @author rosspa.
 *         Created Apr 15, 2014.
 */
public class SettingsActivity extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("I got here!!");
		this.addPreferencesFromResource(R.xml.settings);
	}

}
