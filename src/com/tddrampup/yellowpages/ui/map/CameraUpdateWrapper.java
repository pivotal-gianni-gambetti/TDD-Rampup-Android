package com.tddrampup.yellowpages.ui.map;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;

public interface CameraUpdateWrapper {

	public CameraUpdate newLatLng(LatLng latLng);

	public CameraUpdate zoomTo(float zoom);
}