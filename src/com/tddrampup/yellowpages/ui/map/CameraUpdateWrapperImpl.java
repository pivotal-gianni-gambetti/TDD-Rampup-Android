package com.tddrampup.yellowpages.ui.map;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class CameraUpdateWrapperImpl implements CameraUpdateWrapper {

	final static double MIN_BOUNDING_AREA = 0.00001;

	@Override
	public CameraUpdate centerAt(double lat, double lng) {
		return CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
	}

	@Override
	public CameraUpdate zoomTo(float zoom) {
		return CameraUpdateFactory.zoomTo(zoom);
	}

	@Override
	public CameraUpdate bounds(double maxLatitude, double maxLongitude,
			double minLatitude, double minLongitude) {

		LatLngBounds bounds = calculateBounds(maxLatitude, maxLongitude,
				minLatitude, minLongitude);

		return CameraUpdateFactory.newLatLngBounds(bounds, 20);
	}

	LatLngBounds calculateBounds(double maxLatitude, double maxLongitude,
			double minLatitude, double minLongitude) {
		double area = (maxLatitude - minLatitude)
				* (maxLongitude - minLongitude);

		Log.d(this.getClass().getName(), "AREA = " + area);
		if (area < MIN_BOUNDING_AREA) {
			// extrapolate the min + max of each thing, keep ratio

			double desiredArea = MIN_BOUNDING_AREA;

			double height = maxLatitude - minLatitude;
			double width = maxLongitude - minLongitude;

			double newHeight = 0;
			if (height == 0 || width == 0) {
				newHeight = Math.sqrt(desiredArea);
			} else {
				newHeight = Math.sqrt(desiredArea * height / width);
			}
			double newWidth = desiredArea / newHeight;

			double heightUpdate = (newHeight - height) / 2;
			minLatitude -= heightUpdate;
			maxLatitude += heightUpdate;

			double widthUpdate = (newWidth - width) / 2;
			minLongitude -= widthUpdate;
			maxLongitude += widthUpdate;

			Log.d(this.getClass().getName(), "AREA = " + MIN_BOUNDING_AREA);
		}

		LatLng southWest = new LatLng(minLatitude, minLongitude);
		LatLng northEast = new LatLng(maxLatitude, maxLongitude);

		LatLngBounds bounds = new LatLngBounds(southWest, northEast);
		return bounds;
	}
}
