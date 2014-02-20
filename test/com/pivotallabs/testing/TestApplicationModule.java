package com.pivotallabs.testing;


import org.mockito.Mockito;

import com.android.volley.RequestQueue;
import com.google.inject.AbstractModule;
import com.pivotallabs.toolbox.UserGuid;

public class TestApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
    	bind(RequestQueue.class).toInstance(Mockito.mock(RequestQueue.class));
    	bind(UserGuid.class).toInstance(new UserGuid("This is a test user guid"));
    }
}
