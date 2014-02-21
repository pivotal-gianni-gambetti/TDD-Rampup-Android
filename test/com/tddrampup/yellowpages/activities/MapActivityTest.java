package com.tddrampup.yellowpages.activities;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import roboguice.RoboGuice;

import com.google.inject.Inject;
import com.tddrampup.testing.RobolectricTestRunnerWithInjection;
import com.tddrampup.yellowpages.activities.MapActivity;
import com.tddrampup.yellowpages.injection.GoogleMapProvider;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class MapActivityTest {

	ActivityController<MapActivity> controller;
	MapActivity activity;
	
	@Before
	public void setup(){
		controller = Robolectric.buildActivity(MapActivity.class);
		
		activity = controller.get();
		
		RoboGuice.injectMembers(activity, activity);
	}
	
	@Test
	@Ignore
	public void shouldHaveAMapFragment(){
		
		controller.create().start().resume();
		
	}
	
}

