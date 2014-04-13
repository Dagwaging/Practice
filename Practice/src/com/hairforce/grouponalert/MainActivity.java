package com.hairforce.grouponalert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.hairforce.grouponalert.data.Deal;

public class MainActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener {
	private ActivityUpdater activityUpdater;
	private LocationClient locationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.demo:
			AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
				
				@Override
				protected Void doInBackground(Void... params) {
					Location location = new Location("Fake");
					location.setLatitude(39.9698419286);
					location.setLongitude(-83.0096569857);
					
					List<Deal> newDeals = Utils.getDeals(location, 1000);
					
					Utils.checkNew(newDeals, MainActivity.this);
					return null;
				}
			};
			
			asyncTask.execute();
			
			break;
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			
			break;
		}
		
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		locationClient = new LocationClient(this, this, this);
		
		locationClient.connect();
		
		GetDeal getDeal = new GetDeal();
		getDeal.execute();
		//sendAlertToPebble();
		
		//openInGrouponApp();
	}

	private void sendAlertToPebble() {
		final Intent intent = new Intent("com.getpebble.action.SEND_NOTIFICATION");
		
		final Map<String, String> data = new HashMap<String, String>();
		
		data.put("title", "This was a triumph");
		data.put("body", "I'm making a note here, huge success");
		
		final JSONObject jsonData = new JSONObject(data);
		final String notificationData = new JSONArray().put(jsonData).toString();
		
		intent.putExtra("messageType", "PEBBLE_ALERT");
		intent.putExtra("sender", "Groupon Alert");
		intent.putExtra("notificationData", notificationData);
		
		sendBroadcast(intent);
	}
	
	private void openInGrouponApp() {
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://www.groupon.com/redirect/deals/seadog-cruises-13"));
		
		startActivity(intent);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
	}
	
	@Override
	public void onConnected(Bundle data) {
		
		Location location = locationClient.getLastLocation();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		prefs.edit().putFloat("startLat", (float) location.getLatitude()).putFloat("startLng", (float) location.getLongitude()).commit();
		
		Toast.makeText(MainActivity.this, String.format("%f, %f", location.getLatitude(), location.getLongitude()), Toast.LENGTH_SHORT).show();
		
		locationClient.disconnect();
	}
}
