package com.tddrampup.yellowpages.injection;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.inject.Singleton;
import com.tddrampup.R;
import com.tddrampup.yellowpages.activities.MapActivity;
import com.tddrampup.yellowpages.activities.MapActivity.MapProvider;

@Singleton
public class GoogleMapProvider implements MapProvider {

	@Override
	public GoogleMap get(MapActivity currentActivity) {
				
		MapFragment fragment = (MapFragment) currentActivity.getFragmentManager().findFragmentById(R.id.map);
		
		if(fragment == null){
			boolean hasViewWithId = currentActivity.findViewById(R.id.map) != null;
			
			throw new RuntimeException(
					"The current activity [" 
					+ currentActivity.getClass().getName()
					+ "] does not have a map frament with id R.id.map fix it. Has a view with that id? [ "
					+ hasViewWithId + " ]");
		}
		
		return fragment.getMap();
	}

}
