package com.hairforce.grouponalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationReceiver extends BroadcastReceiver {
	private LocationListener listener;

	public LocationReceiver(LocationListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		listener.onLocationChanged();
	}

	public static interface LocationListener {
		public void onLocationChanged();
	}
}
