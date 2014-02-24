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
import com.google.inject.Inject;
import com.tddrampup.testing.RobolectricTestRunnerWithInjection;
import com.tddrampup.toolbox.Util;
import com.tddrampup.yellowpages.api.YellowPagesApi.BusinessDetailsResponse;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class StoreDetailsActivityTest {

	Intent start;
	ActivityController<StoreDetailsActivity> controller;
	StoreDetailsActivity activity;

	
	@Inject RequestQueue queue;
	
	@Before
	public void setup(){
		start = StoreDetailsActivity.getStartIntent(Robolectric.application, "i d", "A name 123", "City@Place", "ab(._.)cd");

		controller = Robolectric.buildActivity(StoreDetailsActivity.class).withIntent(start);

		activity = controller.get();
	}
	
	@Test
	public void shouldHaveSeoNormalizedParams(){
		
		Set<String> params = new HashSet<String>();
		params.add(StoreDetailsActivity.ID_PARAM);
		params.add(StoreDetailsActivity.NAME_PARAM);
		params.add(StoreDetailsActivity.CITY_PARAM);
		params.add(StoreDetailsActivity.PROV_PARAM);
		
		Bundle extras = start.getExtras();
		
		for(String key : params){
			Assert.assertTrue(extras.containsKey(key));
			
			String param = extras.getString(key);
			Assert.assertEquals(Util.seoNormalize(param), param);
		}
	}
	
	@Test
	public void shouldRequestBusinessDetails_onCreate(){
		controller.create();
		
		Mockito.verify(queue).add(Mockito.<Request<BusinessDetailsResponse>>any());
	}
	
	@Test
	public void shouldDisplayError_whenVolleyErrorIsReturned(){
		
		controller.create().start().resume();
		
		String message = "A volley error message";
		
		VolleyError error = new VolleyError(message);
		
		activity.onErrorResponse(error);
		
		Assert.assertEquals(message, activity.errorMessage.getText());
		Assert.assertEquals(View.VISIBLE, activity.errorMessage.getVisibility());		
	}
	
	@Test
	public void shouldPopulateFields_whenVolleyRequestSucceeds() {
		controller.create().start().resume();
		
		BusinessDetailsResponse response = new BusinessDetailsResponse();
		response.name = "Test Name";
		response.address.street = "123 test street";
		response.address.city = "Toronto";
		response.address.prov = "ON";
		
		activity.onResponse(response);
		
		Assert.assertEquals(response.name, activity.name.getText());
		Assert.assertEquals(response.address.street, activity.streetAddress.getText());
		Assert.assertTrue(activity.cityAndProvince.getText().toString().contains(response.address.city));
		Assert.assertTrue(activity.cityAndProvince.getText().toString().contains(response.address.prov));
	}
}
