package dk.kjeldsen.scheduled_notifications;

import android.app.Activity;

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
        List<Object> arguments = call.arguments();
        if (call.method.equals("scheduleNotification")) {
            ScheduleNotification scheduleNotification = scheduledNotification((Long)arguments.get(0), (String)arguments.get(1), (String)arguments.get(2), (String)arguments.get(3));
            result.success(scheduleNotification.getNotificationId());
        } else if(call.method.equals("unscheduleNotification")) {
            unscheduledNotification((Integer)arguments.get(0));
            result.success(null);
        } else if(call.method.equals("hasScheduledNotification")) {
            result.success(hasScheduledNotification((Integer)arguments.get(0)));
        } else {
            result.notImplemented();
        }
    }

    private ScheduleNotification scheduledNotification(Long triggerInMillis, String ticker, String contentTitle, String content) {
        return ScheduleNotificationsManager.instance().register(new ScheduleNotification(triggerInMillis, ticker, contentTitle, content), activity);
    }

    private void unscheduledNotification(int notificationId) {
        ScheduleNotificationsManager.instance().unregister(notificationId, activity);
    }

    private boolean hasScheduledNotification(int notificationId) {
        return ScheduleNotificationsManager.instance().get(notificationId) != null;
    }

}
