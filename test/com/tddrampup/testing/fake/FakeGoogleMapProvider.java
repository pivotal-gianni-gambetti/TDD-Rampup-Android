package com.tddrampup.testing.fake;

import org.mockito.Mockito;

import com.google.android.gms.maps.GoogleMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tddrampup.yellowpages.activities.MapActivity;
import com.tddrampup.yellowpages.activities.MapActivity.MapProvider;

@Singleton
public class FakeGoogleMapProvider implements MapProvider{

	@Inject
	public FakeGoogleMapProvider() {}
	
	@Override
	public GoogleMap get(MapActivity activity) {
		return Mockito.mock(GoogleMap.class);
	}

}
