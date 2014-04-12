package com.hairforce.grouponalert;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Utils {
	static boolean servicesConnected(Context context) {
		return ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
	}
}
