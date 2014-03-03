package com.ak.newstag;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ak.newstag.model.News;
import com.ak.newstag.service.JsonService;
import com.ak.newstag.util.FillLayout;

public class CommentTreeActivity extends INNewsActivity {

	private static final String COMMENT_TREE = BASE_URL + "commentId/";

	private ProgressDialog mProgressDialog;

	private Activity activity;

	private View view;

	protected LayoutInflater inflater = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activity = this;
		inflater = activity.getLayoutInflater();
		displayDialog();
		setContentView(R.layout.comments_layout);
		view = findViewById(R.id.comments_layout);
		Intent serviceIntent = new Intent(this, JsonService.class);
		Bundle extras = getIntent().getExtras();
		String commentId = extras.getString("commentId");
		String commentUrl = COMMENT_TREE + commentId;
		serviceIntent.putExtra(JsonService.URL, commentUrl);
		serviceIntent.putExtra(JsonService.RECEIVER, new JsonReceiver(
				new Handler()));
		startService(serviceIntent);
	}

	private void displayDialog() {
		mProgressDialog = new ProgressDialog(activity);
		mProgressDialog.setMessage(getString(R.string.get_comments));
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
	}

	private class JsonReceiver extends ResultReceiver {

		public JsonReceiver(Handler handler) {
			super(handler);
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
					if (list == null) {
						// Some error retrieving news
						Toast toast = Toast.makeText(activity,
								R.string.news_error, Toast.LENGTH_LONG);
						toast.show();
					} else if (list.isEmpty()) {
						Toast toast = Toast.makeText(activity,
								R.string.nothing_found, Toast.LENGTH_LONG);
						toast.show();
					} else {
						FillLayout.populateCommentLayout(list.get(0), view,
								inflater, activity);
					}
				}
			}
		}
	}

}
