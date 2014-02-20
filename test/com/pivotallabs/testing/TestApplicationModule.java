package com.pivotallabs.testing;


import com.google.inject.AbstractModule;
import com.pivotallabs.yellowpages.api.YellowPagesApi;

public class TestApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
    	bind(YellowPagesApi.class).to(FakeYellowPagesApi.class);
    }
}
