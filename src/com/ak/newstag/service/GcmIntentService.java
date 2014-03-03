package com.ak.newstag.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ak.newstag.CommentTreeActivity;
import com.ak.newstag.R;
import com.ak.newstag.receiver.GcmBroadcastReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {

	private static final String TAG = "GcmIntentService";

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				String comment = extras.getString("comment");
				String display = extras.getString("display");
				String commentId = extras.getString("commentId");
				sendNotification(comment, display, commentId);
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg, String display, String commentId) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent resultIntent = new Intent(this, CommentTreeActivity.class);
		resultIntent.putExtra("commentId", commentId);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		PendingIntent notifyIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(display)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg).setAutoCancel(true);

		mBuilder.setContentIntent(notifyIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}