package com.tddrampup.testing;

import org.mockito.Matchers;
import org.mockito.Mockito;

import android.location.Location;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.inject.AbstractModule;
import com.tddrampup.testing.fake.FakeGoogleMapProvider;
import com.tddrampup.testing.fake.FakeLocationClientProvider;
import com.tddrampup.toolbox.GsonRequest;
import com.tddrampup.toolbox.UserGuid;
import com.tddrampup.yellowpages.activities.MapActivity.MapProvider;
import com.tddrampup.yellowpages.activities.NotificationDetailsActivity.NotificationCreator;
import com.tddrampup.yellowpages.activities.SearchActivity.LocationClientProvider;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.BusinessDetailsResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.FindBusinessResponse;
import com.tddrampup.yellowpages.ui.map.CameraUpdateWrapper;

public class TestApplicationModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(RequestQueue.class).toInstance(Mockito.mock(RequestQueue.class));
		bind(UserGuid.class).toInstance(
				new UserGuid("This is a test user guid"));
		bind(CameraUpdateWrapper.class).toInstance(
				Mockito.mock(CameraUpdateWrapper.class));
		bind(MapProvider.class).to(FakeGoogleMapProvider.class);
		bind(NotificationCreator.class).toInstance(
				Mockito.mock(NotificationCreator.class));

		bind(LocationClientProvider.class).to(FakeLocationClientProvider.class);

		buildApi();
	}

	private void buildApi() {
		YellowPagesApi apiMock = Mockito.mock(YellowPagesApi.class);

		Request<BusinessDetailsResponse> mockRequest = new GsonRequest<YellowPagesApi.BusinessDetailsResponse>(
				"", BusinessDetailsResponse.class, null, null);

		Request<FindBusinessResponse> mockFindRequest = new GsonRequest<YellowPagesApi.FindBusinessResponse>(
				"", FindBusinessResponse.class, null, null);

		Mockito.when(
				apiMock.getBusinessDetails(Matchers.<String> any(),
						Matchers.<String> any(), Matchers.<String> any(),
						Matchers.<String> any(),
						Matchers.<Listener<BusinessDetailsResponse>> any(),
						Matchers.<ErrorListener> any()))
				.thenReturn(mockRequest);

		Mockito.when(
				apiMock.findBusiness(Matchers.anyInt(),
						Matchers.<String> any(), Matchers.<Location> any(),
						Matchers.<Listener<FindBusinessResponse>> any(),
						Matchers.<ErrorListener> any())).thenReturn(
				mockFindRequest);

		Mockito.when(
				apiMock.findBusiness(Matchers.anyInt(),
						Matchers.<String> any(), Matchers.<String> any(),
						Matchers.<Listener<FindBusinessResponse>> any(),
						Matchers.<ErrorListener> any())).thenReturn(
				mockFindRequest);

		bind(YellowPagesApi.class).toInstance(apiMock);
	}
}
