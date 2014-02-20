package com.pivotallabs.testing;

import com.pivotallabs.yellowpages.api.ApiException;
import com.pivotallabs.yellowpages.api.YellowPagesApi;
import com.pivotallabs.yellowpages.api.YellowPagesApi.Response;

public class FakeYellowPagesApi extends YellowPagesApi {

	private Response fakeResponse;
	
	public FakeYellowPagesApi(){}
	
	public FakeYellowPagesApi(Response resp){
		fakeResponse = resp;
	}
	
	public void setResponse(Response resp){
		fakeResponse = resp;
	}
	
	@Override
	public Response findBusiness(int page, String what, String where, String uid) throws ApiException {
		if(fakeResponse == null){
			throw new ApiException("Some exception because the fake response was null");
		}
		return fakeResponse;
	}
	
}
