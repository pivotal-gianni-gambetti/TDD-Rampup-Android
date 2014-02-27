package com.tddrampup.yellowpages.activities;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import android.location.Location;

import com.android.volley.RequestQueue;
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
public class MapActivityTest {

	@Inject RequestQueue queue;
	@Inject MapProvider mapProvider;
	@Inject CameraUpdateWrapper cameraUpdater;
	@Inject YellowPagesApi api;
	GoogleMap map;

	MapActivity activity;

	Listing testListing;
	Location testLocation;
	FindBusinessResponse testResponse;

	double lat = 44.0, lon = 73.0;
	String title = "title";
	String snippet = "snippet";

	@Before
	public void setup() {

		activity = Robolectric.buildActivity(MapActivity.class).create()
				.start().resume().get();

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
	public void shouldProperlyAddMapMarkers_withOnlyLatLon() {

		activity.addMarker(lat, lon);
		checkArgs(lat, lon, null, null);

	}

	@Test
	public void shouldProperlyAddMapMarkers_withLatLonAndTitle() {
		activity.addMarker(lat, lon, title);
		checkArgs(lat, lon, title, null);
	}

	@Test
	public void shouldProperlyAddMapMarkers_withLatLonTitleAndSnippet() {
		activity.addMarker(lat, lon, title, snippet);
		checkArgs(lat, lon, title, snippet);
	}

	private void checkArgs(double lat, double lon, String title, String snip) {

		ArgumentCaptor<MarkerOptions> argument = ArgumentCaptor
				.forClass(MarkerOptions.class);

		Mockito.verify(map).addMarker(argument.capture());
		Assert.assertEquals(lat, argument.getValue().getPosition().latitude,
				0.0);
		Assert.assertEquals(lon, argument.getValue().getPosition().longitude,
				0.0);
		Assert.assertEquals(title, argument.getValue().getTitle());
		Assert.assertEquals(snip, argument.getValue().getSnippet());
	}

}
