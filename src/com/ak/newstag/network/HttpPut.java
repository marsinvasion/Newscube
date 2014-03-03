package com.ak.newstag.network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.ak.newstag.exception.NotFoundException;
import com.ak.newstag.exception.UnAuthorizedException;
import com.ak.newstag.model.Person;

public class HttpPut {

	private static int CONNECTION_TIMEOUT = 5000;

	public static void put(String urlString, Person person, String jsonString)
			throws MalformedURLException, IOException, UnAuthorizedException,
			NotFoundException {
		URL url = new URL(urlString);

		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setConnectTimeout(CONNECTION_TIMEOUT);
		httpCon.setRequestProperty("Content-Type", "application/json");
		httpCon.setRequestMethod("PUT");
		httpCon.setRequestProperty("account-name", person.getAccountName());
		httpCon.setRequestProperty("display-name", person.getDisplayName());
		httpCon.setRequestProperty("google-id", person.getId());
		httpCon.setRequestProperty("first-name", person.getFirstName());
		httpCon.setRequestProperty("last-name", person.getLastName());

		OutputStreamWriter out = new OutputStreamWriter(
				httpCon.getOutputStream());
		out.write(jsonString);
		out.close();
		switch (httpCon.getResponseCode()) {
		case 401:
			throw new UnAuthorizedException();
		case 404:
			throw new NotFoundException();

		}
	}
}
