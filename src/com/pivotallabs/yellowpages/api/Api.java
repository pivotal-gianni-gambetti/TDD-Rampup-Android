package com.pivotallabs.yellowpages.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

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

	protected byte[] getResource(Uri uri) throws IOException {

		URL url = new URL(uri.toString());

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.connect();
		
		int responseCode = connection.getResponseCode();
		
		if(responseCode != HttpURLConnection.HTTP_OK){
			connection.disconnect();
			throw new IOException("Error making call: http response code " + responseCode);
		}
		
		InputStream stream = connection.getInputStream();

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();

		int read = 0;
		byte[] buffer = new byte[512];

		while(read >= 0){

			read = stream.read(buffer);

			if(read > 0){
				bytes.write(buffer, 0, read);
			}
		}

		stream.close();

		return bytes.toByteArray();
	}

}
