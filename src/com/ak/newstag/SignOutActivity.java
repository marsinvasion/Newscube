package com.ak.newstag;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.ak.newstag.exception.NotFoundException;
import com.ak.newstag.exception.UnAuthorizedException;
import com.ak.newstag.network.HttpPut;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;

public class SignOutActivity extends BaseActivity implements ConnectionCallbacks,
		OnConnectionFailedListener {

	private static final String TAG = "SignOutActivity";

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private SignOutActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		mConnectionProgressDialog = new ProgressDialog(activity);
		mConnectionProgressDialog.setMessage(getString(R.string.signing_out));
		mConnectionProgressDialog.show();
		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				String msg = "";
				SharedPreferences sharedPreferences = getSharedPreferences(
						BaseActivity.MY_PREF, MODE_PRIVATE);
				String registrationId = sharedPreferences.getString(
						SignInActivity.PROPERTY_REG_ID, "");
				if (!registrationId.isEmpty()) {
					unregisterDevice(registrationId, sharedPreferences);
				}
				mPlusClient = new PlusClient.Builder(activity, activity, activity)
						.setScopes(Scopes.PLUS_LOGIN).build();
				mPlusClient.connect();
				return msg;
			}
		}.execute(null, null, null);
		
	}

	private void unregisterDevice(String regid,
			SharedPreferences sharedPreferences) {
		String json = "{ \"regid\":\"" + regid + "\" }";
		try {
			HttpPut.put(SignInActivity.UNREGISTER_URL, BaseActivity.person, json);
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
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == REQUEST_CODE_RESOLVE_ERR
				&& responseCode == RESULT_OK) {
			mPlusClient.connect();
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		SharedPreferences pref = getSharedPreferences(BaseActivity.MY_PREF,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
		if (mPlusClient.isConnected()) {
			mPlusClient.clearDefaultAccount();
			mPlusClient
					.revokeAccessAndDisconnect(new OnAccessRevokedListener() {
						@Override
						public void onAccessRevoked(ConnectionResult status) {
							Log.d(TAG, "access revoked");
						}
					});
			mPlusClient.disconnect();
		}
		finish();
	}

	@Override
	public void onDisconnected() {
		Log.d(TAG, "disconnected");
	}

}
