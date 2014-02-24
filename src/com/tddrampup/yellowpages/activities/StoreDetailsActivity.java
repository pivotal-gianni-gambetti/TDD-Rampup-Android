package com.tddrampup.yellowpages.activities;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.inject.Inject;
import com.tddrampup.R;
import com.tddrampup.toolbox.Util;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.BusinessDetailsResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.FindBusinessResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;

@ContentView(R.layout.activity_store_details)
public class StoreDetailsActivity extends RoboActivity implements Listener<BusinessDetailsResponse>, ErrorListener {


	
	static final String ID_PARAM = "ID";	
	static final String NAME_PARAM = "NAME";	
	static final String CITY_PARAM = "CITY";	
	static final String PROV_PARAM = "PROV";	
	
	public static Intent getStartIntent(Context context, String id, String name, String city, String province){
		Intent start = new Intent(context, StoreDetailsActivity.class);
		
		if(id == null || name == null || city == null || province == null){
			throw new IllegalArgumentException("Id, Name, City, and province must not be null.");
		}
		
		// params for the class
		start.putExtra(ID_PARAM, Util.seoNormalize(id));
		start.putExtra(NAME_PARAM, Util.seoNormalize(name));
		start.putExtra(CITY_PARAM, Util.seoNormalize(city));
		start.putExtra(PROV_PARAM, Util.seoNormalize(province));
		
		return start;
	}
	
	public static void start(Context context, String id, String name, String city, String province){
		Intent start = getStartIntent(context, id, name, city, province);
		context.startActivity(start);
	}

	public static void start(Context context, Listing listing){
		start(context, listing.id, listing.name, listing.address.city, listing.address.prov);
	}
	
	@InjectView(R.id.store_name) TextView name;
	@InjectView(R.id.street_address) TextView streetAddress;
	@InjectView(R.id.city_and_province) TextView cityAndProvince;
	@InjectView(R.id.error) TextView errorMessage;
	
	@Inject YellowPagesApi api;
	@Inject RequestQueue queue;
	
	String storeId, storeName, storeCity, storeProvince;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		Bundle extras = getIntent().getExtras();
		
		// params for the class
		storeId = extras.getString(ID_PARAM);
		storeName = extras.getString(NAME_PARAM);
		storeCity = extras.getString(CITY_PARAM);
		storeProvince = extras.getString(PROV_PARAM);			
		
		if(storeId == null || storeName == null || storeCity == null || storeProvince == null){
			throw new IllegalArgumentException("Id, Name, City, and province must not be null.");
		}
		
		Request<BusinessDetailsResponse> resp = api.getBusinessDetails(storeId, storeName, storeCity, storeProvince, this, this);
		resp.setTag(this);
		queue.add(resp);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_details, menu);
		return true;
	}
	
	@Override
	protected void onStop() {
		queue.cancelAll(this);
		super.onStop();
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		errorMessage.setText(error.getMessage());
		errorMessage.setVisibility(View.VISIBLE);
		Util.Log(Log.DEBUG, this.getClass().getName(), error);
	}

	@Override
	public void onResponse(BusinessDetailsResponse response) {
		name.setText(response.name);
		streetAddress.setText(response.address.street);
		String cityAndProvinceString = getResources().getString(R.string.city_and_province, response.address.city, response.address.prov);
		cityAndProvince.setText(cityAndProvinceString);
	}

}
