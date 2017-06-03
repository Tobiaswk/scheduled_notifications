package dk.kjeldsen.scheduled_notifications;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * ScheduledNotificationsPlugin
 */
public class ScheduledNotificationsPlugin implements MethodCallHandler {

    Activity activity;

    private ScheduledNotificationsPlugin(Activity activity) {
        this.activity = activity;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "scheduled_notifications");
        channel.setMethodCallHandler(new ScheduledNotificationsPlugin(registrar.activity()));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("scheduleNotification")) {
            List<Object> arguments = call.arguments();
            //result.success();
            registerScheduledNotification((Long)arguments.get(0), (String)arguments.get(1), (String)arguments.get(2), (String)arguments.get(3));
        } else {
            result.notImplemented();
        }
    }

    private void registerScheduledNotification(Long triggerInMillis, String ticker, String contentTitle, String content) {
        ScheduleNoficationsPersistentManager.instance().register(new ScheduleNotification(triggerInMillis, ticker, contentTitle, content), activity);
    }

}
