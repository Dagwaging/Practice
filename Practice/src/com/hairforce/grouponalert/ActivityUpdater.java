package com.hairforce.grouponalert;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognitionClient;

public class ActivityUpdater implements ConnectionCallbacks, OnConnectionFailedListener {
	private boolean inProgress;
	
	private boolean start;
	
	private ActivityRecognitionClient client;
	
	private PendingIntent pendingIntent;
	
	private Context context;

	private long interval;
	
	public ActivityUpdater(Context context, Class<? extends Service> clazz) {
		this.context = context;
		
		client = new ActivityRecognitionClient(context, this, this);
		
		Intent intent = new Intent(context, clazz);
		
		pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public void start(long interval) {
		start = true;
		
		this.interval = interval;
		
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
			client.requestActivityUpdates(interval, pendingIntent);
		else
			client.removeActivityUpdates(pendingIntent);
		
		client.disconnect();
		
		inProgress = false;
	}

	@Override
	public void onDisconnected() {
		inProgress = false;
	}
}
