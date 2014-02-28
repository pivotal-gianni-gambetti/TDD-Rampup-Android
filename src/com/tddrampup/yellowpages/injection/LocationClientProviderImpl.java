package com.tddrampup.yellowpages.injection;

import android.content.Context;

import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.inject.Singleton;
import com.tddrampup.yellowpages.activities.SearchActivity.LocationClientProvider;

@Singleton
public class LocationClientProviderImpl implements LocationClientProvider {

	@Override
	public LocationClient get(Context ctx, ConnectionCallbacks con,
			OnConnectionFailedListener fail) {
		return new LocationClient(ctx, con, fail);
	}

}
