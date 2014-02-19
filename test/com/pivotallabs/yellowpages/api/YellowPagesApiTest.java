package com.pivotallabs.yellowpages.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import android.net.Uri;

@RunWith(RobolectricTestRunner.class)
public class YellowPagesApiTest {

	static class TestApi extends YellowPagesApi {
		private final byte[] data;
		public TestApi(byte[] fake){
			data = fake;
		}
		@Override
		protected byte[] getResource(Uri uri){
			return data;
		}
	}
	
	private static final byte[] EMPTY_RESPONSE = "{ \"summary\":{}, \"listings\": [] }".getBytes();
	
	@Test
	public void shouldValidateParameters() throws ApiException{
		YellowPagesApi api = new TestApi(EMPTY_RESPONSE);
		
		try{
			api.findBusiness(-1, null, null, null);
			
			throw new Error("Should fail with validation error");
		}catch( IllegalArgumentException exception ){
			// pass
			
			Assert.assertTrue(exception.getMessage().contains("page"));
			Assert.assertTrue(exception.getMessage().contains("what"));
			Assert.assertTrue(exception.getMessage().contains("where"));
		}
	}
	
	@Test
	public void shouldNotReturnNull(){
		YellowPagesApi api = new TestApi(EMPTY_RESPONSE);
		
		YellowPagesApi.Response response;
		try {
			response = api.findBusiness(0, "what", "where", null);

			Assert.assertNotNull(response);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
		
	}
	
	@Test
	public void shouldHaveValidStructure(){
		YellowPagesApi api = new TestApi(EMPTY_RESPONSE);
		
		YellowPagesApi.Response response;
		try {
			response = api.findBusiness(0, "what", "where", null);
		
			Assert.assertNotNull(response.listings);
			Assert.assertNotNull(response.summary);

		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
		
	}

	@Test
	public void shouldAddApiKeyToParams(){
		YellowPagesApi api = new YellowPagesApi();
		
		Uri uri = api.getPath("");
		
		Assert.assertEquals(uri.getQueryParameter(YellowPagesApi.API_KEY_QUERY_PARAM), YellowPagesApi.YELLOW_API_KEY);
	}
	
}
