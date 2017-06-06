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
import android.os.IBinder;

public class ScheduleNotificationsService extends Service {

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(ScheduleNotificationsService.class.getName(), "Service start");
        if (intent != null) {
            String tickerText = intent.getExtras().getString("ticker");
            String contentTitle = intent.getExtras().getString("contentTitle");
            String content = intent.getExtras().getString("content");
            int notificationId = intent.getExtras().getInt("notificationId");

            ScheduleNotification scheduleNotification = ScheduleNotificationsManager.instance(this.getApplicationContext()).get(
                    notificationId);

            if (scheduleNotification != null) {

                Intent notificationIntent = this.getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
                PendingIntent contentIntent = PendingIntent.getActivity(
                        this.getApplicationContext(), 0, notificationIntent, 0);

                NotificationManager mNotificationManager = (NotificationManager) this
                        .getApplicationContext().getSystemService(
                                Context.NOTIFICATION_SERVICE);

                Notification.Builder builder = new Notification.Builder(
                        this.getApplicationContext());

                try {
                    Drawable icon = getApplicationContext().getPackageManager().getApplicationIcon(getApplicationContext().getPackageName());
                    builder.setContentIntent(contentIntent)
                            .setSmallIcon(Icon.createWithBitmap(((BitmapDrawable) icon).getBitmap()))
                            .setTicker(tickerText)
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true)
                            .setContentTitle(contentTitle)
                            .setContentText(content)
                            .setDefaults(
                                    Notification.DEFAULT_VIBRATE
                                            | Notification.DEFAULT_LIGHTS)
                            .setSound(
                                    RingtoneManager
                                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                            );
                    Notification n = builder.build();

                    mNotificationManager
                            .notify((int) System.currentTimeMillis(), n);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }

            ScheduleNotificationsManager.instance(this.getApplicationContext()).unregister(
                    notificationId,
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
