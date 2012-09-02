/***
  Copyright (c) 2009-2011 CommonsWare, LLC

  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.commonsware.cwac.wakeful;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

abstract public class WakefulIntentService extends IntentService {

	abstract protected void doWakefulWork(Intent intent, Thread callback);

	static final String NAME = "DropBearServer/WakefulIntentService";

	static final String LAST_ALARM = "lastAlarm";

	private static volatile PowerManager.WakeLock lockStatic = null;

	private static Thread _callback = null;

	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic == null) {
			PowerManager mgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, NAME);
			lockStatic.setReferenceCounted(true);
		}
		return(lockStatic);
	}

	public static void sendWakefulWork(Context ctxt, Class<?> clsService) {
		sendWakefulWork(ctxt, new Intent(ctxt, clsService), null); 
	}

	public static void sendWakefulWork(Context ctxt, Class<?> clsService, Thread callback) {
		sendWakefulWork(ctxt, new Intent(ctxt, clsService), callback);
	}

	public static void sendWakefulWork(Context ctxt, Intent i) {
		sendWakefulWork(ctxt, i, null);
	}

	public static void sendWakefulWork(Context ctxt, Intent i, Thread callback) {
		_callback = callback;
		getLock(ctxt.getApplicationContext()).acquire();
		ctxt.startService(i);
	}

	public WakefulIntentService(String name) {
		super(name);
		setIntentRedelivery(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		PowerManager.WakeLock lock=getLock(this.getApplicationContext());

		if (!lock.isHeld() || (flags & START_FLAG_REDELIVERY) != 0) {
			lock.acquire();
		}
		super.onStartCommand(intent, flags, startId);
		return(START_REDELIVER_INTENT);
	}

	@Override
	final protected void onHandleIntent(Intent intent) {
		try {
			doWakefulWork(intent, _callback);
		}
		finally {
			PowerManager.WakeLock lock=getLock(this.getApplicationContext());
			if (lock.isHeld()) {
				lock.release();
			}
		}
	}

	public interface AlarmListener {
		void scheduleAlarms(AlarmManager mgr, PendingIntent pi, Context ctxt);
		void sendWakefulWork(Context ctxt);
		long getMaxAge();
	}

}