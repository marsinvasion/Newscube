package com.ak.newstag.service;

import android.app.IntentService;
import android.content.Intent;

import com.ak.newstag.BaseActivity;
import com.ak.newstag.exception.NotFoundException;
import com.ak.newstag.exception.UnAuthorizedException;
import com.ak.newstag.model.Person;
import com.ak.newstag.network.HttpPut;

public class PutService extends IntentService {

	public static final String PUT_RECEIVER = "com.ak.newstag.putReceiver";
	public static final String PUT_URL = "com.ak.newstag.putUrl";
	public static final String PERSON = "com.ak.newstag.putPerson";
	public static final String JSON = "com.ak.newstag.putJson";
	public static final int PUT_PROGRESS = 10;
	public static final String PUT_RESULT = "com.ak.newstag.putResult";

	public static final int SUCCESS = 100;
	public static final int UNAUTHORIZED = 404;
	public static final int ERROR = -1;
	public static final int NOT_FOUND = 201;

	public static final String PUT_NOTIFICATION = "com.ak.newstag.putNotification";

	public PutService() {
		super("PutService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String url = intent.getStringExtra(PUT_URL);
		Person person = BaseActivity.person;
		String json = intent.getStringExtra(JSON);
		int result = SUCCESS;
		try {
			HttpPut.put(url, person, json);
		} catch (UnAuthorizedException e) {
			result = UNAUTHORIZED;
		} catch (NotFoundException e) {
			result = NOT_FOUND;
		} catch (Exception e) {
			result = ERROR;
		}
		Intent resultIntent = new Intent(PUT_NOTIFICATION);
		resultIntent.putExtra(PUT_RESULT, result);
		sendBroadcast(resultIntent);
	}

}
