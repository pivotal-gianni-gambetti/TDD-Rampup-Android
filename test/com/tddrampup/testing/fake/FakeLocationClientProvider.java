package com.tddrampup.testing.fake;

import org.mockito.Mockito;

import android.content.Context;

import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tddrampup.yellowpages.activities.SearchActivity.LocationClientProvider;

@Singleton
public class FakeLocationClientProvider implements LocationClientProvider {

	private final LocationClient mockClient;

	@Inject
	public FakeLocationClientProvider() {
		mockClient = Mockito.mock(LocationClient.class);
	}

	@Override
	public LocationClient get(Context ctx, ConnectionCallbacks con,
			OnConnectionFailedListener fail) {
		return mockClient;
	}
}
