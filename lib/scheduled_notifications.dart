import 'dart:async';

import 'package:flutter/services.dart';

class ScheduledNotifications {
  static const MethodChannel _channel =
      const MethodChannel('scheduled_notifications');

  static Future<String> scheduleNotification(triggerInMillis, ticker, contentTitle, content) {
   return _channel.invokeMethod('scheduleNotification', [triggerInMillis, ticker, contentTitle, content]);
  }
}
