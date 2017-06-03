# scheduled_notifications

A flutter plugin for scheduling notifications on Android.

## Usage

To use this plugin, add scheduled_notifications as a dependency in your pubspec.yaml file. Note you have to specify minSdkVersion on 23 in your AndroidManifest.xml to use this plugin.

###Example

```
import 'package:scheduled_notifications/scheduled_notifications.dart';

void main() {
  runApp(new Scaffold(
    body: new Center(
      child: new RaisedButton(
        onPressed: _scheduleNotification,
        child: new Text('Schedule notification in 5 seconds'),
      ),
    ),
  ));
}

_scheduleNotification() {
      ScheduledNotifications.scheduleNotification(
          new DateTime.now().add(new Duration(seconds: 5)).millisecondsSinceEpoch,
          "Ticker text",
          "Content title",
          "Content");
}
```