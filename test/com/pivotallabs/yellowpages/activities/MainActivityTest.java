package com.pivotallabs.yellowpages.activities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import android.content.Intent;
import android.os.Bundle;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

	ActivityController<MainActivity> controller;
	MainActivity activity;
	
	// anything else
	
	@Before
	public void setup() {
		controller = Robolectric.buildActivity(MainActivity.class);
		activity = controller.create().start().resume().get();
	}
	
    @Test
    public void shouldNotSearch_onButtonPress_withEmptyText(){
    	
    	// click the search button
    	activity.onClick(activity.search);
    	
    	// start the search activity
    	ShadowActivity shadowActivity = Robolectric.shadowOf_(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        Assert.assertNull(startedIntent);
    }
    
    @Test
    public void shouldPassSearchParamsToSearch_onSearchButtonClick(){
    	
    	String what = "what";
    	String where = "where";
    	
    	activity.whatField.setText(what);
    	activity.whereField.setText(where);
    	
    	// click the search button
    	activity.onClick(activity.search);
    	
    	// start the search activity
    	ShadowActivity shadowActivity = Robolectric.shadowOf_(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        
        Assert.assertNotNull(startedIntent);
        Assert.assertEquals(startedIntent.getComponent().getClassName(), SearchActivity.class.getName());
        
        Bundle extras = startedIntent.getExtras();
        Assert.assertEquals(what, extras.get(SearchActivity.WHAT_QUERY));
        Assert.assertEquals(where, extras.get(SearchActivity.WHERE_QUERY));
    }
}