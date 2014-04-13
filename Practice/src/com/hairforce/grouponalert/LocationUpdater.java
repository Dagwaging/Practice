package com.hairforce.grouponalert;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class LocationUpdater implements ConnectionCallbacks, OnConnectionFailedListener {
	private boolean inProgress;
	
	private boolean start;
	
	private LocationClient client;
	
	private PendingIntent pendingIntent;
	
	private Context context;

	private LocationRequest request;
	
	public LocationUpdater(Context context, Class<? extends Service> clazz) {
		this.context = context;
		
		client = new LocationClient(context, this, this);
		
		request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		Intent intent = new Intent(context, clazz);
		
		pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public void start(long interval) {
		start = true;
		
		request.setFastestInterval(interval);
		request.setInterval(interval);
		
		initiate();
	}
	
	public void stop() {
		start = false;
		
		initiate();
	}
	
	private void initiate() {
		if(!Utils.servicesConnected(context))
			return;
		
		if(!inProgress) {
			inProgress = true;
			
			client.connect();
		}
		else {
			client.disconnect();
			
			inProgress = false;
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		inProgress = false;
	}

	@Override
	public void onConnected(Bundle data) {
		if(start)
			client.requestLocationUpdates(request, pendingIntent);
		else
			client.removeLocationUpdates(pendingIntent);
		
		client.disconnect();
		
		inProgress = false;
	}

	@Override
	public void onDisconnected() {
		inProgress = false;
	}
}
