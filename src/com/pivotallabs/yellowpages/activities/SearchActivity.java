package com.pivotallabs.yellowpages.activities;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.pivotallabs.yellowpages.R;

@ContentView(R.layout.activity_search)
public class SearchActivity extends RoboActivity {

	static final String WHAT_QUERY = "what";
	static final String WHERE_QUERY = "where";
	
	public static Intent getStartIntent(Context context, String what, String where){
		Intent start = new Intent(context, SearchActivity.class);
		
		// params for the class
		start.putExtra(WHAT_QUERY, what);
		start.putExtra(WHERE_QUERY, where);
		
		return start;
	}
	
	public static void start(Context context, String what, String where){
		Intent start = getStartIntent(context, what, where);
		context.startActivity(start);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		//setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	//@SuppressLint("NewApi")
	/*
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
