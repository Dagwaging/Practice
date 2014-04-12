package com.hairforce.grouponalert;

import java.util.Date;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

import com.google.android.gms.location.LocationClient;

public class LocationService extends IntentService {

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
			
			String name = String.format("%f, %f", lat, lng);
	
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	
			Notification notification = new Builder(this)
					.setSmallIcon(R.drawable.ic_launcher).setContentTitle(name)
					.setTicker(name).setWhen(new Date().getTime()).build();
	
			notificationManager.notify(0, notification);
		}
	}

}
