package com.pivotallabs.yellowpages.injection;

import com.android.volley.RequestQueue;
import com.google.inject.AbstractModule;
import com.pivotallabs.toolbox.UserGuid;
import com.pivotallabs.toolbox.UserGuidProvider;

public class InjectionModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(RequestQueue.class).toProvider(RequestQueueProvider.class);
		bind(UserGuid.class).toProvider(UserGuidProvider.class);
	}

}
