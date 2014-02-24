package com.tddrampup.yellowpages.injection;

import roboguice.inject.ContextSingleton;
import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.inject.Inject;
import com.google.inject.Provider;

@ContextSingleton
public class RequestQueueProvider implements Provider<RequestQueue> {

	@Inject Activity currentActivity;
	
	@Override
	@ContextSingleton
	public RequestQueue get() {
		return Volley.newRequestQueue(currentActivity);
	}

}
