package com.tddrampup.yellowpages.injection;

import com.android.volley.RequestQueue;
import com.google.inject.AbstractModule;
import com.tddrampup.toolbox.UserGuid;
import com.tddrampup.toolbox.UserGuidProvider;
import com.tddrampup.yellowpages.activities.MapActivity.MapProvider;
import com.tddrampup.yellowpages.activities.NotificationDetailsActivity.NotificationCreator;
import com.tddrampup.yellowpages.ui.map.CameraUpdateWrapper;
import com.tddrampup.yellowpages.ui.map.CameraUpdateWrapperImpl;

public class InjectionModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(RequestQueue.class).toProvider(RequestQueueProvider.class);
		bind(UserGuid.class).toProvider(UserGuidProvider.class);
		bind(MapProvider.class).to(GoogleMapProvider.class);
		bind(CameraUpdateWrapper.class).to(CameraUpdateWrapperImpl.class);
		bind(NotificationCreator.class).to(
				NotificationDetailsNotificationCreator.class);
	}

}
