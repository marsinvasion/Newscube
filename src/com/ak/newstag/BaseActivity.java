package com.ak.newstag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.ak.newstag.model.Person;
import com.google.analytics.tracking.android.EasyTracker;

public abstract class BaseActivity extends Activity {

	protected static final String MY_PREF = "com.ak.mypref";

	protected static final String CATEGORY = "com.ak.category";

	protected static final String COUNTRY = "com.ak.country";

	protected String selectedNewsUrl;

	public static final int INDIA = 1;

	public static final int NEWS_CATEGORY = 1;

	public static final int FINANCE_CATEGORY = 2;

	public static final int ENTERTAINMENT_CATEGORY = 3;

	public static final int TECH_CATEGORY = 4;

	public static final int SPORTS_CATEGORY = 5;

	public static final String NEWS = "News";
	public static final String FINANCE = "Finance";
	public static final String ENTERTAINMENT = "Entertainment";
	public static final String TECH = "Tech";
	public static final String SPORTS = "Sports";

	public static Person person;

	public static final String PERSON_PREF = "com.ak.person";

	public static final String GOOGLE_ID = "com.ak.google.id";

	public static final String GOOGLE_DISPLAY_NAME = "com.ak.google.displayName";

	public static final String GOOGLE_FIRST_NAME = "com.ak.google.firstName";

	public static final String GOOGLE_LAST_NAME = "com.ak.google.lastName";

	public static final String GOOGLE_ACCOUNT_NAME = "com.ak.google.accountName";

	protected static final String BASE_URL = "http://news.anoopkulkarni.com:3000/";

	protected void setCountry(int country) {
		SharedPreferences pref = getSharedPreferences(MY_PREF, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(COUNTRY, country);
		editor.commit(); // apply changes
	}

	protected void setCategory(int category) {
		SharedPreferences pref = getSharedPreferences(MY_PREF, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(CATEGORY, category);
		editor.commit(); // apply changes
	}

	protected void rateApp() {
		startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=com.ak.newstag")));
	}

	protected void otherApps() {
		startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri
				.parse("market://search?q=pub:\"Anoop Kulkarni\"")));
	}

	protected void sendEmail(String[] recipientList, String title,
			String subject, String body) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipientList);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
		startActivity(Intent.createChooser(emailIntent, title));
	}

	public void setSelectedNewsUrl(String url) {
		selectedNewsUrl = url;
	}

	protected void initialize() {
		SharedPreferences pref = getSharedPreferences(BaseActivity.MY_PREF,
				MODE_PRIVATE);
		String id = pref.getString(GOOGLE_ID, null);
		if (id != null && person == null) {
			person = createPerson(pref, id);
		}
	}

	public static Person createPerson(SharedPreferences pref, String id) {
		return createPerson(id, pref.getString(GOOGLE_DISPLAY_NAME, null),
				pref.getString(GOOGLE_FIRST_NAME, null),
				pref.getString(GOOGLE_LAST_NAME, null),
				pref.getString(GOOGLE_ACCOUNT_NAME, null));
	}

	public static Person createPerson(String id, String googleDisplay,
			String firstName, String lastName, String accountName) {
		Person p = new Person();
		p.setId(id).setDisplayName(googleDisplay).setFirstName(firstName)
				.setLastName(lastName).setAccountName(accountName);
		return p;
	}

	protected void loadINActivity(int category) {
		setCategory(category);
		switch (category) {
		case BaseActivity.NEWS_CATEGORY:
			loadCountry(INNewsActivity.class);
			return;
		case BaseActivity.ENTERTAINMENT_CATEGORY:
			loadCountry(INEntertainmentActivity.class);
			return;
		case BaseActivity.FINANCE_CATEGORY:
			loadCountry(INFinanceActivity.class);
			return;
		case BaseActivity.TECH_CATEGORY:
			loadCountry(INTechActivity.class);
			return;
		case BaseActivity.SPORTS_CATEGORY:
			loadCountry(INSportsActivity.class);
			return;
		default:
			loadCountry(INNewsActivity.class);
			return;
		}
	}

	protected void loadCountry(Class<? extends BaseActivity> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		finish();
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	protected void countryPref() {
		SharedPreferences pref = getSharedPreferences(BaseActivity.MY_PREF,
				MODE_PRIVATE);
		switch (pref.getInt(BaseActivity.COUNTRY, 1)) {
		case BaseActivity.INDIA:
			loadINActivity(pref.getInt(BaseActivity.CATEGORY, 1));
			return;
		default:
			loadINActivity(NEWS_CATEGORY);
			return;
		}
	}
}
