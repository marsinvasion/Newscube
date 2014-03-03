package com.ak.newstag;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;

import com.ak.newstag.util.SystemUiHider;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
@SuppressLint("SetJavaScriptEnabled")
public class BrowserActivity extends Activity {

	private static final String TAG = "BrowserActivity";

	private static final String MENU = "MENU";

	private BrowserActivity activity;

	private ProgressDialog mProgressDialog;

	private WebView myWebView;

	private String url;

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	protected static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	protected static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	protected static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	protected SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_browser);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		Bundle extras = getIntent().getExtras();
		url = extras.getString("url");
		myWebView = (WebView) findViewById(R.id.browser);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setBuiltInZoomControls(true);
		myWebView.setWebViewClient(new MyWebViewClient(){
			 @Override
		        public boolean shouldOverrideUrlLoading(WebView view, String url) {
		           // Here put your code
		              Log.d("My Webview", url);

		           // return true; //Indicates WebView to NOT load the url;
		              return false; //Allow WebView to load url
		        }
		});
		loadUrl();

		systemUiHider(controlsView, myWebView);

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		View closeButton = findViewById(R.id.close_button);
		closeButton.setOnTouchListener(mDelayHideTouchListener);
		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.finish();
			}
		});
	}

	private void loadUrl() {
		showSpinner();
		myWebView.loadUrl(url);
	}

	private void showSpinner() {
		mProgressDialog = new ProgressDialog(activity);
		mProgressDialog.setMessage(getString(R.string.loading));
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.browser_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		EasyTracker easyTracker = EasyTracker.getInstance(this);

		switch (item.getItemId()) {
		case R.id.refresh:
			easyTracker.send(MapBuilder.createEvent(TAG, MENU, "refresh",
					null).build());
			loadUrl();
			return true;
		case R.id.send_mail:
			easyTracker.send(MapBuilder.createEvent(TAG, MENU, "send_mail",
					null).build());
			sendEmail(
					new String[] { "" },
					"Newspin",
					"Newspin is a rad application",
					"What are your thoughts about "
							+ url
							+ "\n\n\n\n"
							+ "My life hasn't been the same since Newspin. Download Newspin from the google play store at https://play.google.com/store/apps/details?id=com.ak.newstag");
			return true;
		case R.id.rate_app:
			easyTracker.send(MapBuilder.createEvent(TAG, MENU, "rate_app",
					null).build());
			rateApp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void systemUiHider(final View controlsView, View myWebView) {
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider
				.getInstance(this, myWebView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	protected void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView webview, String url) {
			super.onPageFinished(webview, url);
			mProgressDialog.dismiss();
		}
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

}
