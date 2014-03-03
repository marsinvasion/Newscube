package com.ak.newstag;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class INNewsActivity extends INActivity implements
		CommentDialogFragment.CommentDialogListener {
	
	private static final String TAG = "INNewsActivity";

	private static final String MENU = "MENU";

	private static final String TAG_URL = BASE_URL + "IN/news/tag";
	private static final String TODAYS_NEWS = BASE_URL + "IN/news/today";
	private static final String TRENDING_NEWS = BASE_URL + "IN/news/trending";

	private static final String ASIAN_AGE = BASE_URL
			+ "IN/news/website/Asian%20Age";
	private static final String BBC_India = BASE_URL
			+ "IN/news/website/BBC%20India";
	private static final String Central_Chronicle = BASE_URL
			+ "IN/news/website/Central%20Chronicle";
	private static final String CNN_IBN = BASE_URL + "IN/news/website/CNN-IBN";
	private static final String DNA = BASE_URL + "IN/news/website/DNA";
	private static final String Deccan_Herald = BASE_URL
			+ "IN/news/website/Deccan%20Herald";
	private static final String Hindu = BASE_URL + "IN/news/website/Hindu";
	private static final String Hindustan_Times = BASE_URL
			+ "IN/news/website/Hindustan%20Times";
	private static final String India_Today = BASE_URL
			+ "IN/news/website/India%20Today";
	private static final String Indian_Express = BASE_URL
			+ "IN/news/website/Indian%20Express";
	private static final String Mid_day = BASE_URL + "IN/news/website/Mid-day";
	private static final String NDTV = BASE_URL + "IN/news/website/NDTV";
	private static final String One_India = BASE_URL
			+ "IN/news/website/One%20India";
	private static final String Outlook_India = BASE_URL
			+ "IN/news/website/Outlook%20India";
	private static final String Rediff = BASE_URL + "IN/news/website/Rediff";
	private static final String Times_of_India = BASE_URL
			+ "IN/news/website/Times%20of%20India";
	private static final String Telegraph = BASE_URL
			+ "IN/news/website/Telegraph";
	private static final String Reuters_India = BASE_URL
			+ "IN/news/website/Reuters%20India";
	private static final String Zee_news = BASE_URL
			+ "IN/news/website/Zee%20news";
	private static final String FIRST_POST = BASE_URL
			+ "IN/news/website/First%20Post";

	private static final String COMMENT_URL = BASE_URL + "IN/news/comment";

	private static final String[] category_array = new String[] { NEWS, ENTERTAINMENT,
			FINANCE, TECH, SPORTS };

	private static final LinkedList<String> categories = new LinkedList<String>(
			Arrays.asList(category_array));

	private static final int[] category_codes = new int[] { NEWS_CATEGORY,
			ENTERTAINMENT_CATEGORY, FINANCE_CATEGORY, TECH_CATEGORY,
			SPORTS_CATEGORY };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected String getTodaysNewsUrl() {
		return TODAYS_NEWS;
	}

	@Override
	protected int getMenuId() {
		return R.menu.in_news;
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
			case R.id.asian_age:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "asian_age",
						null).build());
				setSelectedNewsUrl(ASIAN_AGE);
				refresh();
				return true;
			case R.id.bbc_india:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "bbc_india",
						null).build());
				setSelectedNewsUrl(BBC_India);
				refresh();
				return true;
			case R.id.central_chronicle:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "central_chronicle",
						null).build());
				setSelectedNewsUrl(Central_Chronicle);
				refresh();
				return true;
			case R.id.cnn_ibn:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "cnn_ibn",
						null).build());
				setSelectedNewsUrl(CNN_IBN);
				refresh();
				return true;
			case R.id.deccan_herald:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "deccan_herald",
						null).build());
				setSelectedNewsUrl(Deccan_Herald);
				refresh();
				return true;
			case R.id.dna:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "dna",
						null).build());
				setSelectedNewsUrl(DNA);
				refresh();
				return true;
			case R.id.first_post:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "first_post",
						null).build());
				setSelectedNewsUrl(FIRST_POST);
				refresh();
				return true;
			case R.id.hindu:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "hindu",
						null).build());
				setSelectedNewsUrl(Hindu);
				refresh();
				return true;
			case R.id.hindustan_times:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "hindustan_times",
						null).build());
				setSelectedNewsUrl(Hindustan_Times);
				refresh();
				return true;
			case R.id.india_today:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "india_today",
						null).build());
				setSelectedNewsUrl(India_Today);
				refresh();
				return true;
			case R.id.indian_express:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "indian_express",
						null).build());
				setSelectedNewsUrl(Indian_Express);
				refresh();
				return true;
			case R.id.mid_day:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "mid_day",
						null).build());
				setSelectedNewsUrl(Mid_day);
				refresh();
				return true;
			case R.id.ndtv:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "ndtv",
						null).build());
				setSelectedNewsUrl(NDTV);
				refresh();
				return true;
			case R.id.one_india:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "one_india",
						null).build());
				setSelectedNewsUrl(One_India);
				refresh();
				return true;
			case R.id.outlook_india:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "outlook_india",
						null).build());
				setSelectedNewsUrl(Outlook_India);
				refresh();
				return true;
			case R.id.rediff:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "rediff",
						null).build());
				setSelectedNewsUrl(Rediff);
				refresh();
				return true;
			case R.id.reuters_india:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "reuters_india",
						null).build());
				setSelectedNewsUrl(Reuters_India);
				refresh();
				return true;
			case R.id.telegraph:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "telegraph",
						null).build());
				setSelectedNewsUrl(Telegraph);
				refresh();
				return true;
			case R.id.times_of_india:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "times_of_india",
						null).build());
				setSelectedNewsUrl(Times_of_India);
				refresh();
				return true;
			case R.id.zee_news:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "zee_news",
						null).build());
				setSelectedNewsUrl(Zee_news);
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
