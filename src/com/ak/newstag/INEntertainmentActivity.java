package com.ak.newstag;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class INEntertainmentActivity extends INActivity {

	private static final String TAG = "INEntertainmentActivity";

	private static final String MENU = "MENU";
	
	private static final String TODAYS_NEWS = BASE_URL
			+ "IN/entertainment/today";
	private static final String TRENDING_NEWS = BASE_URL
			+ "IN/entertainment/trending";

	private static final String DNA_Entertainment = BASE_URL
			+ "IN/entertainment/website/DNA%20Entertainment";
	private static final String Glam_Sham = BASE_URL
			+ "IN/entertainment/website/GlamSham";
	private static final String Indian_Express_Entertainment = BASE_URL
			+ "IN/entertainment/website/Indian%20Express%20Entertainment";
	private static final String Mid_day_Entertainment = BASE_URL
			+ "IN/entertainment/website/Mid%20day%20Entertainment";
	private static final String Reuters_India = BASE_URL
			+ "IN/entertainment/website/Reuters%20India%20Entertainment";
	private static final String Times_of_India = BASE_URL
			+ "IN/entertainment/website/Times%20of%20India%20Entertainment";

	private static final String TAG_URL = BASE_URL + "IN/entertainment/tag";
	private static final String COMMENT_URL = BASE_URL
			+ "IN/entertainment/comment";

	private static final String[] category_array = new String[]{
			ENTERTAINMENT,
			NEWS,
			FINANCE,
			TECH,
			SPORTS
	};
	
	private static final LinkedList<String> categories = new LinkedList<String>(Arrays.asList(category_array));

	private static final int[] category_codes = new int[] { ENTERTAINMENT_CATEGORY, NEWS_CATEGORY,
		FINANCE_CATEGORY, TECH_CATEGORY, SPORTS_CATEGORY };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected String getTodaysNewsUrl() {
		return TODAYS_NEWS;
	}

	protected int getMenuId() {
		return R.menu.in_entertainment;
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
			case R.id.dna_entertainment:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "dna_entertainment",
						null).build());
				setSelectedNewsUrl(DNA_Entertainment);
				refresh();
				return true;
			case R.id.glam_sham:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "glam_sham",
						null).build());
				setSelectedNewsUrl(Glam_Sham);
				refresh();
				return true;
			case R.id.indian_express_entertainment:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "indian_express_entertainment",
						null).build());
				setSelectedNewsUrl(Indian_Express_Entertainment);
				refresh();
				return true;
			case R.id.mid_day_entertainment:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "mid_day_entertainment",
						null).build());
				setSelectedNewsUrl(Mid_day_Entertainment);
				refresh();
				return true;
			case R.id.reuters_india_entertainment:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "reuters_india_entertainment",
						null).build());
				setSelectedNewsUrl(Reuters_India);
				refresh();
				return true;
			case R.id.times_of_india_entertainment:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "times_of_india_entertainment",
						null).build());
				setSelectedNewsUrl(Times_of_India);
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
