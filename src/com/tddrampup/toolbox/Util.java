package com.tddrampup.toolbox;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.tddrampup.yellowpages.api.YellowPagesApi.GeoLocation;

public class Util {

	public static String seoNormalize(String str) {
		return Normalizer.normalize(str, Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				.replaceAll("[^\\p{Alnum}]+", "-");
	}

	public static void Log(int priority, String tag, VolleyError error) {

		if (error.networkResponse == null) {
			Log.println(priority, tag, "Unknown error with volley query: "
					+ error.getMessage());
			return;
		}

		Log.println(priority, tag,
				"Error with volley query [" + error.getMessage() + "]: "
						+ error.networkResponse.statusCode);

		for (String key : error.networkResponse.headers.keySet()) {
			Log.println(priority, tag, key + " : "
					+ error.networkResponse.headers.get(key));
		}

		Log.println(priority, tag, new String(error.networkResponse.data));
	}

	public static double calculateDistanceBetweenPoints(GeoLocation l1,
			GeoLocation l2) {
		return calculateDistanceBetweenPoints(l1.convertToLatLng(),
				l2.convertToLatLng());
	}

	public static double calculateDistanceBetweenPoints(LatLng location1,
			LatLng location2) {

		if (location1 == null || location2 == null) {
			return Double.NaN;
		}

		final int radiusOfEarth = 6371;
		double latitudeDelta = radiansFromDegrees((location2.latitude - location1.latitude));
		double longitudeDelta = radiansFromDegrees((location2.longitude - location1.longitude));
		double latitude1 = radiansFromDegrees(location1.latitude);
		double latitude2 = radiansFromDegrees(location2.latitude);

		// Haversine Formula: http://en.wikipedia.org/wiki/Haversine_formula
		double a = Math.sin(latitudeDelta / 2) * Math.sin(latitudeDelta / 2)
				+ Math.sin(longitudeDelta / 2) * Math.sin(longitudeDelta / 2)
				* Math.cos(latitude1) * Math.cos(latitude2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = radiusOfEarth * c;

		return distance;
	}

	private static double radiansFromDegrees(double degrees) {
		return degrees * Math.PI / 180;
	}

}
