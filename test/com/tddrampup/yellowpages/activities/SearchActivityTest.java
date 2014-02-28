package com.tddrampup.yellowpages.activities;

import java.util.Collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.util.ActivityController;

import android.content.Intent;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.inject.Inject;
import com.tddrampup.testing.RobolectricTestRunnerWithInjection;
import com.tddrampup.yellowpages.api.YellowPagesApi.FindBusinessResponse;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class SearchActivityTest {

	@Inject RequestQueue queue;

	ActivityController<SearchActivity> controller;
	SearchActivity activity;

	Listing testListing;

	// anything else

	@Before
	public void setup() {

		Intent start = SearchActivity.getStartIntent(Robolectric.application,
				"What", "where");

		controller = Robolectric.buildActivity(SearchActivity.class)
				.withIntent(start);
		activity = controller.create().start().resume().get();

		testListing = new Listing();

		testListing.address.city = "Toronto";
		testListing.address.pcode = "M4E 1E9";
		testListing.address.prov = "ON";
		testListing.address.street = "King";

		testListing.distance = "1.0";
		testListing.geoCode.latitude = "43.672574";
		testListing.geoCode.longitude = "-79.287992";

		testListing.id = "abc1123";
		testListing.name = "Test Name";
		testListing.isParent = false;
		testListing.parentId = "";
	}

	@Test
	public void shouldAttachScrollListener() {
		ShadowListView listViewShadow = Robolectric.shadowOf(activity
				.getListView());
		Assert.assertEquals(activity, listViewShadow.getOnScrollListener());
	}

	@Test
	public void shouldShowLoadingSpinner_beforeResultsReturn() {
		// spinner should be showing

		Assert.assertEquals(View.VISIBLE, activity.progress.getVisibility());
		Assert.assertEquals(View.GONE, activity.list.getVisibility());
		Assert.assertEquals(View.GONE, activity.error.getVisibility());
	}

	@Test
	public void shouldHideLoadingSpinner_whenResultsReturn() {
		// spinner should not be showing

		// build response.
		FindBusinessResponse resp = buildResponse(testListing);

		// send the response
		activity.onResponse(resp);

		Assert.assertEquals(View.GONE, activity.progress.getVisibility());
		Assert.assertEquals(View.VISIBLE, activity.list.getVisibility());
		Assert.assertEquals(View.GONE, activity.error.getVisibility());
	}

	@Test
	public void shouldCallTheYellowPagesAPI() {
		Mockito.verify(queue).add(Mockito.<Request<?>> any());
	}

	@Test
	public void shouldShowErrorMessage_whenReceivingNetworkRequestWithoutResults() {

		NetworkResponse response = new NetworkResponse(500, new byte[0],
				Collections.<String, String> emptyMap(), true);

		VolleyError error = new VolleyError(response);

		activity.onErrorResponse(error);

		Assert.assertEquals(View.GONE, activity.progress.getVisibility());
		Assert.assertEquals(View.GONE, activity.list.getVisibility());
		Assert.assertEquals(View.VISIBLE, activity.error.getVisibility());
	}

	@Test
	public void shouldNotShowErrorMessage_whenReceivingNetworkErrorAndAlreadyHasResults() {

		// build response.
		FindBusinessResponse resp = buildResponse(testListing);

		// send the response
		activity.onResponse(resp);

		NetworkResponse response = new NetworkResponse(500, new byte[0],
				Collections.<String, String> emptyMap(), true);
		VolleyError error = new VolleyError(response);
		activity.onErrorResponse(error);

		Assert.assertEquals(View.GONE, activity.progress.getVisibility());
		Assert.assertEquals(View.VISIBLE, activity.list.getVisibility());
		Assert.assertEquals(View.GONE, activity.error.getVisibility());
	}

	@Test
	public void shouldStartDetailsActivity_onItemClick() {

		// send the response
		activity.onResponse(buildResponse(testListing));

		activity.onListItemClick(null, null, 0, 0);

		// start the search activity
		ShadowActivity shadowActivity = Robolectric.shadowOf_(activity);
		Intent startedIntent = shadowActivity.getNextStartedActivity();

		Assert.assertNotNull(startedIntent);
		// Assert.assertEquals(startedIntent.getComponent().getClassName(),
		// LocationMapActivity.class.getName());
		Assert.assertEquals(startedIntent.getComponent().getClassName(),
				StoreDetailsActivity.class.getName());
	}

	@Test
	public void shouldRequestNextPageOfResults_whenScrolledToBottomOfCurrentPage() {
		activity.onResponse(buildResponse(2, testListing));
		activity.onScroll(null, 0, 1, 1);
		Mockito.verify(queue, Mockito.times(2)).add(Mockito.<Request<?>> any());
	}

	@Test
	public void shouldUntilTheEndOfResults_whenScrolledToBottom() {
		int pages = 3;

		for (int i = 0; i < pages; i++) {
			Mockito.verify(queue, Mockito.times(i + 1)).add(
					Mockito.<Request<?>> any());
			activity.onResponse(buildResponse(pages, testListing));
			activity.onScroll(null, 0, i + 1, i + 1);
		}

		Mockito.verify(queue, Mockito.times(pages)).add(
				Mockito.<Request<?>> any());
	}

	@Test
	public void shouldNotQueryNextPageOfResults_whenThereAreNoMorePagesToLoad() {
		activity.onResponse(buildResponse(1, testListing));
		activity.onScroll(null, 0, 1, 1);
		Mockito.verify(queue, Mockito.times(1)).add(Mockito.<Request<?>> any());
	}

	@Test
	public void shouldNotQueryNextPageOfResults_whenThereIsAPendingRequest() {
		activity.onResponse(buildResponse(2, testListing));
		activity.onScroll(null, 0, 1, 1);
		// scroll to the bottom again, this should NOT start a 3rd request
		activity.onScroll(null, 0, 1, 1);
		Mockito.verify(queue, Mockito.times(2)).add(Mockito.<Request<?>> any());
	}

	@Test
	public void shouldAddFooterProgressView_toListView() {
		Assert.assertEquals(1, activity.list.getFooterViewsCount());
	}

	@Test
	public void shouldRemoveFooterProgressView_whenThereAreNoMorePagesToLoad() {
		activity.onResponse(buildResponse(testListing));
		Assert.assertEquals(0, activity.list.getFooterViewsCount());
	}

	private FindBusinessResponse buildResponse(Listing... listings) {
		return buildResponse(1, listings);
	}

	private FindBusinessResponse buildResponse(int pages, Listing... listings) {
		FindBusinessResponse resp = new FindBusinessResponse();
		resp.listings = listings;
		resp.summary.pageCount = pages;

		return resp;
	}

}
