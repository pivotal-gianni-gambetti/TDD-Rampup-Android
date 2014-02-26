package com.tddrampup.yellowpages.activities;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.inject.Inject;
import com.tddrampup.R;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.FindBusinessResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;

@ContentView(R.layout.activity_search_near_me)
public class SearchNearMeActivity extends MapActivity implements
		Listener<FindBusinessResponse>, ErrorListener,
		OnMyLocationChangeListener, OnQueryTextListener {

	public static void start(Context context) {
		Intent start = new Intent(context, SearchNearMeActivity.class);
		context.startActivity(start);
	}

	@InjectView(R.id.search) SearchView search;
	@InjectView(R.id.progress) ProgressBar spinner;
	@Inject RequestQueue queue;
	@Inject YellowPagesApi api;

	LocationClient locationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setupActionBar();

		// locationClient = new LocationClient(this, this, this);

		map.setMyLocationEnabled(true);
		map.setOnMyLocationChangeListener(this);

		search.setOnQueryTextListener(this);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_near_me, menu);

		// MenuItem searchItem = menu.findItem(R.id.search);
		//
		// SearchView menuSearch = (SearchView) MenuItemCompat
		// .getActionView(searchItem);
		//
		// menuSearch.setIconifiedByDefault(false);
		// menuSearch.setSubmitButtonEnabled(true);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// locationClient.connect();
	}

	@Override
	protected void onPause() {
		// locationClient.disconnect();
		super.onPause();
	}

	void queryForThingsNearMe(String what) {
		Location approxLocation = map.getMyLocation();

		if (approxLocation == null) {
			return;
		}

		map.clear();
		showSpinner(true);
		Request<FindBusinessResponse> req = api.findBusiness(1, what,
				approxLocation, this, this);
		req.setTag(this);
		queue.add(req);
	}

	@Override
	protected void onStop() {
		queue.cancelAll(this);

		super.onStop();
	}

	private void showSpinner(boolean show) {
		if (show) {
			spinner.setVisibility(View.VISIBLE);
		} else {
			spinner.setVisibility(View.GONE);
		}
	}

	@Override
	public void onMyLocationChange(Location arg0) {
		// move it.
		map.animateCamera(cameraUpdater.centerAt(arg0.getLatitude(),
				arg0.getLongitude()));

		map.setOnMyLocationChangeListener(null);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		showSpinner(false);
	}

	@Override
	public void onResponse(FindBusinessResponse response) {
		showSpinner(false);
		for (Listing storeListing : response.listings) {

			if (storeListing.geoCode == null) {
				continue;
			}

			double latitude = Double.parseDouble(storeListing.geoCode.latitude);
			double longitude = Double
					.parseDouble(storeListing.geoCode.longitude);

			addMarker(latitude, longitude, storeListing.name,
					storeListing.address.street);

			Pair<String, String> key = Pair.<String, String> create(
					storeListing.name, storeListing.address.street);

			// listingsByTitleAndSnippet.put(key, storeListing);
		}

	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		Log.i(this.getClass().getName(), "QueryTextChange: " + arg0);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		Log.i(this.getClass().getName(), "QueryTextSubmit: " + arg0);
		search.clearFocus();
		queryForThingsNearMe(arg0);

		return true;
	}

}
