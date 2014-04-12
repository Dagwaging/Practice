package com.hairforce.grouponalert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private Button start;
	private Button stop;
	private ActivityUpdater activityUpdater;
	public static final String CLIENT_ID = "1D62CAE756A99D4783B724D2EFC4796F";
	private float radius;
	private GetDeal getDeal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		start = (Button) findViewById(R.id.button1);
		stop = (Button) findViewById(R.id.button2);
		
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		
		activityUpdater = new ActivityUpdater(this, ActivityService.class);
	}

	@Override
	protected void onResume() {
		super.onResume();
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
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button1:
			activityUpdater.start(2000);
			
			break;
		case R.id.button2:
			activityUpdater.stop();
			
			break;
		}
	}
	
		public static void getDeals(double longitude, double latitude, float radius) {
		try {
			URL url = new URL("http://api.groupon.com/v2/deals.json?client_id="
					+ CLIENT_ID + "&postal_code=95120");
			BufferedReader in;
			try {
				in = new BufferedReader(
						new InputStreamReader(url.openStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null)
					Log.d("Words", inputLine);
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
