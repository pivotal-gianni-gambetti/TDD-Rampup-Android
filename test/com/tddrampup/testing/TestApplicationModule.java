package com.tddrampup.testing;


import org.mockito.Mockito;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.GoogleMap;
import com.google.inject.AbstractModule;
import com.tddrampup.toolbox.UserGuid;
import com.tddrampup.yellowpages.injection.GoogleMapProvider;

public class TestApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
    	bind(RequestQueue.class).toInstance(Mockito.mock(RequestQueue.class));
    	bind(UserGuid.class).toInstance(new UserGuid("This is a test user guid"));
    	bind(GoogleMap.class).toProvider(GoogleMapProvider.class);
    }
}
