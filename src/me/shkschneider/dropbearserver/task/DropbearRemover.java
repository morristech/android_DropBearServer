package me.shkschneider.dropbearserver.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import me.shkschneider.dropbearserver.Pages.ServerPage;
import me.shkschneider.dropbearserver.util.ServerUtils;
import me.shkschneider.dropbearserver.util.ShellUtils;

public class DropbearRemover extends AsyncTask<Void, String, Boolean> {

	private Context mContext = null;
	private ProgressDialog mProgressDialog = null;

	private DropbearRemoverCallback<Boolean> mCallback;

	public DropbearRemover(Context context, DropbearRemoverCallback<Boolean> callback) {
		mContext = context;
		mCallback = callback;
		if (mContext != null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setTitle("Removing Dropbear");
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setMax(100);
			mProgressDialog.setIcon(0);
		}
	}

	@Override
	protected void onPreExecute() {
		if (ServerPage.mServerLock > 0) {
			// ServerStopper
			ServerStopper serverStopper = new ServerStopper(mContext, null);
			serverStopper.execute();
		}
		super.onPreExecute();
		if (mProgressDialog != null) {
			mProgressDialog.show();
		}
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		super.onProgressUpdate(progress);
		if (mProgressDialog != null) {
			Float f = (Float.parseFloat(progress[0] + ".0") / Float.parseFloat(progress[1] + ".0") * 100);
			mProgressDialog.setProgress(Math.round(f));
			mProgressDialog.setMessage(progress[2]);
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		int step = 0;
		int steps = 10;

		String dropbear = ServerUtils.getLocalDir(mContext) + "/dropbear";
		String dropbearkey = ServerUtils.getLocalDir(mContext) + "/dropbearkey";
		String ssh = "/system/xbin/ssh";
		String scp = "/system/xbin/scp";
		String dbclient = "/system/xbin/dbclient";
		String banner = ServerUtils.getLocalDir(mContext) + "/banner";
		String host_rsa = ServerUtils.getLocalDir(mContext) + "/host_rsa";
		String host_dss = ServerUtils.getLocalDir(mContext) + "/host_dss";
		String authorized_keys = ServerUtils.getLocalDir(mContext) + "/authorized_keys";
		String lock = ServerUtils.getLocalDir(mContext) + "/lock";

		// dropbear
		publishProgress("" + step++, "" + steps, "Dropbear binary");
		ShellUtils.rm(dropbear);

		// dropbearkey
		publishProgress("" + step++, "" + steps, "Dropbearkey binary");
		ShellUtils.rm(dropbearkey);

		// ssh
		publishProgress("" + step++, "" + steps, "SSH binary");
		ShellUtils.rm(ssh);

		// scp
		publishProgress("" + step++, "" + steps, "SCP binary");
		ShellUtils.rm(scp);

		// dbclient
		publishProgress("" + step++, "" + steps, "DBClient binary");
		ShellUtils.rm(dbclient);

		// banner
		publishProgress("" + step++, "" + steps, "Banner");
		ShellUtils.rm(banner);

		// authorized_keys
		publishProgress("" + step++, "" + steps, "Authorized keys");
		ShellUtils.rm(authorized_keys);

		// host_rsa
		publishProgress("" + step++, "" + steps, "Host RSA key");
		ShellUtils.rm(host_rsa);

		// host_dss
		publishProgress("" + step++, "" + steps, "Host DSS key");
		ShellUtils.rm(host_dss);

		// lock
		publishProgress("" + step++, "" + steps, "Lock file");
		ShellUtils.rm(lock);

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		if (mCallback != null) {
			mCallback.onDropbearRemoverComplete(result);
		}
	}
}