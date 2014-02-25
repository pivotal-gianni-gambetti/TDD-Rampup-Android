package com.tddrampup.yellowpages.activities;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.tddrampup.R;
import com.tddrampup.toolbox.Util;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.BusinessDetailsResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.GeoLocation;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;
import com.tddrampup.yellowpages.api.YellowPagesApi.PhoneInformation;

@ContentView(R.layout.activity_store_details)
public class StoreDetailsActivity extends MapActivity implements
		Listener<BusinessDetailsResponse>, ErrorListener,
		OnMyLocationButtonClickListener {

	private static final String TAG = StoreDetailsActivity.class.getName();

	static final String ID_PARAM = "ID";
	static final String NAME_PARAM = "NAME";
	static final String CITY_PARAM = "CITY";
	static final String PROV_PARAM = "PROV";

	public static Intent getStartIntent(Context context, String id,
			String name, String city, String province) {
		Intent start = new Intent(context, StoreDetailsActivity.class);

		if (id == null || name == null || city == null || province == null) {
			throw new IllegalArgumentException(
					"Id, Name, City, and province must not be null.");
		}

		// params for the class
		start.putExtra(ID_PARAM, Util.seoNormalize(id));
		start.putExtra(NAME_PARAM, Util.seoNormalize(name));
		start.putExtra(CITY_PARAM, Util.seoNormalize(city));
		start.putExtra(PROV_PARAM, Util.seoNormalize(province));

		return start;
	}

	public static void start(Context context, String id, String name,
			String city, String province) {
		Intent start = getStartIntent(context, id, name, city, province);
		context.startActivity(start);
	}

	public static void start(Context context, Listing listing) {
		start(context, listing.id, listing.name, listing.address.city,
				listing.address.prov);
	}

	@InjectView(R.id.store_name) TextView name;
	@InjectView(R.id.street_address) TextView streetAddress;
	@InjectView(R.id.city_and_province) TextView cityAndProvince;
	@InjectView(R.id.phone_number) TextView phoneNumber;
	@InjectView(R.id.website) TextView webUrl;
	@InjectView(R.id.progress) ProgressBar loadingSpinner;
	@InjectView(R.id.error) TextView errorMessage;
	@InjectView(R.id.store_hours) LinearLayout storeHoursContainer;
	@InjectView(R.id.details_view) LinearLayout detailsContainer;

	@Inject YellowPagesApi api;
	@Inject RequestQueue queue;

	private GeoLocation storeLocation;

	@InjectExtra(value = ID_PARAM) String storeId;
	@InjectExtra(value = NAME_PARAM) String storeName;
	@InjectExtra(value = CITY_PARAM) String storeCity;
	@InjectExtra(value = PROV_PARAM) String storeProvince;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		map.setOnMyLocationButtonClickListener(this);

		Request<BusinessDetailsResponse> resp = api.getBusinessDetails(storeId,
				storeName, storeCity, storeProvince, this, this);
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
		loadingSpinner.setVisibility(View.GONE);
		errorMessage.setText(R.string.details_api_error);
		errorMessage.setVisibility(View.VISIBLE);
		Util.Log(Log.DEBUG, this.getClass().getName(), error);
	}

	@Override
	public void onResponse(BusinessDetailsResponse response) {

		loadingSpinner.setVisibility(View.GONE);
		detailsContainer.setVisibility(View.VISIBLE);

		name.setText(response.name);
		streetAddress.setText(response.address.street);
		String cityAndProvinceString = getResources().getString(
				R.string.city_and_province, response.address.city,
				response.address.prov);
		cityAndProvince.setText(cityAndProvinceString);

		Log.v(this.getClass().getName(), new Gson().toJson(response));

		storeLocation = response.geoCode;
		setStoreHours(response);

		setPhoneNumber(response);

		setWebUrl(response);

		updateMap(response);
	}

	@Override
	public boolean onMyLocationButtonClick() {
		double latitude = Double.parseDouble(storeLocation.latitude);
		double longitude = Double.parseDouble(storeLocation.longitude);
		map.animateCamera(cameraUpdater.centerAt(latitude, longitude));
		return true;
	}

	private void updateMap(BusinessDetailsResponse response) {

		if (response.geoCode == null) {
			Log.i(TAG, "No geocode on response");
			return;
		}

		double lat = Double.parseDouble(response.geoCode.latitude), lon = Double
				.parseDouble(response.geoCode.longitude);

		addMarker(lat, lon, response.name, response.address.street);
		map.moveCamera(cameraUpdater.centerAt(lat, lon));
	}

	private void setStoreHours(BusinessDetailsResponse response) {
		if (response.products.profiles.length > 0) {
			for (String hours : response.products.profiles[0].keywords.storeHours) {
				TextView dynamicView = new TextView(this);
				dynamicView.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				dynamicView.setText(hours);
				storeHoursContainer.addView(dynamicView);
			}
		} else {
			Log.v(this.getClass().getName(), "No profiles on this product.");
		}
	}

	private void setPhoneNumber(BusinessDetailsResponse response) {
		if (response.phones.length > 0) {
			String displayNumber = "";
			for (PhoneInformation phone : response.phones) {
				if ("primary".equalsIgnoreCase(phone.type)) {
					displayNumber = phone.displayNumber;
					break;
				} else if (displayNumber.isEmpty()) {
					displayNumber = phone.displayNumber;
				}
			}

			phoneNumber.setText(displayNumber);
		}
	}

	private void setWebUrl(BusinessDetailsResponse response) {
		if (response.products.webUrl.length > 0) {
			webUrl.setText(response.products.webUrl[0]);
		} else {
			Log.v(this.getClass().getName(), "No web url for this store.");
		}
	}

}
