package com.tddrampup.yellowpages.ui.map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tddrampup.testing.RobolectricTestRunnerWithInjection;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class CameraUpdateWrapperImplTest {

	CameraUpdateWrapperImpl cameraUpdater;

	@Before
	public void setup() {
		cameraUpdater = new CameraUpdateWrapperImpl();
	}

	@Test
	public void calculateBoundsCorrectly_whenAreaIsLarge() {
		double maxLatitude = 1.0;
		double minLatitude = 0.0;
		double maxLongitude = 1.0;
		double minLongitude = 0.0;

		LatLngBounds bounds = cameraUpdater.calculateBounds(maxLatitude,
				maxLongitude, minLatitude, minLongitude);
		double area = (bounds.northeast.latitude - bounds.southwest.latitude)
				* (bounds.northeast.longitude - bounds.southwest.longitude);

		Assert.assertEquals(maxLongitude, bounds.northeast.longitude, 0.0);
		Assert.assertEquals(maxLatitude, bounds.northeast.latitude, 0.0);
		Assert.assertEquals(minLongitude, bounds.southwest.longitude, 0.0);
		Assert.assertEquals(minLatitude, bounds.southwest.latitude, 0.0);
		Assert.assertTrue(area > CameraUpdateWrapperImpl.MIN_BOUNDING_AREA);
	}

	@Test
	public void calculateBoundsCorrectly_whenAreaIsSmall() {
		double maxLatitude = 0.0001;
		double minLatitude = 0.0;
		double maxLongitude = 0.0001;
		double minLongitude = 0.0;

		LatLngBounds bounds = cameraUpdater.calculateBounds(maxLatitude,
				maxLongitude, minLatitude, minLongitude);
		double area = (bounds.northeast.latitude - bounds.southwest.latitude)
				* (bounds.northeast.longitude - bounds.southwest.longitude);

		Assert.assertTrue(bounds
				.contains(new LatLng(maxLatitude, maxLongitude)));
		Assert.assertTrue(bounds
				.contains(new LatLng(maxLatitude, minLongitude)));
		Assert.assertTrue(bounds
				.contains(new LatLng(minLatitude, maxLongitude)));
		Assert.assertTrue(bounds
				.contains(new LatLng(minLatitude, minLongitude)));
		Assert.assertTrue(area >= CameraUpdateWrapperImpl.MIN_BOUNDING_AREA);
	}

	@Test
	public void shouldKeepCenter_whenAreaIsSmall() {
		double maxLatitude = 0.0001;
		double minLatitude = 0.0;
		double maxLongitude = 0.0001;
		double minLongitude = 0.0;

		LatLngBounds bounds = cameraUpdater.calculateBounds(maxLatitude,
				maxLongitude, minLatitude, minLongitude);

		Assert.assertEquals(mid(maxLatitude, minLatitude),
				mid(bounds.northeast.latitude, bounds.southwest.latitude),
				CameraUpdateWrapperImpl.MIN_BOUNDING_AREA * 0.001);

		Assert.assertEquals(mid(maxLongitude, minLongitude),
				mid(bounds.northeast.longitude, bounds.southwest.longitude),
				CameraUpdateWrapperImpl.MIN_BOUNDING_AREA * 0.001);

	}

	@Test
	public void shouldKeepCenter_whenAreaIsLarge() {
		double maxLatitude = 1.0;
		double minLatitude = 0.0;
		double maxLongitude = 1.0;
		double minLongitude = 0.0;

		LatLngBounds bounds = cameraUpdater.calculateBounds(maxLatitude,
				maxLongitude, minLatitude, minLongitude);

		Assert.assertEquals(mid(maxLatitude, minLatitude),
				mid(bounds.northeast.latitude, bounds.southwest.latitude),
				CameraUpdateWrapperImpl.MIN_BOUNDING_AREA * 0.001);

		Assert.assertEquals(mid(maxLongitude, minLongitude),
				mid(bounds.northeast.longitude, bounds.southwest.longitude),
				CameraUpdateWrapperImpl.MIN_BOUNDING_AREA * 0.001);

	}

	private double mid(double a, double b) {
		return (a - b) / 2 + b;
	}
}
