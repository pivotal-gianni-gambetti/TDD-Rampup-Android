package com.tddrampup.yellowpages.activities;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import android.content.Intent;
import android.location.Location;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;
import com.tddrampup.testing.RobolectricTestRunnerWithInjection;
import com.tddrampup.yellowpages.activities.MapActivity.MapProvider;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.FindBusinessResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;
import com.tddrampup.yellowpages.ui.map.CameraUpdateWrapper;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class SearchNearMeActivityTest {

	@Inject RequestQueue queue;
	@Inject MapProvider mapProvider;
	@Inject CameraUpdateWrapper cameraUpdater;
	@Inject YellowPagesApi api;
	GoogleMap map;

	ActivityController<SearchNearMeActivity> controller;
	SearchNearMeActivity activity;

	Listing testListing;
	Location testLocation;
	FindBusinessResponse testResponse;

	// anything else

	@Before
	public void setup() {

		Intent start = SearchNearMeActivity
				.getStartIntent(Robolectric.application);

		controller = Robolectric.buildActivity(SearchNearMeActivity.class)
				.withIntent(start);
		activity = controller.create().start().resume().get();

		testListing = new Listing();
		testListing.address.city = "Toronto";
		testListing.address.pcode = "M4E 1E9";
		testListing.address.prov = "ON";
		testListing.address.street = "King";
		testListing.distance = "1.0";
		testListing.geoCode.latitude = "43.672574";
		testListing.geoCode.longitude = "-79.287992";
		testListing.id = "abc1123";
		testListing.name = "Test Name";
		testListing.isParent = false;
		testListing.parentId = "";

		testLocation = new Location("");
		testLocation.setLatitude(23.23);
		testLocation.setLongitude(44.44);

		testResponse = new FindBusinessResponse();
		testResponse.listings = new Listing[] { testListing };

		map = mapProvider.get(activity);

		Mockito.when(map.getMyLocation()).thenReturn(testLocation);
	}

	@Test
	public void setupListeners_onCreationOfActivity() {
		Mockito.verify(activity.map).setMyLocationEnabled(true);
		Mockito.verify(activity.map).setOnMyLocationChangeListener(activity);
	}

	@Test
	public void shouldMakeAWebRequest_onSearchSubmit() {
		activity.onQueryTextSubmit("test search");
		Mockito.verify(map).clear();
		Assert.assertEquals(View.VISIBLE, activity.spinner.getVisibility());
		Mockito.verify(api).findBusiness(1, "test search", testLocation,
				activity, activity);
		Mockito.verify(queue)
				.add(Mockito.<Request<FindBusinessResponse>> any());
	}

	@Test
	public void shouldHideLoadingIndicator_onWebRequestFailure() {
		activity.onErrorResponse(Mockito.mock(VolleyError.class));
		Assert.assertEquals(View.GONE, activity.spinner.getVisibility());
	}

	@Test
	public void shouldAddMarkersForLocations_onSuccessfulWebRequest() {
		activity.onResponse(testResponse);
		Assert.assertEquals(View.GONE, activity.spinner.getVisibility());
		Mockito.verify(map).addMarker(Mockito.<MarkerOptions> any());
	}

	@Test
	public void shouldMoveCamera_onLocationChange() {
		activity.onMyLocationChange(testLocation);

		Mockito.verify(cameraUpdater).centerAt(testLocation.getLatitude(),
				testLocation.getLongitude());

		Mockito.verify(map, Mockito.atLeastOnce()).moveCamera(
				Mockito.<CameraUpdate> any());
	}
}
