package com.hairforce.grouponalert;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityService extends IntentService {

	private LocationUpdater locationUpdater;
	
	public ActivityService() {
		super("com.hairforce.grouponalert.ActivityService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		locationUpdater = new LocationUpdater(this, LocationService.class);
		
		if (ActivityRecognitionResult.hasResult(intent)) {
			ActivityRecognitionResult result = ActivityRecognitionResult
					.extractResult(intent);

			DetectedActivity activity = result.getMostProbableActivity();

			int type = activity.getType();

			if (Utils.servicesConnected(this)) {
/*				String name;

				switch (type) {
				case DetectedActivity.IN_VEHICLE:
					name = "In vehicle";
					break;
				case DetectedActivity.ON_BICYCLE:
					name = "On bicycle";
					break;
				case DetectedActivity.ON_FOOT:
					name = "On foot";
					break;
				case DetectedActivity.STILL:
					name = "Still";
					break;
				case DetectedActivity.TILTING:
					name = "Tilting";
					break;
				default:
					name = "Unknown";
					break;
				}
*/

				if (type == DetectedActivity.ON_FOOT)
					locationUpdater.start(2000);
				else
					locationUpdater.stop();
			}
		}
	}

}
