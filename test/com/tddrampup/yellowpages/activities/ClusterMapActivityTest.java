package com.tddrampup.yellowpages.activities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;
import com.google.inject.Inject;
import com.tddrampup.testing.RobolectricTestRunnerWithInjection;
import com.tddrampup.toolbox.Util;
import com.tddrampup.yellowpages.activities.MapActivity.MapProvider;
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
	@Inject MapProvider mapProvider;

	@Before
	public void setup() {
		Intent start = ClusterMapActivity.getStartIntent(
				Robolectric.application, "what", "where");

		controller = Robolectric.buildActivity(ClusterMapActivity.class)
				.withIntent(start);

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
		testListing.name = "test store";
		testListing.isParent = false;
		testListing.parentId = "";
	}

	@Test
	public void shouldHaveAMapFragment() {
		controller.create().start().resume();
	}

	@Test
	public void shouldBoundASinglePointCorrectly_OnResponse() {
		controller.create().start().resume();

		Mockito.verify(queue)
				.add(Mockito.<Request<FindBusinessResponse>> any());

		FindBusinessResponse resp = new FindBusinessResponse();
		resp.listings = new Listing[1];
		resp.listings[0] = testListing;

		activity.onResponse(resp);

		Mockito.verify(cameraWrapper).bounds(testLatitude, testLongitude,
				testLatitude, testLongitude);
	}

	@Test
	public void shouldBoundAllListings() {
		controller.create().start().resume();

		Mockito.verify(queue)
				.add(Mockito.<Request<FindBusinessResponse>> any());

		FindBusinessResponse resp = new FindBusinessResponse();
		resp.listings = new Listing[4];

		int index = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				testListing.geoCode.latitude = Double.toString(i * 2 - 1);
				testListing.geoCode.longitude = Double.toString(j * 2 - 1);
				resp.listings[index] = testListing.clone();
				index++;
			}
		}

		resp.listings[0] = testListing;

		activity.onResponse(resp);

		Mockito.verify(cameraWrapper).bounds(1, 1, -1, -1);

		GoogleMap map = mapProvider.get(activity);

		Mockito.verify(map).animateCamera(Mockito.<CameraUpdate> any());
	}

	@Test
	public void shouldOpenStoreDetails_onInfoWindowClick() {

		controller.create().start().resume();

		FindBusinessResponse resp = new FindBusinessResponse();
		resp.listings = new Listing[1];
		resp.listings[0] = testListing;

		// activity.cameraUpdater;

		activity.onResponse(resp);

		Marker mockMarker = Mockito.mock(Marker.class);

		Mockito.stub(mockMarker.getTitle()).toReturn(testListing.name);
		Mockito.stub(mockMarker.getSnippet()).toReturn(
				testListing.address.street);

		activity.onInfoWindowClick(mockMarker);

		// start the search activity
		ShadowActivity shadowActivity = Robolectric.shadowOf_(activity);
		Intent startedIntent = shadowActivity.getNextStartedActivity();

		Assert.assertNotNull(startedIntent);
		Assert.assertEquals(startedIntent.getComponent().getClassName(),
				StoreDetailsActivity.class.getName());

		Bundle extras = startedIntent.getExtras();

		Assert.assertEquals(Util.seoNormalize(testListing.name),
				extras.get(StoreDetailsActivity.NAME_PARAM));
		Assert.assertEquals(Util.seoNormalize(testListing.id),
				extras.get(StoreDetailsActivity.ID_PARAM));
		Assert.assertEquals(Util.seoNormalize(testListing.address.city),
				extras.get(StoreDetailsActivity.CITY_PARAM));
		Assert.assertEquals(Util.seoNormalize(testListing.address.prov),
				extras.get(StoreDetailsActivity.PROV_PARAM));
	}

	@Test
	public void shouldAttachSomethingToMap_onInfoWindowClickListener() {

		controller.create().start().resume();

		GoogleMap map = mapProvider.get(activity);

		Mockito.verify(map).setOnInfoWindowClickListener(
				Mockito.<OnInfoWindowClickListener> any());

	}

	@Test
	public void shouldNotCrash_withoutGeoCode() {
		controller.create().start().resume();

		FindBusinessResponse resp = new FindBusinessResponse();
		resp.listings = new Listing[1];
		resp.listings[0] = testListing;
		testListing.geoCode = null;

		activity.onResponse(resp);
	}

}
