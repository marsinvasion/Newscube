package com.ak.newstag.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.newstag.BaseActivity;
import com.ak.newstag.BrowserActivity;
import com.ak.newstag.CommentDialogFragment;
import com.ak.newstag.R;
import com.ak.newstag.SignInActivity;
import com.ak.newstag.model.Comment;
import com.ak.newstag.model.News;

public class FillLayout {

	public static void loadUrl(String url, Activity activity) {
		Intent intent = new Intent(activity, BrowserActivity.class);
		intent.putExtra("url", url);
		activity.startActivity(intent);
	}

	public static void populateCommentLayout(final News news, View view,
			LayoutInflater inflater, final Activity activity) {
		// Title
		setTitle(view, news, activity);
		setPublishedTime(view, news);
		setWebsite(view, news);
		if (BaseActivity.person == null) {
			View signInComment = view.findViewById(R.id.news_comment);
			signInComment.setVisibility(View.GONE);
			View newsComment = view.findViewById(R.id.sign_in_comment);
			newsComment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(activity, SignInActivity.class);
					activity.startActivity(intent);
					activity.finish();
				}
			});
		} else {
			View newsComment = view.findViewById(R.id.sign_in_comment);
			newsComment.setVisibility(View.GONE);
			commentDialog(view, news.getId(), R.id.news_comment, activity, null);
		}

		// Comments
		ViewGroup commentView = (ViewGroup) view
				.findViewById(R.id.scroll_comments);
		if (news.getComments() != null && news.getComments().length > 0) {
			View noCommentsView = commentView.findViewById(R.id.no_comments);
			noCommentsView.setVisibility(View.GONE);
			addComments(commentView, news.getComments(), 0, inflater, activity);
		} else {
			commentDialog(view, news.getId(), R.id.no_comments, activity, null);
		}
	}

	public static void populateNewsSummaryLayout(final News news, View view,
			LayoutInflater inflater, final Activity activity) {
		// Title
		setTitle(view, news, activity);
		// Summary
		TextView content = (TextView) view.findViewById(R.id.content);
		content.setText(news.getSummary());
		setPublishedTime(view, news);
		setWebsite(view, news);
		TextView commentCount = (TextView) view.findViewById(R.id.comment_count);
		String text = "";
		if(news.getComments().length > 1){
			text = news.getComments().length + " comments";
		}else{
			text = news.getComments().length + " comment";
		}
		commentCount.setText(text);
		commentCount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(activity, R.string.comments_rotate, Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}

	private static void addComments(ViewGroup commentView, Comment[] comments,
			int level, LayoutInflater inflater, Activity activity) {
		if (comments != null && comments.length > 0) {
			for (Comment comment : comments) {
				ViewGroup view = (ViewGroup) inflater.inflate(
						R.layout.comment_fragment, null);
				View fillerViews[] = new View[] {
						view.findViewById(R.id.filler1),
						view.findViewById(R.id.filler2),
						view.findViewById(R.id.filler3),
						view.findViewById(R.id.filler4),
						view.findViewById(R.id.filler5),
						view.findViewById(R.id.filler6),
						view.findViewById(R.id.filler7),
						view.findViewById(R.id.filler8),
						view.findViewById(R.id.filler9),
						view.findViewById(R.id.filler10), };
				for (int i = level; i < fillerViews.length; i++) {
					fillerViews[i].setVisibility(View.GONE);
				}
				TextView commentText = (TextView) view
						.findViewById(R.id.comment);
				commentText.setText(comment.getComment());
				TextView displayName = (TextView) view
						.findViewById(R.id.display_name);
				if (comment.getDisplayName() != null) {
					displayName.setText(comment.getDisplayName());
				} else {
					displayName.setText(comment.getAccountName());
				}

				TextView commentScore = (TextView) view
						.findViewById(R.id.comment_score);
				if (comment.getScore() > 1) {
					commentScore.setText(comment.getScore() + " points");
				} else {
					commentScore.setText(comment.getScore() + " point");
				}
				TextView commentTime = (TextView) view
						.findViewById(R.id.comment_time);
				if (comment.getPublished() == null) {
					comment.setPublished(getPublishedDate(comment
							.getInserted_at()));
				}
				commentTime.setText(comment.getPublished());
				if (BaseActivity.person == null){
					View commentReply = view.findViewById(R.id.comment_reply);
					commentReply.setVisibility(View.GONE);
				}else{
					commentDialog(view, comment.getId(), R.id.comment_reply,
							activity, comment.getComment());
				}
				commentView.addView(view);
				addComments(commentView, comment.getComments(), level + 1,
						inflater, activity);
			}
		}

	}

	private static void commentDialog(View view, final String headId,
			int commentViewId, final Activity activity, final String comment) {
		View newsComment = view.findViewById(commentViewId);
		newsComment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CommentDialogFragment dialog = new CommentDialogFragment();
				dialog.setHeadId(headId);
				dialog.setHeadComment(comment);
				dialog.show(activity.getFragmentManager(),
						"CommentDialogFragment");

			}
		});
	}

	private static final SimpleDateFormat sdfDate = new SimpleDateFormat(
			"dd/MM/yyyy", Locale.getDefault());
	private static final SimpleDateFormat sdfTime = new SimpleDateFormat(
			"hh:mm aa", Locale.getDefault());

	private static String getPublishedDate(Date date) {
		if (date == null) {
			return "unknown";
		} else {
			Calendar cal = Calendar.getInstance(Locale.getDefault());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0); // mid night

			Calendar publishedCal = Calendar.getInstance(Locale.getDefault());
			publishedCal.setTime(date);
			String published = "";
			if (publishedCal.before(cal)) { // date is before midnight
				published = sdfDate.format(date);
			} else {
				published = sdfTime.format(date);
			}
			return published;
		}
	}

	private static void setTitle(View view, final News news,
			final Activity activity) {
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(news.getTitle());
		title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadUrl(news.getUrl(), activity);
			}
		});
	}

	private static void setWebsite(View view, final News news) {
		// website
		TextView website = (TextView) view.findViewById(R.id.website1);
		website.setText(news.getSource());
	}

	private static void setPublishedTime(View view, final News news) {
		// Published at
		TextView date = (TextView) view.findViewById(R.id.date_published1);
		if (news.getPublished() == null) {
			news.setPublished(getPublishedDate(news.getPublished_at()));
		}
		date.setText(news.getPublished());
	}
}