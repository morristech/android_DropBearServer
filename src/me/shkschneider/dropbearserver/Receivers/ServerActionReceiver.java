package me.shkschneider.dropbearserver.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import me.shkschneider.dropbearserver.Services.ServerActionService;

public class ServerActionReceiver extends BroadcastReceiver {

	private static final String TAG = "DropBearServer/ServerActionReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received Server Action: " + intent.getAction());
		intent.setClass(context, ServerActionService.class);
		WakefulIntentService.sendWakefulWork(context, intent);
	}
}
