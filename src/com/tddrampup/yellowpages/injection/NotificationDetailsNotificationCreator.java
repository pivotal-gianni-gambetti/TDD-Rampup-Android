package com.tddrampup.yellowpages.injection;

import roboguice.inject.ContextSingleton;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.inject.Inject;
import com.tddrampup.yellowpages.activities.NotificationDetailsActivity;
import com.tddrampup.yellowpages.activities.NotificationDetailsActivity.NotificationCreator;

@ContextSingleton
public class NotificationDetailsNotificationCreator implements
		NotificationCreator {

	Context context;

	@Inject
	public NotificationDetailsNotificationCreator(Context context) {
		this.context = context;
	}

	@Override
	public Notification createNotification(String title, String content,
			int icon) {

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				context);

		notificationBuilder.setContentTitle(title);
		notificationBuilder.setContentText(content);

		notificationBuilder.setSmallIcon(icon);

		notificationBuilder.setAutoCancel(true);

		Intent resultIntent = NotificationDetailsActivity.getStartIntent(
				context, title, content);

		// The stack builder object will contain an artificial back stack for
		// the started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(NotificationDetailsActivity.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setContentIntent(resultPendingIntent);

		return notificationBuilder.build();
	}
}
