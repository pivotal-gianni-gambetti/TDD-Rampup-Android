package com.tddrampup.yellowpages.activities;

import java.util.HashMap;
import java.util.Map;

import roboguice.inject.InjectExtra;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;
import com.google.inject.Inject;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.FindBusinessResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;

public class ClusterMapActivity extends MapActivity implements
		Listener<FindBusinessResponse>, ErrorListener,
		OnInfoWindowClickListener {

	public static Intent getStartIntent(Context context, String what,
			String where) {
		Intent intent = new Intent(context, ClusterMapActivity.class);

		intent.putExtra(MainActivity.WHAT_QUERY, what);
		intent.putExtra(MainActivity.WHERE_QUERY, where);

		return intent;
	}

	public static void start(Context context, String what, String where) {
		Intent start = getStartIntent(context, what, where);
		context.startActivity(start);
	}

	@Inject RequestQueue queue;

	@Inject YellowPagesApi api;

	@InjectExtra(value = MainActivity.WHAT_QUERY) String what;
	@InjectExtra(value = MainActivity.WHERE_QUERY) String where;

	Map<Pair<String, String>, Listing> listingsByTitleAndSnippet = new HashMap<Pair<String, String>, Listing>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		map.setOnInfoWindowClickListener(this);

		Request<FindBusinessResponse> request = api.findBusiness(1, what,
				where, this, this);
		request.setTag(this);
		queue.add(request);
	}

	@Override
	protected void onStop() {

		queue.cancelAll(this);

		super.onStop();
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponse(FindBusinessResponse response) {
		double minLatitude = 180, maxLatitude = -180;
		double minLongitude = 180, maxLongitude = -180;

		for (Listing storeListing : response.listings) {
			double latitude = Double.parseDouble(storeListing.geoCode.latitude);
			double longitude = Double
					.parseDouble(storeListing.geoCode.longitude);

			minLatitude = Math.min(minLatitude, latitude);
			maxLatitude = Math.max(maxLatitude, latitude);
			maxLongitude = Math.max(maxLongitude, longitude);
			minLongitude = Math.min(minLongitude, longitude);

			addMarker(latitude, longitude, storeListing.name,
					storeListing.address.street);

			Pair<String, String> key = Pair.<String, String> create(
					storeListing.name, storeListing.address.street);

			listingsByTitleAndSnippet.put(key, storeListing);
		}

		map.animateCamera(cameraUpdater.bounds(maxLatitude, maxLongitude,
				minLatitude, minLongitude));
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		Pair<String, String> key = Pair.<String, String> create(
				arg0.getTitle(), arg0.getSnippet());

		Listing listing = listingsByTitleAndSnippet.get(key);

		StoreDetailsActivity.start(this, listing);
	}

}
