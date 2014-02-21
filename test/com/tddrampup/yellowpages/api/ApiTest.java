package com.tddrampup.yellowpages.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.tddrampup.yellowpages.api.Api;

import android.net.Uri;

@RunWith(RobolectricTestRunner.class)
public class ApiTest {

	@Test
	public void shouldGeneratePath_basedOnHostnamePassed() {
		String testHostname = "abcdef";
		Api baseApi = new Api(testHostname);
		
		Uri simpleUri = baseApi.getPath("");
		
		Assert.assertEquals(testHostname, simpleUri.getAuthority());
	}
	
	@Test
	public void shouldGeneratePath_withDefaultHttpsScheme() {
		String testHostname = "abcdef";
		Api baseApi = new Api(testHostname);
		
		Uri simpleUri = baseApi.getPath("");
		
		Assert.assertEquals("https", simpleUri.getScheme());
	}
	
	@Test
	public void shouldGeneratePath_basedOnSchemePassed() {
		String testScheme = "qwerty";
		String testHostname = "abcdef";
		Api baseApi = new Api(testScheme, testHostname);
		
		Uri simpleUri = baseApi.getPath("");
		
		Assert.assertEquals(testScheme, simpleUri.getScheme());
	}
	
	@Test
	public void shouldGeneratePath_basedOnParametersPassed() {
		String testHostname = "abcdef";
		Api baseApi = new Api(testHostname);
		
		Map<String,String> params = new HashMap<String,String>();
		
		params.put("hello", "world");
		params.put("power", "rangers");
		params.put("ninja", "turtles");
		params.put("trog", "dor");
		params.put("james", "bond");
		
		
		Uri simpleUri = baseApi.getPath("", params);
		
		for(Map.Entry<String, String> value : params.entrySet()){
			Assert.assertEquals(value.getValue(), simpleUri.getQueryParameter(value.getKey()));
		}
	}
	
}
