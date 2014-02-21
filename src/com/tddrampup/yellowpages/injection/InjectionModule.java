package com.tddrampup.yellowpages.injection;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.GoogleMap;
import com.google.inject.AbstractModule;
import com.tddrampup.toolbox.UserGuid;
import com.tddrampup.toolbox.UserGuidProvider;

public class InjectionModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(RequestQueue.class).toProvider(RequestQueueProvider.class);
		bind(UserGuid.class).toProvider(UserGuidProvider.class);
		bind(GoogleMap.class).toProvider(GoogleMapProvider.class);
	}

}
