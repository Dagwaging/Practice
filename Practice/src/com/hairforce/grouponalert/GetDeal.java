package com.hairforce.grouponalert;

import android.location.Location;
import android.os.AsyncTask;

/**
 * TODO Put here a description of what this class does.
 *
 * @author rosspa.
 *         Created Apr 12, 2014.
 */
public class GetDeal extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... arg0) {
		Location location = new Location("Fake");
		
		location.setLatitude(39.977384);
		location.setLongitude(-83.0127741);
		
		Utils.getDeals(location, 20);
		return null;
	}

}
