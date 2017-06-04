import 'dart:async';

import 'package:flutter/services.dart';

class ScheduledNotifications {
  static const MethodChannel _channel =
      const MethodChannel('scheduled_notifications');

  // schedule a notification to fire at triggerInMillis
  static Future<int> scheduleNotification(triggerInMillis, ticker, contentTitle, content) {
   return _channel.invokeMethod('scheduleNotification', [triggerInMillis, ticker, contentTitle, content]);
  }

  // unschedule a notification with the
  // notificationId returned when scheduling a notification
  static Future<Null> unscheduleNotification(notificationId) {
    return _channel.invokeMethod('unscheduleNotification', [notificationId]);
  }

  // check if notification is scheduled with notificationId
  static Future<bool> hasScheduledNotification(notificationId) {
    return _channel.invokeMethod('hasScheduledNotification', [notificationId]);
  }
}
