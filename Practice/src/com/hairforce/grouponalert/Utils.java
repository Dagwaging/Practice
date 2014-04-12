package com.hairforce.grouponalert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.hairforce.grouponalert.data.Deal;
import com.hairforce.grouponalert.data.getDealsData;

public class Utils {
	public static final String CLIENT_ID = "1D62CAE756A99D4783B724D2EFC4796F";
	
	static boolean servicesConnected(Context context) {
		return ConnectionResult.SUCCESS == GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
	}

	public static void getDeals(double longitude, double latitude, float radius) {
		Gson gson = new Gson();
		
		try {
			URL url = new URL("http://api.groupon.com/v2/deals.json?client_id="
					+ CLIENT_ID + "&postal_code=95120");
			BufferedReader in;
			try {
				in = new BufferedReader(new InputStreamReader(url.openStream()));
				
				getDealsData data = gson.fromJson(in, getDealsData.class);
				
				for(Deal deal : data.deals) {
					Log.d("Deal", deal.announcementTitle);
				}
				
				in.close();
			} catch (IOException exception) {
				// TODO Auto-generated catch-block stub.
				exception.printStackTrace();
			}
		} catch (MalformedURLException exception) {
			// TODO Auto-generated catch-block stub.
			exception.printStackTrace();
		}

	}
}
