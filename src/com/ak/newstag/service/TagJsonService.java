package com.ak.newstag.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import android.os.Bundle;
import android.os.ResultReceiver;

import com.ak.newstag.model.Tag;
import com.ak.newstag.network.JsonReader;
import com.google.gson.reflect.TypeToken;

public class TagJsonService extends JsonService {
	
	@Override
	protected void getData(ResultReceiver receiver, String urlToDownload) {
		JsonReader<Tag> reader = new JsonReader<Tag>();
		Type listType = new TypeToken<Collection<Tag>>() {
		}.getType();
		Collection<Tag> jsonData = reader.getJsonData(urlToDownload, listType);

		sendProgress(receiver, 100, jsonData);
	}

	private void sendProgress(ResultReceiver receiver, int percentage,
			Collection<Tag> jsonData) {
		Bundle resultData = new Bundle();
		resultData.putInt(PROGRESS, percentage);
		if (jsonData != null) {
			ArrayList<Tag> parcels = new ArrayList<Tag>();
			parcels.addAll(jsonData);
			resultData.putParcelableArrayList(UPDATE_DATA, parcels);
		}
		receiver.send(UPDATE_PROGRESS, resultData);
	}

}
