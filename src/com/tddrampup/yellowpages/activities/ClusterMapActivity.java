package com.tddrampup.yellowpages.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.inject.Inject;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.FindBusinessResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;

public class ClusterMapActivity extends MapActivity implements Listener<FindBusinessResponse>, ErrorListener{

	public static Intent getStartIntent(Context context, String what, String where){
		Intent intent = new Intent(context, ClusterMapActivity.class);
		
		intent.putExtra(MainActivity.WHAT_QUERY, what);
		intent.putExtra(MainActivity.WHERE_QUERY, where);
		
		return intent;
	}
	
	public static void start(Context context, String what, String where){
		Intent start = getStartIntent(context, what, where);
		context.startActivity(start);
	}

	@Inject
	RequestQueue queue;
	
	@Inject
	YellowPagesApi api;
	
	String what;
	String where;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		what = getIntent().getExtras().getString(MainActivity.WHAT_QUERY);
		where = getIntent().getExtras().getString(MainActivity.WHERE_QUERY);
		
		Request<FindBusinessResponse> request = api.findBusiness(1, what, where, this, this);
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
		double averageLatitude = 0.0;
		double averageLongitude = 0.0;
		
		for (Listing iter : response.listings) {
			double latitude = Double.parseDouble(iter.geoCode.latitude);
			double longitude = Double.parseDouble(iter.geoCode.longitude);
			
			averageLatitude += latitude;
			averageLongitude += longitude;
			
			addMarker(latitude, longitude, iter.name);
		}
		
		averageLatitude = averageLatitude / response.listings.length;
		averageLongitude = averageLongitude / response.listings.length;
		
		Log.v(this.getClass().getName(), "Lat/Lng : " +averageLatitude+ " / " +averageLongitude+ " and length : " + response.listings.length);
		
		map.animateCamera(cameraUpdater.centerAt(averageLatitude, averageLongitude));
	}
	
}
