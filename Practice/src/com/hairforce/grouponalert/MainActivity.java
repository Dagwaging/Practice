package com.hairforce.grouponalert;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private Button start;
	private Button stop;
	private ActivityUpdater activityUpdater;
	
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
}
