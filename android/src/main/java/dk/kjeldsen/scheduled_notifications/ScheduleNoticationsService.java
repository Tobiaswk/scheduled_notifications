package dk.kjeldsen.scheduled_notifications;

import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.Log;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class ScheduleNoticationsService extends Service {

	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(ScheduleNoticationsService.class.getName(), "Service start");
		if (intent != null) { // notifications
			// enabled, if no,
			// unregister scheduleNotification and
			// return
			String tickerText = intent.getExtras().getString("ticker");
			String contentTitle = intent.getExtras().getString("contentTitle");
			String content = intent.getExtras().getString("content");

			ScheduleNoficationsPersistentManager.instance().load(
					getApplicationContext());

			ScheduleNotification scheduleNotification = ScheduleNoficationsPersistentManager.instance().getAlarm(
					tickerText);

			if (scheduleNotification != null) {

				Intent notificationIntent = new Intent(
						this.getApplicationContext(), NotificationActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(
						this.getApplicationContext(), 0, notificationIntent, 0);

				NotificationManager mNotificationManager = (NotificationManager) this
						.getApplicationContext().getSystemService(
								Context.NOTIFICATION_SERVICE);

				Notification.Builder builder = new Notification.Builder(
						this.getApplicationContext());

				String notificationSound = PreferenceManager
						.getDefaultSharedPreferences(
								this.getApplicationContext()).getString(
								"notificationSound", null);

				//builder.setContentIntent(contentIntent)
				try {
					Drawable icon = getApplicationContext().getPackageManager().getApplicationIcon(getApplicationContext().getPackageName());
					builder.setContentIntent(contentIntent)
							.setSmallIcon(Icon.createWithBitmap(((BitmapDrawable)icon).getBitmap()))
							.setTicker(tickerText)
							.setWhen(System.currentTimeMillis())
							.setAutoCancel(true)
							.setContentTitle(contentTitle)
							.setContentText(content)
							.setDefaults(
									Notification.DEFAULT_VIBRATE
											| Notification.DEFAULT_LIGHTS)
							.setSound(
									(notificationSound == null ? RingtoneManager
											.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
											: Uri.parse(notificationSound)));
					Notification n = builder.getNotification();

					mNotificationManager
							.notify((int) System.currentTimeMillis(), n);
				} catch (PackageManager.NameNotFoundException e) {
					e.printStackTrace();
				}

			}

			ScheduleNoficationsPersistentManager.instance().unregister(
					tickerText,
					this.getApplicationContext());
		}
		return START_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
