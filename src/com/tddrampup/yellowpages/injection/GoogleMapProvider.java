package com.tddrampup.yellowpages.injection;

import roboguice.inject.ContextSingleton;
import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tddrampup.R;

@ContextSingleton
public class GoogleMapProvider implements Provider<GoogleMap>{

	Activity currentActivity;
	
	@Inject
	public GoogleMapProvider(Activity current) {
		currentActivity = current;
	}
	
	
	@Override
	public GoogleMap get() {
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
