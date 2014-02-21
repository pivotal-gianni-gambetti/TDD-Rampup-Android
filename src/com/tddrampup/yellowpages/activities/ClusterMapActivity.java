package com.tddrampup.yellowpages.activities;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.inject.Inject;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ClusterMapActivity extends MapActivity implements Listener<Response>, ErrorListener{

	public static Intent getStartIntent(Context context, String what, String where){
		Intent intent = new Intent(context, ClusterMapActivity.class);
		
		intent.putExtra(MainActivity.WHAT_QUERY, what);
		intent.putExtra(MainActivity.WHERE_QUERY, where);
		
		return intent;
	}
	
	public static void start(Context context, String what, String where){
		Intent start = getStartIntent(context, what, where);
		context.startActivity(start);
	}

	@Inject
	RequestQueue queue;
	
	@Inject
	YellowPagesApi api;
	
	String what;
	String where;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		what = getIntent().getExtras().getString(MainActivity.WHAT_QUERY);
		where = getIntent().getExtras().getString(MainActivity.WHERE_QUERY);
		
		Request<Response> request = api.findBusiness(1, what, where, this, this);
		request.setTag(this);
		queue.add(request);
	}

	@Override
	protected void onStop() {

		queue.cancelAll(this);
		
		super.onStop();
	}
	
	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(Response response) {
		// TODO Auto-generated method stub
		
	}
	
}
