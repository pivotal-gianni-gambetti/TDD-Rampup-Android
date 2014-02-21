package com.tddrampup.yellowpages.ui.map;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class CameraUpdateWrapperImpl implements CameraUpdateWrapper{

	@Override
	public CameraUpdate newLatLng(LatLng latLng) {
		return CameraUpdateFactory.newLatLng(latLng);
	}

	@Override
	public CameraUpdate zoomTo(float zoom) {
		return CameraUpdateFactory.zoomTo(zoom);
	}

}
