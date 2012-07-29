package me.shkschneider.dropbearserver.Receivers;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import me.shkschneider.dropbearserver.Services.ServerActionService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServerActionReceiver extends BroadcastReceiver {

	private static final String TAG = "DropBearServer";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received Server Action: " + intent.getAction());
		intent.setClass(context, ServerActionService.class);
		WakefulIntentService.sendWakefulWork(context, intent);
	}
}
