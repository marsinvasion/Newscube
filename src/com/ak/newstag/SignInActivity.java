package com.ak.newstag;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ak.newstag.exception.NotFoundException;
import com.ak.newstag.exception.UnAuthorizedException;
import com.ak.newstag.network.HttpPut;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.PlusClient;

public class SignInActivity extends BaseActivity implements
		ConnectionCallbacks, OnConnectionFailedListener {

	private static final String TAG = "SignInActivity";

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;

	private static final String REGISTER_URL = BASE_URL + "registerDevice";
	public static final String UNREGISTER_URL = BASE_URL + "unregisterDevice";

	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private static final String SENDER_ID = "436142318152";

	private GoogleCloudMessaging gcm;
	private Context context;

	String regid;

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences();
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences() {
		return getSharedPreferences(BaseActivity.MY_PREF, MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		new AsyncTask<String, Object, String>() {

			@Override
			protected String doInBackground(String... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					sendRegistrationIdToBackend();
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}
		}.execute(null, null, null);
	}

	private void sendRegistrationIdToBackend() {
		String json = "{ \"regid\":\"" + regid + "\" }";
		try {
			HttpPut.put(REGISTER_URL, BaseActivity.person, json);
			storeRegistrationId();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnAuthorizedException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

	}

	private void storeRegistrationId() {
		final SharedPreferences prefs = getGCMPreferences();
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regid);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();

		SharedPreferences pref = getSharedPreferences(BaseActivity.MY_PREF,
				MODE_PRIVATE);
		String id = pref.getString(BaseActivity.GOOGLE_ID, null);
		if (id == null) {
			mPlusClient = new PlusClient.Builder(this, this, this).setScopes(
					Scopes.PLUS_LOGIN).build();
			mConnectionProgressDialog = new ProgressDialog(this);
			mConnectionProgressDialog
					.setMessage(getString(R.string.signing_in));
			mConnectionProgressDialog.show();
			mPlusClient.connect();
		} else {
			checkForGoogleRegId();
			countryPref();
		}
	}

	private void checkForGoogleRegId() {
		// Check device for Play Services APK.
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		mPlusClient.disconnect();
		if (mConnectionProgressDialog != null) {
			mConnectionProgressDialog.dismiss();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
			} catch (SendIntentException e) {
				mPlusClient.connect();
			}
		} else {
			Toast toast = Toast.makeText(this, R.string.error_google,
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == REQUEST_CODE_RESOLVE_ERR
				&& responseCode == RESULT_OK) {
			mPlusClient.connect();
		} else if (requestCode == REQUEST_CODE_RESOLVE_ERR
				&& responseCode == RESULT_CANCELED) {
			finish();
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		SharedPreferences pref = getSharedPreferences(BaseActivity.MY_PREF,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		String id = mPlusClient.getCurrentPerson().getId();
		editor.putString(BaseActivity.GOOGLE_ID, id);
		String displayName = mPlusClient.getCurrentPerson().getDisplayName();
		editor.putString(BaseActivity.GOOGLE_DISPLAY_NAME, displayName);
		String firstName = mPlusClient.getCurrentPerson().getName()
				.getGivenName();
		editor.putString(BaseActivity.GOOGLE_FIRST_NAME, firstName);
		String lastName = mPlusClient.getCurrentPerson().getName()
				.getFamilyName();
		editor.putString(BaseActivity.GOOGLE_LAST_NAME, lastName);
		String accountName = mPlusClient.getAccountName();
		editor.putString(BaseActivity.GOOGLE_ACCOUNT_NAME, accountName);
		editor.commit();
		BaseActivity.createPerson(id, displayName, firstName, lastName,
				accountName);
		checkForGoogleRegId();
		countryPref();
	}

	@Override
	public void onDisconnected() {
		Log.d(TAG, "disconnected");
	}

}
