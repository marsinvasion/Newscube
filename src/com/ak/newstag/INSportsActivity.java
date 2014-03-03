package com.ak.newstag;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class INSportsActivity extends INActivity {
	
	private static final String TAG = "INSportsActivity";

	private static final String MENU = "MENU";

	private static final String TODAYS_NEWS = BASE_URL + "IN/sports/today";
	private static final String TRENDING_NEWS = BASE_URL + "IN/sports/trending";

	private static final String CRICINFO = BASE_URL
			+ "IN/sports/website/Cricinfo";
	private static final String CNN_IBN = BASE_URL
			+ "IN/sports/website/CNN%20IBN%20Sports";
	private static final String INDIAN_EXPRESS = BASE_URL
			+ "IN/sports/website/Indian%20Express%20Sports";
	private static final String HINDUSTAN_TIMES = BASE_URL
			+ "IN/sports/website/Hindustan%20Times%20Sport";
	private static final String TIMES_OF_INDIA = BASE_URL
			+ "IN/sports/website/Times%20of%20India%20Sports";

	private static final String TAG_URL = BASE_URL + "IN/tech/tag";
	private static final String COMMENT_URL = BASE_URL + "IN/tech/comment";

	private static final String[] category_array = new String[] { SPORTS, NEWS,
			ENTERTAINMENT, FINANCE, TECH };

	private static final LinkedList<String> categories = new LinkedList<String>(
			Arrays.asList(category_array));

	private static final int[] category_codes = new int[] { SPORTS_CATEGORY, NEWS_CATEGORY,
			ENTERTAINMENT_CATEGORY, FINANCE_CATEGORY, TECH_CATEGORY };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected String getTodaysNewsUrl() {
		return TODAYS_NEWS;
	}

	protected int getMenuId() {
		return R.menu.in_sports;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		EasyTracker easyTracker = EasyTracker.getInstance(this);
		// Handle presses on the action bar items
		if (getString(R.string.sign_out).equals(item.getTitle())) {
			easyTracker.send(MapBuilder.createEvent(TAG, MENU, "sign_out",
					null).build());
			Intent intent = new Intent(this, SignOutActivity.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			switch (item.getItemId()) {
			case R.id.refresh:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "refresh",
						null).build());
				refresh();
				return true;
			case R.id.latest_news:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "latest_news",
						null).build());
				setSelectedNewsUrl(TODAYS_NEWS);
				refresh();
				return true;
			case R.id.trending_news:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "trending_news",
						null).build());
				setSelectedNewsUrl(TRENDING_NEWS);
				refresh();
				return true;
			case R.id.send_mail:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "send_mail",
						null).build());
				sendEmail(
						new String[] { "" },
						"Newspin",
						"Newspin is a rad application",
						"What are your thoughts about "
								+ currentNews
								+ "\n\n\n\n"
								+ "My life hasn't been the same since Newspin. Download Newspin from the google play store at https://play.google.com/store/apps/details?id=com.ak.newstag");
				return true;
			case R.id.rate_app:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "rate_app",
						null).build());
				rateApp();
				return true;
			case R.id.tag:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "tag",
						null).build());
				loadTags();
				return true;
			case R.id.cricinfo:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "cricinfo",
						null).build());
				setSelectedNewsUrl(CRICINFO);
				refresh();
				return true;
			case R.id.cnn_ibn:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "cnn_ibn",
						null).build());
				setSelectedNewsUrl(CNN_IBN);
				refresh();
				return true;
			case R.id.indian_express:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "indian_express",
						null).build());
				setSelectedNewsUrl(INDIAN_EXPRESS);
				refresh();
				return true;
			case R.id.hindustan_times_sports:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "hindustan_times_sports",
						null).build());
				setSelectedNewsUrl(HINDUSTAN_TIMES);
				refresh();
				return true;
			case R.id.times_of_india_sports:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "times_of_india_sports",
						null).build());
				setSelectedNewsUrl(TIMES_OF_INDIA);
				refresh();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
	}

	@Override
	protected String getTagUrl() {
		return TAG_URL;
	}

	@Override
	protected String getCommentUrl() {
		return COMMENT_URL;
	}

	@Override
	protected Collection<? extends String> getCategories() {
		return categories;
	}

	@Override
	protected int getCategoryCode(int itemPosition) {
		return category_codes[itemPosition];
	}
}
