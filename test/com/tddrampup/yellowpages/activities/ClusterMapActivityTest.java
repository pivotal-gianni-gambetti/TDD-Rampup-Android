package com.tddrampup.yellowpages.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import android.content.Intent;

import com.tddrampup.testing.RobolectricTestRunnerWithInjection;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class ClusterMapActivityTest {

	ActivityController<ClusterMapActivity> controller;
	ClusterMapActivity activity;
	
	@Before
	public void setup(){
		Intent start = ClusterMapActivity.getStartIntent(Robolectric.application, "what", "where");

		controller = Robolectric.buildActivity(ClusterMapActivity.class).withIntent(start);
		
		activity = controller.get();
	}
	
	@Test
	public void shouldHaveAMapFragment(){
		controller.create().start().resume();		
	}
	
}

