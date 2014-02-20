package com.pivotallabs.yellowpages.api;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.pivotallabs.toolbox.GsonRequest;
import com.pivotallabs.toolbox.UserGuid;

@Singleton
public class YellowPagesApi extends Api {

	private static final String TAG = YellowPagesApi.class.getPackage().getName();
	
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
	protected Uri getPath(String path, Map<String,String> params){
		Map<String,String> newParams = new HashMap<String, String>(params);
		newParams.put(API_KEY_QUERY_PARAM, YELLOW_API_KEY);
		return super.getPath(path, newParams);
	}
	
	// TODO Refactor WHERE + UID into location + user identifier providers respectively
	public Request<Response> findBusiness(int page, String what, String where, Listener<Response> listener, ErrorListener errorListener) {
		
		StringBuilder validation = new StringBuilder();
		
		if(page < 0){
			validation.append("Argument 'page' must be >= 0\n");
		}
		
		if(what == null){
			validation.append("Argument 'what' must not be null\n");
		}
		
		if(where == null){
			validation.append("Argument 'where' must not be null\n");
		}
		
		if(validation.length() > 0){
			throw new IllegalArgumentException(validation.toString());
		}
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("pg", Integer.toString(page));
		params.put("what", what);
		params.put("where", where);
		
		params.put("UID", guid.getGuid());
		
		params.put("fmt", "json");
		params.put("lang", "en");
		
		Uri path = getPath("/FindBusiness/", params);
		
		Log.v(TAG, path.toString());
		
		return new GsonRequest<Response>(path.toString(), Response.class, listener, errorListener);
	}
	
	public static class Response {
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
	
	public static class Listing {
		public String id;
		public String name;
		public ListingAddress address = new ListingAddress(); // to avoid null pointers
		public GeoLocation geoCode = new GeoLocation();
		
		public String distance; // probably a double
		public String parentId;
		public boolean isParent;
		
		// This exists, but we don't know wtf it does.
		// public Object? content;
		
	}
	
	public static class ListingAddress {
		public String street;
		public String city;
		public String prov;
		public String pcode;
	}
	
	public static class GeoLocation {
		public String latitude;
		public String longitude;
	}
	
}
