package com.tddrampup.yellowpages.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import com.android.volley.Request;
import com.google.inject.Inject;
import com.tddrampup.testing.RobolectricTestRunnerWithInjection;
import com.tddrampup.yellowpages.api.YellowPagesApi.FindBusinessResponse;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class YellowPagesApiTest {

	@Inject YellowPagesApi api;

	@Test
	public void shouldValidateParameters() throws ApiException {
		try {
			api.findBusiness(-1, null, (String) null, null, null);

			throw new Error("Should fail with validation error");
		} catch (IllegalArgumentException exception) {
			// pass

			Assert.assertTrue(exception.getMessage().contains("page"));
			Assert.assertTrue(exception.getMessage().contains("what"));
			Assert.assertTrue(exception.getMessage().contains("where"));
		}
	}

	@Test
	public void shouldNotReturnNull() {
		Request<FindBusinessResponse> resquest = api.findBusiness(0, "what",
				"where", null, null);

		Assert.assertNotNull(resquest);
	}

	@Test
	public void shouldAddApiKeyToParams() {
		Uri uri = api.getPath("");

		Assert.assertEquals(
				uri.getQueryParameter(YellowPagesApi.API_KEY_QUERY_PARAM),
				YellowPagesApi.YELLOW_API_KEY);
	}

}
