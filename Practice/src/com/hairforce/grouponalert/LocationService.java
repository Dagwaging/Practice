package com.hairforce.grouponalert;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

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
