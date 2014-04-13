package com.hairforce.grouponalert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.hairforce.grouponalert.LocationReceiver.LocationListener;
import com.hairforce.grouponalert.data.Deal;

public class MainActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, OnItemClickListener, OnCheckedChangeListener, LocationListener {
	private LocationUpdater locationUpdater;
	private LocationClient locationClient;
	
	private ListView listView;
	
	private ProgressBar progressBar;
	
	private DealAdapter dealAdapter;
	
	private boolean start;
	
	private LocationReceiver locationReceiver;
	
	private Switch enabled;
	
	public static double FAKE_LAT = 40.7320215;
	public static double FAKE_LNG = -73.9963378;
	
	static final double SCALE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		locationReceiver = new LocationReceiver(this);
		
		setContentView(R.layout.activity_main);
		
		locationUpdater = new LocationUpdater(this, LocationService.class);
		
		dealAdapter = new DealAdapter(this);
		
		progressBar = new ProgressBar(this);
		progressBar.setIndeterminate(true);
		
		addContentView(progressBar, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		
		listView = (ListView) findViewById(R.id.ListView1);
		listView.setEmptyView(progressBar);
		listView.setAdapter(dealAdapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		enabled = (Switch) menu.getItem(1).getActionView().findViewById(R.id.switch1);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		enabled.setChecked(prefs.getBoolean("notifications", false));
		enabled.setOnCheckedChangeListener(this);
		
		return true;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(prefs.getBoolean("notifications", false))
			locationUpdater.start(2000);
		
		locationClient = new LocationClient(this, this, this);
		
		start = true;
		
		locationClient.connect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		unregisterReceiver(locationReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		registerReceiver(locationReceiver, new IntentFilter("com.hairforce.grouponalert.LOCATION"));
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
	}
	
	@Override
	public void onConnected(Bundle data) {
		final Location location = locationClient.getLastLocation();
		
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		
		if(start)
			prefs.edit().putFloat("startLat", (float) location.getLatitude()).putFloat("startLng", (float) location.getLongitude()).commit();
		
		start = false;
		
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		
		lat -= prefs.getFloat("startLat", 0);
		lng -= prefs.getFloat("startLng", 0);
		
		lat *= SCALE;
		lng *= SCALE;
		
		lat += MainActivity.FAKE_LAT;
		lng += MainActivity.FAKE_LNG;
		
		location.setLatitude(lat);
		location.setLongitude(lng);
		
		Intent intent= getIntent();
		
		if(intent.getExtras() != null) {
			final String[] ids = intent.getStringArrayExtra("deals");
			
			AsyncTask<Void, Void, List<Deal>> getDeals = new AsyncTask<Void, Void, List<Deal>>() {
				
				@Override
				protected List<Deal> doInBackground(Void... params) {
					List<Deal> deals = new ArrayList<Deal>();
					
					for(String id : ids)
						deals.add(Utils.getDeal(id, location));
					
					return deals;
				}
	
				@Override
				protected void onPostExecute(List<Deal> result) {
					Collections.sort(result, new Comparator<Deal>() {
						@Override
						public int compare(Deal lhs, Deal rhs) {
							return (int) (lhs.distance - rhs.distance);
						}
					});
					
					progressBar.setVisibility(View.GONE);
					
					dealAdapter.setData(result);
				}
			};
			
			getDeals.execute();
		}
		else {
			AsyncTask<Void, Void, List<Deal>> getLocal = new AsyncTask<Void, Void, List<Deal>>() {
	
				@Override
				protected List<Deal> doInBackground(Void... params) {
					return Utils.getDeals(location, 10000);
				}
	
				@Override
				protected void onPostExecute(List<Deal> result) {
					Collections.sort(result, new Comparator<Deal>() {
						@Override
						public int compare(Deal lhs, Deal rhs) {
							return (int) (lhs.distance - rhs.distance);
						}
					});
					
					progressBar.setVisibility(View.GONE);
					
					dealAdapter.setData(result);
				}
			};
			
			getLocal.execute();
		}
		
		//Toast.makeText(MainActivity.this, String.format("%f, %f", location.getLatitude(), location.getLongitude()), Toast.LENGTH_SHORT).show();
		
		locationClient.disconnect();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.groupon.com/dispatch/us/deal/" + dealAdapter.getItem(position).id));
		
		startActivity(intent);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		prefs.edit().putBoolean("notifications", isChecked).commit();
		
		if(isChecked)
			locationUpdater.start(2000);
		else
			locationUpdater.stop();
	}

	@Override
	public void onLocationChanged() {
		dealAdapter.setData(new ArrayList<Deal>());
		
		progressBar.setVisibility(View.VISIBLE);
		
		locationClient = new LocationClient(this, this, this);
		
		locationClient.connect();
	}
}
