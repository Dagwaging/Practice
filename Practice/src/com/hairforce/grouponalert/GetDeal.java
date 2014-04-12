package com.hairforce.grouponalert;

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
		// TODO Auto-generated method stub.
		Utils.getDeals(5.0, 5.0, (float) 5.0);
		return null;
	}

}
