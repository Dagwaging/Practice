package com.hairforce.grouponalert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.hairforce.grouponalert.data.Deal;
import com.hairforce.grouponalert.data.Option;
import com.hairforce.grouponalert.data.RedemptionLocation;
import com.hairforce.grouponalert.data.getDealsData;

public class Utils {
	public static final String CLIENT_ID = "1D62CAE756A99D4783B724D2EFC4796F";
	
	static boolean servicesConnected(Context context) {
		return ConnectionResult.SUCCESS == GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
	}

	public static List<Deal> getDeals(Location location, int radius) {
		List<Deal> nearby = new ArrayList<Deal>();
		
		Gson gson = new Gson();
		
		try {
			URL url = new URL("http://api.groupon.com/v2/deals.json?client_id="
					+ CLIENT_ID + "&lat=" + location.getLatitude() + "&lng=" + location.getLongitude());
			BufferedReader in;
			try {
				in = new BufferedReader(new InputStreamReader(url.openStream()));
				
				getDealsData data = gson.fromJson(in, getDealsData.class);
				
				for(Deal deal : data.deals) {
					boolean isNear = false;
					
					for(Option option : deal.options) {
						for(RedemptionLocation redemptionLocation : option.redemptionLocations) {
							Location rLocation = new Location("Groupon");
							
							rLocation.setLatitude(redemptionLocation.lat);
							rLocation.setLongitude(redemptionLocation.lng);
							
							if(location.distanceTo(rLocation) < radius && !deal.isSoldOut) {
								isNear = true;
								
								break;
							}
						}
						
						if(isNear)
							break;
					}
					
					if(isNear)
						nearby.add(deal);
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

		return nearby;
	}
}
