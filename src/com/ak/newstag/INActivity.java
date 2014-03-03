package com.ak.newstag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.newstag.model.News;
import com.ak.newstag.model.Tag;
import com.ak.newstag.receiver.PutReceiver;
import com.ak.newstag.service.JsonService;
import com.ak.newstag.service.PutService;
import com.ak.newstag.service.TagJsonService;
import com.ak.newstag.util.FillLayout;
import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;

public abstract class INActivity extends BaseActivity implements
		CommentDialogFragment.CommentDialogListener {

	private static final String SAVED_NEWS = "saved_news";

	private JazzyViewPager vpage;

	protected LayoutInflater inflater = null;

	private static final String CURRENT_ID = "CURRENT_ID";

	private ProgressDialog mProgressDialog;

	private List<News> newsList = new ArrayList<News>();

	private List<Tag> tagList = new ArrayList<Tag>();

	protected INActivity activity;

	private View lastView;

	protected String currentNews;

	private String currentId = null;

	private int newsStart = 0;

	private int newsCount = 9;

	private int tagStart = 0;

	private int tagCount = 49;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize();
		setCountry(INDIA);
		activity = this;
		setSelectedNewsUrl(getTodaysNewsUrl());
		inflater = activity.getLayoutInflater();
		showOverLay();
		if (savedInstanceState != null) {
			restoreFromSession(savedInstanceState);
		} else {
			retrieveNewsShowDialog();
		}
		setupView();
		setCurrentPage(currentId);
	}

	protected void restoreFromSession(Bundle savedInstanceState) {
		newsList = savedInstanceState.getParcelableArrayList(SAVED_NEWS);
		currentId = savedInstanceState.getString(CURRENT_ID);
	}

	private void startTagJsonService() {
		displayDialog(R.string.get_tags);
		Intent serviceIntent = new Intent(activity, TagJsonService.class);
		String url = getTagUrl() + "/" + tagStart + "/" + (tagStart + tagCount);
		serviceIntent.putExtra(JsonService.URL, url);
		serviceIntent.putExtra(JsonService.RECEIVER, new TagJsonReceiver(
				new Handler()));
		startService(serviceIntent);
	}

	protected abstract String getTagUrl();

	protected void setupView() {
		setContentView(R.layout.activity_main);
		setupPager(TransitionEffect.CubeOut);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item);
		mSpinnerAdapter.addAll(getCategories());
		actionBar.setListNavigationCallbacks(mSpinnerAdapter,
				new ActionBar.OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition,
							long itemId) {
						if (itemPosition > 0)
							loadINActivity(getCategoryCode(itemPosition));
						return true;
					}
				});
	}

	protected abstract int getCategoryCode(int itemPosition);
	
	protected abstract Collection<? extends String> getCategories();

	public void retrieveNewsShowDialog() {
		displayDialog(R.string.get_articles);
		startNewsService(false);
	}

	private void displayDialog(int message) {
		mProgressDialog = new ProgressDialog(activity);
		mProgressDialog.setMessage(getString(message));
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
	}

	private void startNewsService(boolean append) {
		Intent serviceIntent = new Intent(activity, JsonService.class);
		String newsUrl = getSelectedNewsUrl() + "/" + newsStart + "/"
				+ (newsStart + newsCount);
		serviceIntent.putExtra(JsonService.URL, newsUrl);
		serviceIntent.putExtra(JsonService.RECEIVER, new JsonReceiver(
				new Handler(), append));
		startService(serviceIntent);
	}

	private void setupPager(TransitionEffect effect) {
		vpage = (JazzyViewPager) findViewById(R.id.jazzy_pager);
		vpage.setTransitionEffect(effect);
		vpage.setPageMargin(0);
	}

	protected void setCurrentPage(String pageId) {
		vpage.setAdapter(new MainAdapter());
		if (pageId != null) {
			for (int i = 0; i < newsList.size(); i++) {
				if (pageId.equals(newsList.get(i).getId())) {
					vpage.setCurrentItem(i - 1);
					break;
				}
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		ArrayList<News> parcelableNews = new ArrayList<News>();
		parcelableNews.addAll(newsList);
		outState.putParcelableArrayList(SAVED_NEWS, parcelableNews);
		outState.putString(CURRENT_ID, currentId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(getMenuId(), menu);
		if (person != null) {
			menu.add(getString(R.string.signed_in) + " "
					+ person.getAccountName());
			menu.add(getString(R.string.sign_out));
		}
		return super.onCreateOptionsMenu(menu);
	}

	protected abstract int getMenuId();

	protected void loadTags() {
		if (tagList == null || tagList.isEmpty()) {
			startTagJsonService();
		} else {
			displayTags();
		}
	}

	public void refresh() {
		String pageId = currentId;
		newsStart = 0;
		tagStart = 0;
		if (lastView != null) {
			ViewGroup vw = (ViewGroup) lastView.getParent();
			if (vw != null)
				vw.removeAllViewsInLayout();
		}
		retrieveNewsShowDialog();
		setupView();
		setCurrentPage(pageId);
	}

	private void displayTags() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth(); // targeting api 11. Will change to
										// point once we upgrade to 13
		int height = display.getHeight();

		TagCloudLayout mTagCloudView = new TagCloudLayout(activity, width,
				height, tagList, getTagUrl()); // passing current context
		setContentView(mTagCloudView);
		mTagCloudView.requestFocus();
		mTagCloudView.setFocusableInTouchMode(true);
	}

	public String getSelectedNewsUrl() {
		return selectedNewsUrl;
	}

	protected void showOverLay() {
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		int orientation = display.getRotation();
		switch (orientation) {
		case Surface.ROTATION_0:
			SharedPreferences pref = getSharedPreferences(MY_PREF, MODE_PRIVATE);

			if (pref.getBoolean("firststart", true)) {
				// update sharedpreference - another start wont be the first
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean("firststart", false);
				editor.commit(); // apply changes
				final Dialog dialog = new Dialog(activity, R.style.Translucent);
				dialog.setContentView(R.layout.overlay_view);
				LinearLayout layout = (LinearLayout) dialog
						.findViewById(R.id.overlayLayout);
				layout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onPause() {
		super.onPause();

		if (mProgressDialog != null)
			mProgressDialog.dismiss();
		mProgressDialog = null;
		unregisterReceiver(receiver);
	}

	protected abstract String getTodaysNewsUrl();

	private class MainAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			News news = newsList.get(position);
			currentId = news.getId();
			currentNews = news.getUrl();
			int orientation = getResources().getConfiguration().orientation;
			View view = null;
			if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
				view = landscape(container, news);
			} else {
				view = portrait(container, news);
			}
			container.addView(view);
			vpage.setObjectForPosition(view, position);
			if (newsList.size() - position == 5) {
				newsStart = newsStart + newsCount + 1;
				startNewsService(true);
			}
			lastView = view;
			return view;
		}

		private View landscape(ViewGroup container, final News news) {
			View view = inflater.inflate(R.layout.comments_layout, null);
			FillLayout.populateCommentLayout(news, view, inflater, activity);

			return view;
		}

		private View portrait(ViewGroup container, final News news) {
			View view = inflater.inflate(R.layout.news, null);
			FillLayout
					.populateNewsSummaryLayout(news, view, inflater, activity);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			container.removeView((View) obj);
		}

		@Override
		public int getCount() {
			return newsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	private class JsonReceiver extends ResultReceiver {
		private boolean append = false;

		public JsonReceiver(Handler handler, boolean append) {
			super(handler);
			this.append = append;
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			if (resultCode == JsonService.UPDATE_PROGRESS) {
				int progress = resultData.getInt(JsonService.PROGRESS);
				if (progress == 100) {
					ArrayList<News> list = resultData
							.getParcelableArrayList(JsonService.UPDATE_DATA);
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					if (list != null && !list.isEmpty()) {
						if (append) {
							newsList.addAll(list);
							vpage.getAdapter().notifyDataSetChanged();
						} else {
							newsList = list;
							setCurrentPage(currentId);
						}

					}
				}
			}
		}
	}

	private class TagJsonReceiver extends ResultReceiver {

		public TagJsonReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			if (resultCode == JsonService.UPDATE_PROGRESS) {
				int progress = resultData.getInt(JsonService.PROGRESS);
				if (progress == 100) {
					tagList = resultData
							.getParcelableArrayList(JsonService.UPDATE_DATA);
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					if (tagList == null || tagList.isEmpty()) {
						// Some error retrieving news
						Toast toast = Toast.makeText(activity,
								R.string.news_error, Toast.LENGTH_LONG);
						toast.show();
					} else {
						displayTags();
					}
				}
			}
		}
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				int result = bundle.getInt(PutService.PUT_RESULT);
				switch (result) {
				case PutService.UNAUTHORIZED:
					Toast toast = Toast.makeText(activity, R.string.log_out_in,
							Toast.LENGTH_LONG);
					toast.show();
					break;
				case PutService.NOT_FOUND:
					toast = Toast.makeText(activity,
							R.string.resource_not_found, Toast.LENGTH_LONG);
					toast.show();
					break;
				case PutService.ERROR:
					toast = Toast.makeText(activity, R.string.put_error,
							Toast.LENGTH_LONG);
					toast.show();
					break;
				default:
					toast = Toast.makeText(activity, R.string.success,
							Toast.LENGTH_SHORT);
					toast.show();
					refresh();
				}
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver,
				new IntentFilter(PutService.PUT_NOTIFICATION));
	}

	@Override
	public void onDialogPositiveClick(DialogFragment fragment) {
		Dialog dialog = fragment.getDialog();
		EditText text = (EditText) dialog.findViewById(R.id.edit_comment);
		String comment = text.getText().toString();
		String headId = ((TextView) dialog.findViewById(R.id.headId)).getText()
				.toString();
		if (comment != null && !comment.isEmpty()) {
			String json = "{ \"head\":\"" + headId + "\", \"comment\":\""
					+ comment + "\" }";
			Intent serviceIntent = new Intent(activity, PutService.class);
			serviceIntent.putExtra(PutService.PUT_URL, getCommentUrl());
			serviceIntent.putExtra(PutService.JSON, json);
			serviceIntent.putExtra(PutService.PUT_RECEIVER, new PutReceiver(
					new Handler(), activity));
			startService(serviceIntent);
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// do nothing
	}

	protected abstract String getCommentUrl();

}
