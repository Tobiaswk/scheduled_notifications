package dk.kjeldsen.scheduled_notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnRebootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent i) {
		Log.d(this.getClass().getName(), "Reboot detected!");
		ScheduleNotificationsManager.instance(context).registerLoaded(context.getApplicationContext());
	}

}
