package com.pivotallabs.yellowpages.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;


public class YellowPagesApi extends Api {

	private static final String TAG = YellowPagesApi.class.getPackage().getName();
	
	// TODO refactor into settings provider
	private static final String YELLOW_PAGE_HOST = "api.sandbox.yellowapi.com";
	static final String YELLOW_API_KEY = "34d5jf8npvp952a2yv7fsgsa";
	
	static final String API_KEY_QUERY_PARAM = "apikey";
	
	// TODO inject this using roboguice
	private final Gson jsonParser = new Gson();
	
	public YellowPagesApi() {
		super(YELLOW_PAGE_HOST);
	}

	public YellowPagesApi(String protocol) {
		super(protocol, YELLOW_PAGE_HOST);
	}

	@Override
	protected Uri getPath(String path, Map<String,String> params){
		Map<String,String> newParams = new HashMap<String, String>(params);
		newParams.put(API_KEY_QUERY_PARAM, YELLOW_API_KEY);
		return super.getPath(path, newParams);
	}
	
	// TODO Refactor WHERE + UID into location + user identifier providers respectively
	public Response findBusiness(int page, String what, String where, String uid) throws ApiException{
		
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
		
		if(uid == null || uid.length() == 0){
			uid = "abc123";
			// TODO use uid provider
		}
		params.put("UID", uid);
		
		params.put("fmt", "json");
		params.put("lang", "en");
		
		Uri path = getPath("/FindBusiness/", params);
		
		try {
			byte[] data = getResource(path);
			
			return jsonParser.fromJson(new String(data), Response.class);
			
		} catch (IOException e) {
			Log.w(TAG, e.getMessage());
			throw new ApiException(e);
		}
	}
	
	static class Response {
		public CallSummary summary = new CallSummary();
		public Listing[] listings = new Listing[0];
	}
	
	static class CallSummary {
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
	
	static class Listing {
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
	
	static class ListingAddress {
		public String street;
		public String city;
		public String prov;
		public String pcode;
	}
	
	static class GeoLocation {
		public String latitude;
		public String longitude;
	}
	
}
