package com.tddrampup.yellowpages.api;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tddrampup.toolbox.GsonRequest;
import com.tddrampup.toolbox.UserGuid;

@Singleton
public class YellowPagesApi extends Api {

	private static final String TAG = YellowPagesApi.class.getPackage()
			.getName();

	// TODO refactor into settings provider
	private static final String YELLOW_PAGE_HOST = "api.sandbox.yellowapi.com";
	static final String YELLOW_API_KEY = "34d5jf8npvp952a2yv7fsgsa";

	static final String API_KEY_QUERY_PARAM = "apikey";

	private UserGuid guid;

	@Inject
	public YellowPagesApi(UserGuid guid) {
		super("http", YELLOW_PAGE_HOST);

		this.guid = guid;
	}

	@Override
	protected Uri getPath(String path, Map<String, String> params) {
		Map<String, String> newParams = new HashMap<String, String>(params);
		newParams.put(API_KEY_QUERY_PARAM, YELLOW_API_KEY);
		return super.getPath(path, newParams);
	}

	// TODO Refactor WHERE + UID into location + user identifier providers
	// respectively
	public Request<FindBusinessResponse> findBusiness(int page, String what,
			String where, Listener<FindBusinessResponse> listener,
			ErrorListener errorListener) {

		StringBuilder validation = new StringBuilder();

		if (page < 0) {
			validation.append("Argument 'page' must be >= 0\n");
		}

		if (what == null) {
			validation.append("Argument 'what' must not be null\n");
		}

		if (where == null) {
			validation.append("Argument 'where' must not be null\n");
		}

		if (validation.length() > 0) {
			throw new IllegalArgumentException(validation.toString());
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("pg", Integer.toString(page));
		params.put("what", what);
		params.put("where", where);

		params.put("UID", guid.getGuid());

		params.put("fmt", "json");
		params.put("lang", "en");

		Uri path = getPath("/FindBusiness/", params);

		Log.v(TAG, path.toString());

		return new GsonRequest<FindBusinessResponse>(path.toString(),
				FindBusinessResponse.class, listener, errorListener);
	}

	public Request<BusinessDetailsResponse> getBusinessDetails(
			String listingId, String businessName, String city,
			String province, Listener<BusinessDetailsResponse> listener,
			ErrorListener errorListener) {

		StringBuilder validation = new StringBuilder();

		if (listingId == null) {
			validation.append("Argument 'listingId' must not be null\n");
		}

		if (businessName == null) {
			validation.append("Argument 'businessName' must not be null\n");
		}

		if (city == null) {
			validation.append("Argument 'city' must not be null\n");
		}

		if (province == null) {
			validation.append("Argument 'province' must not be null\n");
		}

		if (validation.length() > 0) {
			throw new IllegalArgumentException(validation.toString());
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("listingId", listingId);
		params.put("bus-name", businessName);
		params.put("city", city);
		params.put("prov", province);

		params.put("UID", guid.getGuid());

		params.put("fmt", "json");
		params.put("lang", "en");

		Uri path = getPath("/GetBusinessDetails/", params);

		Log.v(TAG, path.toString());

		return new GsonRequest<BusinessDetailsResponse>(path.toString(),
				BusinessDetailsResponse.class, listener, errorListener);
	}

	public static class FindBusinessResponse {
		public CallSummary summary = new CallSummary();
		public Listing[] listings = new Listing[0];
	}

	public static class CallSummary {
		public String what;
		public String where;
		public String latitude;
		public String longitude;

		public int firstListing;
		public int lastListing;
		public int totalListings;

		public int pageCount;
		public int currentPage;
		public int listingsPerPage;

		public String Prov;
	}

	public static class Listing implements Cloneable {
		public String id;
		public String name;
		public ListingAddress address = new ListingAddress(); // to avoid null
																// pointers
		public GeoLocation geoCode = new GeoLocation();

		public String distance; // probably a double
		public String parentId;
		public boolean isParent;

		// This exists, but we don't know wtf it does.
		// public Object? content;

		@Override
		public Listing clone() {
			Listing copy = new Listing();
			copy.id = id;
			copy.name = name;
			copy.address = address.clone();
			copy.geoCode = geoCode.clone();
			copy.distance = distance;
			copy.parentId = parentId;
			copy.isParent = isParent;

			return copy;
		}
	}

	public static class ListingAddress implements Cloneable {
		public String street;
		public String city;
		public String prov;
		public String pcode;

		@Override
		public ListingAddress clone() {
			ListingAddress copy = new ListingAddress();
			copy.street = street;
			copy.city = city;
			copy.prov = prov;
			copy.pcode = pcode;
			return copy;
		}
	}

	public static class GeoLocation implements Cloneable {
		public String latitude;
		public String longitude;

		@Override
		public GeoLocation clone() {
			GeoLocation copy = new GeoLocation();
			copy.latitude = latitude;
			copy.longitude = longitude;

			return copy;
		}

		public LatLng convertToLatLng() {
			try {
				return new LatLng(Double.parseDouble(latitude),
						Double.parseDouble(longitude));
			} catch (NumberFormatException exception) {
				Log.d(this.getClass().getName(),
						"Failure converting GeoLocation to a LatLng", exception);
				return null;
			}
		}
	}

	public static class PhoneInformation {
		@SerializedName("npa") public String areaCode;
		@SerializedName("nxx") public String firstThree;
		@SerializedName("num") public String lastFour;
		@SerializedName("dispNum") public String displayNumber;
		public String type;
	}

	public static class Products {
		public String[] webUrl = new String[0];
		public Profile[] profiles = new Profile[0];
	}

	public static class Profile {
		public Keywords keywords = new Keywords();
	}

	public static class Keywords {
		@SerializedName("OpenHrs") public String[] storeHours = new String[0];
	}

	public static class BusinessDetailsResponse {
		public String id;
		public String name;
		public ListingAddress address = new ListingAddress(); // to avoid null
																// pointers
		public GeoLocation geoCode = new GeoLocation();
		public PhoneInformation[] phones = new PhoneInformation[0];
		public Products products = new Products();
	}

}
