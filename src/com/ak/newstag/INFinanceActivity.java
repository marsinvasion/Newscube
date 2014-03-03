package com.ak.newstag;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class INFinanceActivity extends INActivity {
	
	private static final String TAG = "INFinanceActivity";

	private static final String MENU = "MENU";

	private static final String TODAYS_NEWS = BASE_URL + "IN/finance/today";
	private static final String TRENDING_NEWS = BASE_URL
			+ "IN/finance/trending";

	private static final String Business_Standard = BASE_URL
			+ "IN/finance/website/Business%20Standard";
	private static final String Economic_Times_of_India = BASE_URL
			+ "IN/finance/website/Economic%20Times%20India";
	private static final String Live_mint = BASE_URL
			+ "IN/finance/website/Live%20Mint";
	private static final String Indian_Express = BASE_URL
			+ "IN/finance/website/Indian%20Express%20Finance";
	private static final String Money_Control = BASE_URL
			+ "IN/finance/website/MoneyControl";
	private static final String HINDUSTAN_TIMES = BASE_URL
			+ "IN/finance/website/Hindustan%20Times%20Business";

	private static final String TAG_URL = BASE_URL + "IN/finance/tag";
	private static final String COMMENT_URL = BASE_URL + "IN/finance/comment";

	private static final String[] category_array = new String[] { FINANCE, NEWS,
			ENTERTAINMENT, TECH, SPORTS };

	private static final LinkedList<String> categories = new LinkedList<String>(
			Arrays.asList(category_array));

	private static final int[] category_codes = new int[] { FINANCE_CATEGORY, NEWS_CATEGORY,
			ENTERTAINMENT_CATEGORY, TECH_CATEGORY, SPORTS_CATEGORY };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected String getTodaysNewsUrl() {
		return TODAYS_NEWS;
	}

	protected int getMenuId() {
		return R.menu.in_finance;
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
			case R.id.business_standard:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "business_standard",
						null).build());
				setSelectedNewsUrl(Business_Standard);
				refresh();
				return true;
			case R.id.economic_times_india:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "economic_times_india",
						null).build());
				setSelectedNewsUrl(Economic_Times_of_India);
				refresh();
				return true;
			case R.id.indian_express_finance:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "indian_express_finance",
						null).build());
				setSelectedNewsUrl(Indian_Express);
				refresh();
				return true;
			case R.id.live_mint:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "live_mint",
						null).build());
				setSelectedNewsUrl(Live_mint);
				refresh();
				return true;
			case R.id.money_control:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "money_control",
						null).build());
				setSelectedNewsUrl(Money_Control);
				refresh();
				return true;
			case R.id.hindustan_times_business:
				easyTracker.send(MapBuilder.createEvent(TAG, MENU, "hindustan_times_business",
						null).build());
				setSelectedNewsUrl(HINDUSTAN_TIMES);
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
