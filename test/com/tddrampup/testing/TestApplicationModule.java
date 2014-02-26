package com.tddrampup.testing;

import org.mockito.Mockito;

import com.android.volley.RequestQueue;
import com.google.inject.AbstractModule;
import com.tddrampup.testing.fake.FakeGoogleMapProvider;
import com.tddrampup.toolbox.UserGuid;
import com.tddrampup.yellowpages.activities.MapActivity.MapProvider;
import com.tddrampup.yellowpages.activities.NotificationDetailsActivity.NotificationCreator;
import com.tddrampup.yellowpages.ui.map.CameraUpdateWrapper;

public class TestApplicationModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(RequestQueue.class).toInstance(Mockito.mock(RequestQueue.class));
		bind(UserGuid.class).toInstance(
				new UserGuid("This is a test user guid"));
		bind(CameraUpdateWrapper.class).toInstance(
				Mockito.mock(CameraUpdateWrapper.class));
		bind(MapProvider.class).to(FakeGoogleMapProvider.class);
		bind(NotificationCreator.class).toInstance(
				Mockito.mock(NotificationCreator.class));
	}
}
