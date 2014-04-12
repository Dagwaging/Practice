package com.hairforce.grouponalert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v4.app.NotificationCompat.Builder;

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
	
	public static void checkNew(List<Deal> current, Context context) {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		List<Deal> seen = Deal.getAll(db);
		
		for(Deal deal : current) {
			if(seen.contains(deal))
				deal.update(db);
			else {
				deal.insert(db);
				
				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				
				Notification notification = new Builder(context)
						.setSmallIcon(R.drawable.ic_launcher).setContentTitle(deal.announcementTitle)
						.setTicker(deal.announcementTitle).setContentText(deal.title).setWhen(new Date().getTime()).build();
		
				notificationManager.notify(Integer.parseInt(deal.id), notification);
			}
		}
		
		seen = Deal.getAll(db);
		
		for(Deal deal : seen) {
			if(new Date().getTime() - deal.lastSeen > 1 * 60 * 60 * 1000) {
				deal.delete(db);
			}
		}
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
