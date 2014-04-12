package com.hairforce.grouponalert;

import java.util.Date;
import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

import com.google.android.gms.location.LocationClient;
import com.hairforce.grouponalert.data.Deal;

public class LocationService extends IntentService {

	public LocationService() {
		super("com.hairforce.grouponalert.LocationService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if(intent.getExtras() != null) {
			Bundle extras = intent.getExtras();
			
			Location location = extras.getParcelable(LocationClient.KEY_LOCATION_CHANGED);
			
			List<Deal> newDeals = Utils.getDeals(location, 1000);
			
			Utils.checkNew(newDeals, this);
		}
	}

}
