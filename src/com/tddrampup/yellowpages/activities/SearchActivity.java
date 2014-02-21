package com.tddrampup.yellowpages.activities;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.inject.Inject;
import com.tddrampup.R;
import com.tddrampup.yellowpages.adapters.SearchResultAdapter;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;
import com.tddrampup.yellowpages.api.YellowPagesApi.Response;

@ContentView(R.layout.activity_search)
public class SearchActivity extends RoboListActivity implements Listener<Response>, ErrorListener {

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
	
	@Inject SearchResultAdapter adapter;
	@Inject RequestQueue queue;
	
	private String searchWhat;
	private String searchWhere;
	private int searchPage = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(this.getPackageName(), "onCreate");
		super.onCreate(savedInstanceState);
		
		searchWhat = getIntent().getExtras().getString(WHAT_QUERY);
		searchWhere = getIntent().getExtras().getString(WHERE_QUERY);

		setListAdapter(adapter);

		// by default, hide the empty view. only display 
		// it AFTER results have been returned
		empty.setVisibility(View.GONE);
		
		// Show the Up button in the action bar.
		//setupActionBar();
		
		ListView listView = getListView();
		listView.setTextFilterEnabled(false); // TODO enable keyboard filtering
		
		
		Log.v(this.getPackageName(), "onCreate FINISH");
		
		makeNextApiRequest();
	}
	
	@Override
	protected void onStop() {
		
		queue.cancelAll(this);
		
		super.onStop();
	}
	
	private void makeNextApiRequest(){
		Request<Response> resp = api.findBusiness(searchPage, searchWhat, searchWhere, this, this);
		searchPage++;
		resp.setTag(this);
		queue.add(resp);
	}
	
	@Override
	public void onContentChanged() {
		// custom progress bar handling
		
		if( adapter.getCount() > 0){
			progress.setVisibility(View.GONE);
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Listing item = adapter.getItem(position);
		
		MapActivity.start(this);
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

	@Override
	public void onErrorResponse(VolleyError error) {
		progress.setVisibility(View.GONE);
		adapter.notifyDataSetInvalidated();
		// TODO assuming that the adapter displays the empty message for us.
		empty.setText("An error occured while execuing: " + error.getMessage());
		
		Log(Log.ERROR, this.getClass().getName(), error);
	}

	@Override
	public void onResponse(Response response) {
		progress.setVisibility(View.GONE);			
		adapter.addAll(response.listings);
		adapter.notifyDataSetChanged();
	}

	public static void Log( int priority, String tag, VolleyError error){
		Log.println( priority, tag, "Error with volley query: " + error.networkResponse.statusCode);

		for(String key : error.networkResponse.headers.keySet()){
			Log.println( priority, tag, key + " : " + error.networkResponse.headers.get(key) );
		}

		Log.println( priority, tag, new String(error.networkResponse.data ) );
	}
	
}
