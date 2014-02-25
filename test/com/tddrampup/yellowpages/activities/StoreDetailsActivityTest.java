package com.tddrampup.yellowpages.activities;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;
import com.tddrampup.testing.RobolectricTestRunnerWithInjection;
import com.tddrampup.toolbox.Util;
import com.tddrampup.yellowpages.activities.MapActivity.MapProvider;
import com.tddrampup.yellowpages.api.YellowPagesApi.BusinessDetailsResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.PhoneInformation;
import com.tddrampup.yellowpages.ui.map.CameraUpdateWrapper;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class StoreDetailsActivityTest {

	Intent start;
	ActivityController<StoreDetailsActivity> controller;
	StoreDetailsActivity activity;

	@Inject RequestQueue queue;
	@Inject MapProvider mapsProvider;
	@Inject CameraUpdateWrapper cameraUpdater;

	BusinessDetailsResponse response;

	@Before
	public void setup() {
		response = new BusinessDetailsResponse();
		response.name = "Test Name";
		response.address.street = "123 test street";
		response.address.city = "Toronto";
		response.address.prov = "ON";
		response.phones = new PhoneInformation[] { new PhoneInformation(),
				new PhoneInformation() };
		response.phones[0].displayNumber = "1-888-888-8888";
		response.phones[0].type = "primary";
		response.phones[1].displayNumber = "1-999-999-9999";
		response.phones[1].type = "secondary";
		response.geoCode.latitude = "23.77";
		response.geoCode.longitude = "69.98";

		start = StoreDetailsActivity.getStartIntent(Robolectric.application,
				"i d", "A name 123", "City@Place", "ab(._.)cd");

		controller = Robolectric.buildActivity(StoreDetailsActivity.class)
				.withIntent(start);

		activity = controller.get();
	}

	@Test
	public void shouldHaveSeoNormalizedParams() {

		Set<String> params = new HashSet<String>();
		params.add(StoreDetailsActivity.ID_PARAM);
		params.add(StoreDetailsActivity.NAME_PARAM);
		params.add(StoreDetailsActivity.CITY_PARAM);
		params.add(StoreDetailsActivity.PROV_PARAM);

		Bundle extras = start.getExtras();

		for (String key : params) {
			Assert.assertTrue(extras.containsKey(key));

			String param = extras.getString(key);
			Assert.assertEquals(Util.seoNormalize(param), param);
		}
	}

	@Test
	public void shouldRequestBusinessDetails_onCreate() {
		controller.create();

		Mockito.verify(queue).add(
				Mockito.<Request<BusinessDetailsResponse>> any());
	}

	@Test
	public void shouldHideLoadingSpinner_onErrorResponse() {
		controller.create().start().resume();

		String message = "A volley error message";

		VolleyError error = new VolleyError(message);

		activity.onErrorResponse(error);

		Assert.assertEquals(View.GONE, activity.loadingSpinner.getVisibility());
	}

	@Test
	public void shouldHideLoadingSpinner_onResponse() {
		controller.create().start().resume();

		activity.onResponse(response);

		Assert.assertEquals(View.GONE, activity.loadingSpinner.getVisibility());
	}

	@Test
	public void shouldNotShowContent_beforeErrorResponseReturned() {

		controller.create().start().resume();

		String message = "A volley error message";

		VolleyError error = new VolleyError(message);

		Assert.assertEquals(View.GONE,
				activity.detailsContainer.getVisibility());

		activity.onErrorResponse(error);

		Assert.assertEquals(View.GONE,
				activity.detailsContainer.getVisibility());
		Assert.assertEquals(View.VISIBLE, activity.errorMessage.getVisibility());
	}

	@Test
	public void shouldNotShowContent_beforeResponseReturned() {
		controller.create().start().resume();

		Assert.assertEquals(View.GONE,
				activity.detailsContainer.getVisibility());

		activity.onResponse(response);

		Assert.assertEquals(View.GONE, activity.loadingSpinner.getVisibility());
		Assert.assertEquals(View.VISIBLE,
				activity.detailsContainer.getVisibility());
	}

	@Test
	public void shouldDisplayError_whenVolleyErrorIsReturned() {

		controller.create().start().resume();

		String message = "A volley error message";

		VolleyError error = new VolleyError(message);

		activity.onErrorResponse(error);

		Assert.assertEquals(View.VISIBLE, activity.errorMessage.getVisibility());
	}

	@Test
	public void shouldPopulateFields_whenVolleyRequestSucceeds() {
		controller.create().start().resume();

		activity.onResponse(response);

		Assert.assertEquals(response.name, activity.name.getText());
		Assert.assertEquals(response.address.street,
				activity.streetAddress.getText());
		Assert.assertTrue(activity.cityAndProvince.getText().toString()
				.contains(response.address.city));
		Assert.assertTrue(activity.cityAndProvince.getText().toString()
				.contains(response.address.prov));
	}

	@Test
	public void shouldShowPrimaryPhoneNumber() {
		controller.create().start().resume();

		activity.onResponse(response);

		Assert.assertEquals(response.phones[0].displayNumber,
				activity.phoneNumber.getText().toString());
	}

	@Test
	public void shouldShowFirstPhoneNumber_whenThereIsNoPrimary() {
		controller.create().start().resume();

		response.phones = new PhoneInformation[] { new PhoneInformation(),
				new PhoneInformation() };
		response.phones[0].displayNumber = "1-888-888-8888";
		response.phones[0].type = "tertiary";
		response.phones[1].displayNumber = "1-999-999-9999";
		response.phones[1].type = "secondary";

		activity.onResponse(response);

		Assert.assertEquals(response.phones[0].displayNumber,
				activity.phoneNumber.getText().toString());
	}

	@Test
	public void shouldCenterMap_onStoreLocation_andAddStoreMarker() {

		controller.create().start().resume();

		activity.onResponse(response);

		LatLng latLng = new LatLng(
				Double.parseDouble(response.geoCode.latitude),
				Double.parseDouble(response.geoCode.longitude));

		GoogleMap map = mapsProvider.get(activity);

		Mockito.verify(map).addMarker(Mockito.<MarkerOptions> any());
		// indirectly verify that the map has been moved to these coordinates
		Mockito.verify(cameraUpdater).centerAt(latLng.latitude,
				latLng.longitude);
	}

	@Test
	public void shouldMoveCameraToStoreLocation() {
		controller.create().start().resume();
		activity.onResponse(response);
		activity.onMyLocationButtonClick();

		LatLng latLng = response.geoCode.convertToLatLng();
		GoogleMap map = mapsProvider.get(activity);

		Mockito.verify(cameraUpdater, Mockito.times(2)).centerAt(
				latLng.latitude, latLng.longitude);
		Mockito.verify(map).animateCamera(Mockito.<CameraUpdate> any());
	}

	@Test
	public void shouldNotCrash_withoutGeoCode() {
		controller.create().start().resume();

		response.geoCode = null;

		activity.onResponse(response);
	}
}
