package com.ak.newstag.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonReader<T> {

	private static int CONNECTION_TIMEOUT = 5000;

	private String getJsonString(String uri) {
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(uri);
			BufferedReader reader = null;
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private Collection<T> getData(String json, Type listType) {
		Gson gson = new GsonBuilder().setDateFormat(
				"EEE MMM dd yyyy HH:mm:ss z").create();
		Collection<T> jsonList = gson.fromJson(json, listType);
		return jsonList;
	}

	public JsonReader() {
		super();
	}

	public Collection<T> getJsonData(String url, Type listType) {
		String json = getJsonString(url);
		return getData(json, listType);
	}
}
