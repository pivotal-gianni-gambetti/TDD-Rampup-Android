package com.tddrampup.yellowpages.api;

import java.util.Collections;
import java.util.Map;

import android.net.Uri;

public class Api {

	protected Api(String hostname){
		this("https", hostname);
	}

	protected Api(String protocol, String hostname){
		this.protocol = protocol;
		this.hostname = hostname;
	}

	private final String protocol;
	private final String hostname;

	protected Uri getPath(String path){
		return getPath(path, Collections.<String,String>emptyMap());
	}

	protected Uri getPath(String path, Map<String, String> params){

		Uri.Builder builder = new Uri.Builder()
		.authority(hostname)
		.scheme(protocol)
		.path(path);

		for (Map.Entry<String, String> entry: params.entrySet()) {
			builder.appendQueryParameter(entry.getKey(), entry.getValue());
		}

		return builder.build();
	}

}
