package me.shkschneider.dropbearserver.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import me.shkschneider.dropbearserver.MainActivity;
import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.SettingsHelper;
import me.shkschneider.dropbearserver.Tasks.ServerStarter;
import me.shkschneider.dropbearserver.Tasks.ServerStopper;
import me.shkschneider.dropbearserver.Utils.ServerUtils;

public class ServerActionService extends WakefulIntentService {

	private static final String TAG = "DropBearServer";

	public static final String ACTION_START_SERVER = "me.shkschneider.dropbearserver.START_SERVER";
	public static final String ACTION_SERVER_STARTED = "me.shkschneider.dropbearserver.SERVER_STARTED";
	public static final String ACTION_STOP_SERVER = "me.shkschneider.dropbearserver.STOP_SERVER";
	public static final String ACTION_SERVER_STOPPED = "me.shkschneider.dropbearserver.SERVER_STOPPED";
	public static final String ACTION_UPDATE_UI = "me.shkschneider.dropbearserver.UPDATE_UI";

	public static final String EXTRA_IS_SUCCESS = "is_success";

	private static final int NOTIFICATION_ID = 1;

	public ServerActionService() {
		super(ServerActionService.class.getSimpleName());
	}

	@Override
	protected void doWakefulWork(final Intent intent, final Thread callback) {		
		try {
			Context context = getApplicationContext();
			String action = intent.getAction();

			if (ACTION_START_SERVER.equals(action)) {
				startServerInBackground(context);
			}
			else if (ACTION_SERVER_STARTED.equals(action)) {
				handleServerStarted(context, intent);
			}
			if (ACTION_STOP_SERVER.equals(action)) {
				stopServerInBackground(context);
			}
			else if (ACTION_SERVER_STOPPED.equals(action)) {
				handleServerStopped(context, intent);
			}
		}
		catch (Exception e) {
			Log.e(TAG, "Error handling Server Action");
		}
	}

	private void startServerInBackground(Context context) {
		Log.d(TAG, "Processing Start Service broadcast");
		ServerStarter serverStarter = new ServerStarter(context, null, true);
		serverStarter.execute();
	}

	private void handleServerStarted(Context context, Intent intent) {
		Log.d(TAG, "Processing Service Started broadcast");
		boolean success = intent.getBooleanExtra(EXTRA_IS_SUCCESS, false);
		if (success) {
			int mListeningPort = SettingsHelper.getInstance(context).getListeningPort();

			String infos = "ssh ";
			if (SettingsHelper.getInstance(context).getCredentialsLogin() == true) {
				infos = infos.concat("root@");
			}
			String localIpAddress = ServerUtils.getLocalIpAddress();
			infos = infos.concat((localIpAddress != null) ? localIpAddress : "UNKNOWN.INTERNAL.IP.ADDRESS");
			if (mListeningPort != SettingsHelper.LISTENING_PORT_DEFAULT) {
				infos = infos.concat(" -p " + mListeningPort);
			}
			infos = infos.concat("\n");
			infos = infos.concat("ssh ");
			if (SettingsHelper.getInstance(context).getCredentialsLogin() == true) {
				infos = infos.concat("root@");
			}
			String externalIpAddress = ServerUtils.getExternalIpAddress();
			infos = infos.concat((externalIpAddress != null) ? externalIpAddress : "UNKNOWN.EXTERNAL.IP.ADDRESS");
			if (mListeningPort != SettingsHelper.LISTENING_PORT_DEFAULT) {
				infos = infos.concat(" -p " + mListeningPort);
			}

			if (SettingsHelper.getInstance(context).getNotification() == true) {
				Log.d(TAG, "ServerPage: updateServerStatus(): Notification");
				Notification notification = new Notification(R.drawable.ic_launcher, "DropBear Server is running", System.currentTimeMillis());
				Intent notifyIntent = new Intent(context, MainActivity.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);
				notification.setLatestEventInfo(context, "DropBear Server", infos, pendingIntent);
				notification.flags |= Notification.FLAG_ONGOING_EVENT;
				((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notification);
			}
		}

		context.sendBroadcast(new Intent(ServerActionService.ACTION_UPDATE_UI));
	}

	private void stopServerInBackground(Context context) {
		Log.d(TAG, "Processing Stop Service broadcast");
		ServerStopper serverStopper = new ServerStopper(context, null, true);
		serverStopper.execute();
	}

	private void handleServerStopped(Context context, Intent intent) {
		Log.d(TAG, "Processing Service Stopped broadcast");
		boolean success = intent.getBooleanExtra(EXTRA_IS_SUCCESS, false);
		if(success && SettingsHelper.getInstance(context).getNotification() == true) {
			((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);					
		}

		context.sendBroadcast(new Intent(ServerActionService.ACTION_UPDATE_UI));
	}

}
