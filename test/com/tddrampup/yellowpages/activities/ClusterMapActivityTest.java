package com.tddrampup.yellowpages.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.inject.Inject;
import com.tddrampup.testing.RobolectricTestRunnerWithInjection;
import com.tddrampup.yellowpages.api.YellowPagesApi.FindBusinessResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;
import com.tddrampup.yellowpages.ui.map.CameraUpdateWrapper;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class ClusterMapActivityTest {

	ActivityController<ClusterMapActivity> controller;
	ClusterMapActivity activity;

	double testLatitude = 43.672574;
	double testLongitude = -79.287992;
	Listing testListing;

	@Inject CameraUpdateWrapper cameraWrapper;
	@Inject RequestQueue queue;


	@Before
	public void setup(){
		Intent start = ClusterMapActivity.getStartIntent(Robolectric.application, "what", "where");

		controller = Robolectric.buildActivity(ClusterMapActivity.class).withIntent(start);

		activity = controller.get();

		testListing = new Listing();

		testListing.address.city = "Toronto";
		testListing.address.pcode = "M4E 1E9";
		testListing.address.prov = "ON";
		testListing.address.street = "King";

		testListing.distance = "1.0";
		testListing.geoCode.latitude = Double.toString(testLatitude);
		testListing.geoCode.longitude = Double.toString(testLongitude);

		testListing.id = "abc1123";
		testListing.isParent = false;
		testListing.parentId = "";
	}

	@Test
	public void shouldHaveAMapFragment(){
		controller.create().start().resume();		
	}

	@Test
	public void shouldProperlyCalculateAverageLatitudeAndLongitude_basedOnResponse(){
		controller.create().start().resume();

		Mockito.verify(queue).add( Mockito.<Request<FindBusinessResponse>>any() );

		FindBusinessResponse resp = new FindBusinessResponse();
		resp.listings = new Listing[1];
		resp.listings[0] = testListing;

		//activity.cameraUpdater;

		activity.onResponse(resp);

		Mockito.verify(cameraWrapper).centerAt(testLatitude, testLongitude);
	}

}

