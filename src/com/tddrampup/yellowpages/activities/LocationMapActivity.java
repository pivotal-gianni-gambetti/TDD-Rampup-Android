package com.tddrampup.yellowpages.activities;

import android.content.Context;
import android.content.Intent;

public class LocationMapActivity extends MapActivity {

	static final String LAT_PARAM = "latitude";
	static final String LON_PARAM = "longitude";

	public static Intent getStartIntent(Context context, double latitude,
			double longitude) {
		Intent intent = new Intent(context, LocationMapActivity.class);

		intent.putExtra(LAT_PARAM, latitude);
		intent.putExtra(LON_PARAM, longitude);

		return intent;
	}

	public static void start(Context context, double latitude, double longitude) {
		Intent start = getStartIntent(context, latitude, longitude);
		context.startActivity(start);
	}

	@Override
	protected void onResume() {
		super.onResume();

		double latitude = getIntent().getDoubleExtra(LAT_PARAM, 0);
		double longitude = getIntent().getDoubleExtra(LON_PARAM, 0);

		addMarker(latitude, longitude);
		map.moveCamera(cameraUpdater.centerAt(latitude, longitude));
	}

}
