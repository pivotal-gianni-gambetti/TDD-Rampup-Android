package com.pivotallabs.yellowpages.activities;

import java.util.Collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import android.content.Intent;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.inject.Inject;
import com.pivotallabs.testing.RobolectricTestRunnerWithInjection;
import com.pivotallabs.yellowpages.api.YellowPagesApi.Listing;
import com.pivotallabs.yellowpages.api.YellowPagesApi.Response;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class SearchActivityTest {
	
	@Inject
	RequestQueue queue;
	
	
	ActivityController<SearchActivity> controller;
	SearchActivity activity;
	
	// anything else
	
	@Before
	public void setup() {
	
		Intent start = SearchActivity.getStartIntent(Robolectric.application, "What", "where");
		
		controller = Robolectric.buildActivity(SearchActivity.class).withIntent(start);
		activity = controller.get();
	}
	
	@Test
	public void shouldShowLoadingSpinner_beforeResultsReturn(){
		controller.create().start().resume();
		
		// spinner should be showing
		
		Assert.assertEquals(View.VISIBLE, activity.progress.getVisibility());
		Assert.assertEquals(View.GONE, activity.list.getVisibility());
		Assert.assertEquals(View.GONE, activity.empty.getVisibility());		
	}
	
	@Test
	public void shouldCallTheYellowPagesAPI() {
		
		controller.create().start().resume();
		
		Mockito.verify(queue).add(Mockito.<Request<?>>any());
	}
	
	@Test
	public void shouldDisplayItemsInListView() {
		
	}
	
	@Test
	public void shouldShowErrorMessage_whenReceivingNetworkRequestWithoutResults(){
		controller.create().start().resume();
		
		NetworkResponse response = new NetworkResponse(500, new byte[0], Collections.<String,String>emptyMap(), true);
		
		VolleyError error = new VolleyError(response);
		
		activity.onErrorResponse(error);
				
		Assert.assertEquals(View.GONE, activity.progress.getVisibility());
		Assert.assertEquals(View.GONE, activity.list.getVisibility());
		Assert.assertEquals(View.VISIBLE, activity.empty.getVisibility());		
	}

	@Test
	public void shouldNotShowErrorMessage_whenReceivingNetworkRequestAndHasResults(){
		controller.create().start().resume();
		
		// build response.
		Response resp = new Response();
		resp.listings = new Listing[1];
		resp.listings[0] = new Listing();
		
		// send the response
		activity.onResponse(resp);
		
		NetworkResponse response = new NetworkResponse(500, new byte[0], Collections.<String,String>emptyMap(), true);
		VolleyError error = new VolleyError(response);
		activity.onErrorResponse(error);
		
		Assert.assertEquals(View.GONE, activity.progress.getVisibility());
		Assert.assertEquals(View.VISIBLE, activity.list.getVisibility());
		Assert.assertEquals(View.GONE, activity.empty.getVisibility());		
	}
	
}
