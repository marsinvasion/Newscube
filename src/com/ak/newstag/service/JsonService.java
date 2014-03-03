package com.ak.newstag.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.ak.newstag.model.News;
import com.ak.newstag.network.JsonReader;
import com.google.gson.reflect.TypeToken;

public class JsonService extends IntentService {
	
	private static final String CLAZZ = JsonService.class.getName();

	public static final String URL = "com.ak.url";
	public static final String RECEIVER = "com.ak.receiver";
	public static final String PROGRESS = "com.ak.progress";
	public static final int UPDATE_PROGRESS = 5;
	public static final String UPDATE_DATA = "com.ak.data";

	public JsonService() {
		super("JsonService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ResultReceiver receiver = (ResultReceiver) intent
				.getParcelableExtra(RECEIVER);

		String urlToDownload = intent.getStringExtra(URL);
		Log.d(CLAZZ, urlToDownload);
		getData(receiver, urlToDownload);
	}

	protected void getData(ResultReceiver receiver, String urlToDownload) {
		JsonReader<News> reader = new JsonReader<News>();
		Type listType = new TypeToken<Collection<News>>() {
		}.getType();
		Collection<News> jsonData = reader.getJsonData(urlToDownload, listType);

		sendProgress(receiver, 100, jsonData);
	}

	private void sendProgress(ResultReceiver receiver, int percentage,
			Collection<News> jsonData) {
		Bundle resultData = new Bundle();
		resultData.putInt(PROGRESS, percentage);
		if (jsonData != null) {
			ArrayList<News> parcels = new ArrayList<News>();
			parcels.addAll(jsonData);
			resultData.putParcelableArrayList(UPDATE_DATA, parcels);
		}
		receiver.send(UPDATE_PROGRESS, resultData);
	}
}