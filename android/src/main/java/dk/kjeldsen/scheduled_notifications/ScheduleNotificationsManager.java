package dk.kjeldsen.scheduled_notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ScheduleNotificationsManager {

    public static String ALARM_FILE = "scheduledNotifications";
    private static ScheduleNotificationsManager instance;

    private HashMap<Integer, ScheduleNotification> scheduledNotifications;

    private ScheduleNotificationsManager() {
        scheduledNotifications = new HashMap<>();
    }

    public void save(Context context) {
        FileOutputStream fos;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(ALARM_FILE,
                    Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(fos));
            for (Integer key : scheduledNotifications.keySet()) {
                oos.writeObject(scheduledNotifications.get(key));
                Log.d(this.getClass().getName(), "Saved scheduled notification!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null)
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    public void load(Context context) {
        scheduledNotifications.clear();
        FileInputStream fis;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(
                    ALARM_FILE);
            ois = new ObjectInputStream(
                    new BufferedInputStream(fis));
            ScheduleNotification scheduleNotification;
            while ((scheduleNotification = (ScheduleNotification) ois.readObject()) != null) {
                scheduledNotifications.put(scheduleNotification.getNotificationId(), scheduleNotification);
                Log.d(this.getClass().getName(), "Loaded scheduled notification!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void registerLoadedAlarms(Context ctx) {
        for (ScheduleNotification scheduleNotification: scheduledNotifications.values()) {
            register(scheduleNotification, ctx);
        }
    }

    public static ScheduleNotificationsManager instance() {
        if (instance == null)
            instance = new ScheduleNotificationsManager();
        return instance;
    }

    public ScheduleNotification register(ScheduleNotification scheduleNotification, Context ctx) {
        Log.d(this.getClass().getName(), "Register notification!");
        AlarmManager am = (AlarmManager) ctx
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(ctx, ScheduleNotificationsService.class);
        intent.putExtra("ticker", scheduleNotification.getTicker());
        intent.putExtra("contentTitle", scheduleNotification.getContentTitle());
        intent.putExtra("content", scheduleNotification.getContent());
        intent.putExtra("notificationId", scheduleNotification.getNotificationId());

        PendingIntent sender = PendingIntent.getService(ctx,
                scheduleNotification.getNotificationId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, scheduleNotification.getTriggerInMillis(), sender);

        scheduledNotifications.put(scheduleNotification.getNotificationId(), scheduleNotification);

        save(ctx);

        return scheduleNotification;
    }

    public void unregister(int notificationId, Context ctx) {
        Log.d(this.getClass().getName(), "Unregister notification!");
        ScheduleNotification scheduleNotification = scheduledNotifications.get(notificationId);
        AlarmManager am = (AlarmManager) ctx
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, ScheduleNotificationsService.class);
        PendingIntent sender = PendingIntent.getService(ctx,
                scheduleNotification.getNotificationId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(sender);

        scheduledNotifications.remove(notificationId);

        save(ctx);
    }

    public ScheduleNotification get(int notificationId) {
        return scheduledNotifications.get(notificationId);
    }

}
