package com.tddrampup.yellowpages.activities;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;
import com.tddrampup.R;
import com.tddrampup.yellowpages.ui.map.CameraUpdateWrapper;

@ContentView(R.layout.activity_map)
public class MapActivity extends RoboActivity {

	public interface MapProvider {

		GoogleMap get(MapActivity activity);

	}

	// I'm so sorry that we had to do this with the dependency
	// injection framework, unfortunately, we're not so sure why
	// we had to do this sort of stupid thing...but google maps
	// just doesn't want to be setup during onCreate. :/
	@Inject MapProvider provider;

	GoogleMap map;

	@Inject CameraUpdateWrapper cameraUpdater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		map = provider.get(this);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		// set the default zoom level
		float reasonableZoom = (map.getMaxZoomLevel() - map.getMinZoomLevel())
				* 2 / 3 + map.getMinZoomLevel();
		map.moveCamera(cameraUpdater.zoomTo(reasonableZoom));
	}

	protected void addMarker(double latitude, double longitude) {
		addMarker(latitude, longitude, null);
	}

	protected void addMarker(double latitude, double longitude, String title) {
		addMarker(latitude, longitude, title, null);
	}

	protected void addMarker(double latitude, double longitude, String title,
			String snippet) {

		MarkerOptions marker = new MarkerOptions().position(new LatLng(
				latitude, longitude));

		if (title != null) {
			marker.title(title);
		}

		if (snippet != null) {
			marker.snippet(snippet);
		}

		map.addMarker(marker);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
