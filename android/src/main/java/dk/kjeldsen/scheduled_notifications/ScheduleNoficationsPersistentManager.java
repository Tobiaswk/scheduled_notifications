package dk.kjeldsen.scheduled_notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ScheduleNoficationsPersistentManager {

    public static String ALARM_FILE = "alarms";
    private static ScheduleNoficationsPersistentManager instance;

    private ConcurrentHashMap<String, ScheduleNotification> alarms;

    private ScheduleNoficationsPersistentManager() {
        alarms = new ConcurrentHashMap<String, ScheduleNotification>();
    }

    public void save(Context context) {
        FileOutputStream fos;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(ALARM_FILE,
                    Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(fos));
            for (String key : alarms.keySet()) {
                oos.writeObject(alarms.get(key));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (oos != null)
                try {
                    oos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

    }

    public void load(Context context) {
        alarms.clear();
        FileInputStream fis;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(
                    ALARM_FILE);
            ois = new ObjectInputStream(
                    new BufferedInputStream(fis));
            ScheduleNotification scheduleNotification;
            while ((scheduleNotification = (ScheduleNotification) ois.readObject()) != null) {
                alarms.put(scheduleNotification.getKey(), scheduleNotification);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
    }

    public static ScheduleNoficationsPersistentManager instance() {
        if (instance == null)
            instance = new ScheduleNoficationsPersistentManager();
        return instance;
    }

    public void register(ScheduleNotification scheduleNotification, Context ctx) {
        Log.d(ScheduleNoficationsPersistentManager.class.getName(), "Register notification");
        AlarmManager am = (AlarmManager) ctx
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(ctx, ScheduleNoticationsService.class);
        intent.putExtra("ticker", scheduleNotification.getTicker());
        intent.putExtra("contentTitle", scheduleNotification.getContentTitle());
        intent.putExtra("content", scheduleNotification.getContent());

        PendingIntent sender = PendingIntent.getService(ctx,
                scheduleNotification.getRequestCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, scheduleNotification.getTriggerInMillis(), sender);

        alarms.put(scheduleNotification.getKey(), scheduleNotification);

        save(ctx);
    }

    public void unregister(String key, Context ctx) {
        ScheduleNotification scheduleNotification = alarms.get(key);
        AlarmManager am = (AlarmManager) ctx
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, ScheduleNoticationsService.class);
        PendingIntent sender = PendingIntent.getService(ctx,
                scheduleNotification.getRequestCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(sender);

        alarms.remove(key);

        save(ctx);
    }

    public Collection<ScheduleNotification> getAlarms() {
        return alarms.values();
    }

    public ScheduleNotification getAlarm(String key) {
        return alarms.get(key);
    }

}
