package com.tddrampup.yellowpages.injection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import android.app.Notification;

import com.tddrampup.testing.RobolectricTestRunnerWithInjection;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class NotificationDetailsNotificationCreatorTest {

	NotificationDetailsNotificationCreator notificationDetailsCreator;

	@Before
	public void setup() {
		notificationDetailsCreator = new NotificationDetailsNotificationCreator(
				Robolectric.application);
	}

	@Test
	public void correctlyBuildsNotification() {
		Notification notification = notificationDetailsCreator
				.createNotification("test title", "test content", 1);

		Assert.assertEquals(1, notification.icon);
		Assert.assertNotNull(notification.contentIntent);
	}
}
