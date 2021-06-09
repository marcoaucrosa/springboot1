package br.com.marco.sw.planets.api.services.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ServiceUtils {
	
	private ServiceUtils() {
		
	}

	private static JsonObject getRequest(HttpGet getRequest) throws IOException {

		HttpClient httpClient = HttpClientBuilder.create().build();
		getRequest.addHeader("accept", "application/json");
		HttpResponse response = httpClient.execute(getRequest);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Erro : código do erro HTTP: " + response.getStatusLine().getStatusCode());
		}

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

		String line;
		StringBuilder stringBuilder = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}

		JsonObject jsonObject = deserialize(stringBuilder.toString());
		bufferedReader.close();

		return jsonObject;
	}

	private static JsonObject deserialize(String json) {
		Gson gson = new Gson();
		JsonObject jsonClass = gson.fromJson(json, JsonObject.class);
		return jsonClass;
	}

	public static JsonObject getBuilder(String url) throws Exception {
		HttpGet httpGet = new HttpGet(url);
		return getRequest(httpGet);
	}
}
