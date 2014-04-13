package com.hairforce.grouponalert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.location.LocationClient;
import com.hairforce.grouponalert.data.Deal;

public class LocationService extends IntentService {
	public LocationService() {
		super("com.hairforce.grouponalert.LocationService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(intent.getExtras() != null) {
			sendBroadcast(new Intent("com.hairforce.grouponalert.LOCATION"));
			
			Bundle extras = intent.getExtras();
			
			Location location = extras.getParcelable(LocationClient.KEY_LOCATION_CHANGED);
			
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			
			lat -= prefs.getFloat("startLat", 0);
			lng -= prefs.getFloat("startLng", 0);
			
			lat *= MainActivity.SCALE;
			lng *= MainActivity.SCALE;
			
			lat += MainActivity.FAKE_LAT;
			lng += MainActivity.FAKE_LNG;
			
			location.setLatitude(lat);
			location.setLongitude(lng);
			
			List<Deal> newDeals = Utils.getDeals(location, 1000);
			
			Collections.sort(newDeals, new Comparator<Deal>() {
				@Override
				public int compare(Deal lhs, Deal rhs) {
					return (int) (lhs.distance - rhs.distance);
				}
			});
			
			Utils.checkNew(newDeals, this);
		}
	}

}
