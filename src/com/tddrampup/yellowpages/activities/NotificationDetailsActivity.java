package com.tddrampup.yellowpages.activities;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.tddrampup.R;

@ContentView(R.layout.activity_notification_details)
public class NotificationDetailsActivity extends RoboActivity {

	static final String TITLE_PARAM = "notification_title";
	static final String CONTENT_PARAM = "notification_content";

	public interface NotificationCreator {

		Notification createNotification(String title, String content, int icon);
	}

	public static Intent getStartIntent(Context context, String title,
			String content) {
		Intent start = new Intent(context, NotificationDetailsActivity.class);
		start.putExtra(TITLE_PARAM, title);
		start.putExtra(CONTENT_PARAM, content);

		return start;
	}

	@InjectView(R.id.title) TextView title;
	@InjectView(R.id.content) TextView content;

	@InjectExtra(value = TITLE_PARAM) String titleText;
	@InjectExtra(value = CONTENT_PARAM) String contentText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		title.setText(titleText);
		content.setText(contentText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification_details, menu);
		return true;
	}

}
