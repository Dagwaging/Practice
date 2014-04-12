package com.hairforce.grouponalert;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		sendAlertToPebble();
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
}
