package com.pivotallabs.yellowpages.activities;

import java.util.ArrayList;

import roboguice.activity.RoboListActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.inject.Inject;
import com.pivotallabs.yellowpages.R;
import com.pivotallabs.yellowpages.api.YellowPagesApi;

@ContentView(R.layout.activity_search)
public class SearchActivity extends RoboListActivity {

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
	
	@Inject YellowPagesApi api;
	@InjectView(android.R.id.list) ListView list;
	@InjectView(android.R.id.empty) TextView empty;
	@InjectView(R.id.progress) ProgressBar progress;
	
	private ArrayList<String> items;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(this.getPackageName(), "onCreate");
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		items = new ArrayList<String>();
		items.add("Bam");
		items.add("Sam");
		items.add("Ram");
		items.add("Lam");
		items.add("Tam");
		items.add("Dam");
		items.add("Jam");
		
		Log.v(this.getPackageName(), "setListAdapter");
		setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_search_row, items));
		
		items.add("Test");
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		
		Log.v(this.getPackageName(), "onCreate FINISH");
	}
	
	@Override
	public void onContentChanged() {
		// custom progress bar handling
		
		if( /* TODO get adapter */ true ){
			ListAdapter adapter = null;
			
			if( adapter.getCount() > 0){
				progress.setVisibility(View.GONE);
			}
		}
		
		// delegate to super
		super.onContentChanged();
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
