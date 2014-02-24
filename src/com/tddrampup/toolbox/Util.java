package com.tddrampup.toolbox;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import android.util.Log;

import com.android.volley.VolleyError;

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

}
