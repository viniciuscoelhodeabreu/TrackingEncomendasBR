package br.com.vinicius.trackingencomendasbr.util;

import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import lombok.NonNull;

public class HttpUtil {
	
	private static HttpClient httpClient;
	
	public static Optional<HttpResponse> doGet(final @NonNull String url) {
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = getHttpClient().execute(httpGet);
			return Optional.of(httpResponse);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to execute get to url " + url);
			return Optional.empty();
		}
	}
	

	public static HttpClient getHttpClient() {
		if(httpClient == null)
			httpClient = HttpClients.createDefault();
		
		return httpClient;
	}
	
}
