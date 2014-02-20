package com.pivotallabs.testing.fake;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;

public class FakeRequestQueue extends RequestQueue {

	public FakeRequestQueue(Cache cache, Network network, int threadPoolSize, ResponseDelivery delivery) {
		super(cache, network, threadPoolSize, delivery);
		// TODO Auto-generated constructor stub
	}

	public FakeRequestQueue(Cache cache, Network network, int threadPoolSize) {
		super(cache, network, threadPoolSize);
		// TODO Auto-generated constructor stub
	}

	public FakeRequestQueue(Cache cache, Network network) {
		super(cache, network);
		// TODO Auto-generated constructor stub
	}

}
