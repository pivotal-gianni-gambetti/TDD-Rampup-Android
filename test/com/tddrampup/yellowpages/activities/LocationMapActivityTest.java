package com.tddrampup.yellowpages.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import android.content.Intent;

import com.tddrampup.testing.RobolectricTestRunnerWithInjection;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class LocationMapActivityTest {

	ActivityController<LocationMapActivity> controller;
	LocationMapActivity activity;
	
	@Before
	public void setup(){
		controller = Robolectric.buildActivity(LocationMapActivity.class);
		
		activity = controller.get();
	}
	
	@Test
	public void shouldHaveAMapFragment(){
		controller.create().start().resume();		
	}

	@Test
	public void shouldHaveASinglePoint_whenStartedWithIntent(){
		Intent start = LocationMapActivity.getStartIntent(Robolectric.application, 0.0, 0.0);
		
		controller.withIntent(start);
		
		controller.create().start().resume();
	}
	
}

