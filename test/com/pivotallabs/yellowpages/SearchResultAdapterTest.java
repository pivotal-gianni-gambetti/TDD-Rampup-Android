package com.pivotallabs.yellowpages;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowApplication;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pivotallabs.testing.RobolectricTestRunnerWithInjection;
import com.pivotallabs.yellowpages.adapters.SearchResultAdapter;
import com.pivotallabs.yellowpages.api.YellowPagesApi.Listing;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class SearchResultAdapterTest {

	
	
	SearchResultAdapter adapter;
	
	@Before
	public void setup(){
		ShadowApplication app = Robolectric.getShadowApplication();
		
		Context context = app.getApplicationContext();
		
		adapter = new SearchResultAdapter(context);
	}
	
	@Test
	public void shouldPopulateBusinessName(){
		
		Listing data = new Listing();
		data.name = "A name to test with.";
		
		adapter.add(data);
		
		View populatedView = adapter.getView(0, null, null);
		
		TextView name = (TextView) populatedView.findViewById(R.id.business_title);
		
		Assert.assertEquals(data.name, name.getText());
	}

}
