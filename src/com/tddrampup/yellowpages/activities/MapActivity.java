package com.tddrampup.yellowpages.activities;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.inject.Inject;
import com.tddrampup.R;
import com.tddrampup.yellowpages.injection.GoogleMapProvider;

@ContentView(R.layout.activity_map)
public class MapActivity extends RoboActivity {

	public static Intent getStartIntent(Context context){
		Intent intent = new Intent(context, MapActivity.class);
		
		return intent;
	}
	
	public static void start(Context context){
		Intent start = getStartIntent(context);
		context.startActivity(start);
	}
	
	@Inject
	GoogleMapProvider mapProvider;
	// "injected"
	GoogleMap map;
	
	@Inject
	LocationManager locationManager;
	
	@Inject
	MapLocationListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// have to use weird injection to get this setup right.
		map = mapProvider.get();
		
		map.setMyLocationEnabled(true);
		map.setIndoorEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
	}
	
	@Override
	protected void onPause() {
		locationManager.removeUpdates(listener);
		
		super.onPause();
	}
	
	static class MapLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			Log.v(this.getClass().getName(), "Location changed: " + location.toString());
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
