package com.tddrampup.yellowpages.ui.map;

import com.google.android.gms.maps.CameraUpdate;

public interface CameraUpdateWrapper {

	public CameraUpdate centerAt(double latitude, double longitude);

	public CameraUpdate zoomTo(float zoom);
}