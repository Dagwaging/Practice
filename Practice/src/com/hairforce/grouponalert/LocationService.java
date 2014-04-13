package com.hairforce.grouponalert;

import java.util.Date;
import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.Builder;

import com.google.android.gms.location.LocationClient;
import com.hairforce.grouponalert.data.Deal;

public class LocationService extends IntentService {

	private static final double SCALE = 10;

	public LocationService() {
		super("com.hairforce.grouponalert.LocationService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(intent.getExtras() != null) {
			Bundle extras = intent.getExtras();
			
			Location location = extras.getParcelable(LocationClient.KEY_LOCATION_CHANGED);
			
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			
			lat -= prefs.getFloat("startLat", 0);
			lng -= prefs.getFloat("startLng", 0);
			
			lat *= SCALE;
			lng *= SCALE;
			
			lat += 40.722965;
			lng += -74.003893;
			
			location.setLatitude(lat);
			location.setLongitude(lng);
			
			List<Deal> newDeals = Utils.getDeals(location, 1000);
			
			if(newDeals.size() == 0) {
				NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				
				String title = String.format("%f, %f", lat, lng);
				
				Notification notification = new Builder(this)
						.setSmallIcon(R.drawable.ic_launcher).setContentTitle(title)
						.setTicker(title).setWhen(new Date().getTime()).build();
		
				notificationManager.notify(0, notification);
			}
			
			Utils.checkNew(newDeals, this);
		}
	}

}
