package com.tddrampup.yellowpages.activities;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;
import com.tddrampup.R;
import com.tddrampup.yellowpages.activities.NotificationDetailsActivity.NotificationCreator;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity implements OnClickListener {

	static final String WHAT_QUERY = "what";
	static final String WHERE_QUERY = "where";

	@InjectView(R.id.what) EditText whatField;
	@InjectView(R.id.where) EditText whereField;
	@InjectView(R.id.start_search) Button search;
	@InjectView(R.id.start_map) Button map;

	// for notification stuff
	@InjectView(R.id.launch_notification) Button notification;
	@Inject NotificationCreator notificationCreator;
	@Inject NotificationManager notificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		search.setOnClickListener(this);
		map.setOnClickListener(this);
		notification.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void doSearch() {
		String what = whatField.getText().toString();
		String where = whereField.getText().toString();

		if (what.length() > 0 && where.length() > 0) {
			SearchActivity.start(this, what, where);
		}
	}

	private void doMap() {
		String what = whatField.getText().toString();
		String where = whereField.getText().toString();

		if (what.length() > 0 && where.length() > 0) {
			ClusterMapActivity.start(this, what, where);
		}
	}

	private void doNotify() {
		notificationManager.notify(0, notificationCreator.createNotification(
				"Content title", "content text", R.drawable.ic_launcher));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_search:
			doSearch();
			break;
		case R.id.start_map:
			doMap();
			break;
		case R.id.launch_notification:
			doNotify();
			break;
		}
	}

}
