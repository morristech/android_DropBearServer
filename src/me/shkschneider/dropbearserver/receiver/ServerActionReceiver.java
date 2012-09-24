package me.shkschneider.dropbearserver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import me.shkschneider.dropbearserver.service.ServerActionService;
import me.shkschneider.dropbearserver.util.L;

public class ServerActionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		L.d("Received Server Action: " + intent.getAction());
		intent.setClass(context, ServerActionService.class);
		WakefulIntentService.sendWakefulWork(context, intent);
	}
}
