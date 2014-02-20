package com.pivotallabs.yellowpages.activities;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import android.view.View;

import com.google.inject.Inject;
import com.pivotallabs.testing.FakeYellowPagesApi;
import com.pivotallabs.testing.RobolectricTestRunnerWithInjection;
import com.pivotallabs.yellowpages.api.YellowPagesApi;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class SearchActivityTest {

	@Inject FakeYellowPagesApi api;
	
	ActivityController<SearchActivity> controller;
	SearchActivity activity;
	
	// anything else
	
	@Before
	public void setup() {
		controller = Robolectric.buildActivity(SearchActivity.class);
		activity = controller.get();
	}
	
	@Test
	public void shouldShowLoadingSpinner_beforeResultsReturn(){
		api.setResponse(new YellowPagesApi.Response());
		
		controller.create().start().resume();
		
		// spinner should be showing
		
		Assert.assertEquals(View.VISIBLE, activity.progress.getVisibility());
		Assert.assertEquals(View.GONE, activity.list.getVisibility());
		Assert.assertEquals(View.GONE, activity.empty.getVisibility());		
	}
	
	@Test
	public void shouldCallTheYellowPagesAPI() {
		
	}
	
	@Test
	public void shouldDisplayItemsInListView() {
		
	}
	
}
