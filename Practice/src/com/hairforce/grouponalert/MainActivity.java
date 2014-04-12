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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
	public static final String CLIENT_ID = "1D62CAE756A99D4783B724D2EFC4796F";
	private float radius;
	private GetDeal getDeal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
		GetDeal getDeal = new GetDeal();
		getDeal.execute();
		sendAlertToPebble();
	}

	private void sendAlertToPebble() {
		final Intent intent = new Intent(
				"com.getpebble.action.SEND_NOTIFICATION");

		final Map<String, String> data = new HashMap<String, String>();
		
		
		data.put("title", "This was a triumph");
		data.put("body", "I'm making a note here, huge success");

		final JSONObject jsonData = new JSONObject(data);
		final String notificationData = new JSONArray().put(jsonData)
				.toString();

		intent.putExtra("messageType", "PEBBLE_ALERT");
		intent.putExtra("sender", "Groupon Alert");
		intent.putExtra("notificationData", notificationData);

		sendBroadcast(intent);
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
