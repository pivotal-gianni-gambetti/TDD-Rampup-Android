package com.tddrampup.testing.fake;

import org.mockito.Mockito;

import com.google.android.gms.maps.GoogleMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tddrampup.yellowpages.activities.MapActivity;
import com.tddrampup.yellowpages.activities.MapActivity.MapProvider;

@Singleton
public class FakeGoogleMapProvider implements MapProvider{

	private final GoogleMap map;
	
	@Inject
	public FakeGoogleMapProvider() {
		map = Mockito.mock(GoogleMap.class);
	}
	
	@Override
	public GoogleMap get(MapActivity activity) {
		return map;
	}

}
