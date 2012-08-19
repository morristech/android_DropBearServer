package me.shkschneider.dropbearserver.Tasks;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import me.shkschneider.dropbearserver.Pages.ServerPage;
import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;

public class DropbearRemover extends AsyncTask<Void, String, Boolean> {

	private static final String TAG = "DropBearServer";

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

	private Boolean falseWithError(String error) {
		Log.d(TAG, "DropBearRemover: ERROR: " + error);
		//Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.i(TAG, "DropbearRemover: doInBackground()");

		int step = 0;
		int steps = 11;

		String dropbear = ServerUtils.getLocalDir(mContext) + "/dropbear";
		String dropbearkey = ServerUtils.getLocalDir(mContext) + "/dropbearkey";
		String ssh = ServerUtils.getLocalDir(mContext) + "/ssh";
		String scp = ServerUtils.getLocalDir(mContext) + "/scp";
		String banner = ServerUtils.getLocalDir(mContext) + "/banner";
		String host_rsa = ServerUtils.getLocalDir(mContext) + "/host_rsa";
		String host_dss = ServerUtils.getLocalDir(mContext) + "/host_dss";
		String authorized_keys = ServerUtils.getLocalDir(mContext) + "/authorized_keys";
		String lock = ServerUtils.getLocalDir(mContext) + "/lock";

		// dropbear
		publishProgress("" + step++, "" + steps, "Dropbear binary");
		if (new File(dropbear).exists() == true && ShellUtils.rm(dropbear) == false) {
			return falseWithError(dropbear);
		}

		// dropbearkey
		publishProgress("" + step++, "" + steps, "Dropbearkey binary");
		if (new File(dropbearkey).exists() == true && ShellUtils.rm(dropbearkey) == false) {
			return falseWithError(dropbearkey);
		}

		// ssh
		publishProgress("" + step++, "" + steps, "SSH binary");
		if (new File("/system/xbin/ssh").exists() == true && ShellUtils.rm("/system/xbin/ssh") == false) {
			return falseWithError("/system/xbin/ssh");
		}
		publishProgress("" + step++, "" + steps, "SSH binary");
		if (new File(ssh).exists() == true && ShellUtils.rm(ssh) == false) {
			return falseWithError(ssh);
		}

		// scp
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (new File("/system/xbin/scp").exists() == true && ShellUtils.rm("/system/xbin/scp") == false) {
			return falseWithError("/system/xbin/scp");
		}
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (new File(scp).exists() == true && ShellUtils.rm(scp) == false) {
			return falseWithError(scp);
		}

		// banner
		publishProgress("" + step++, "" + steps, "Banner");
		if (new File(banner).exists() == true && ShellUtils.rm(banner) == false) {
			return falseWithError(banner);
		}

		// authorized_keys
		publishProgress("" + step++, "" + steps, "Authorized keys");
		if (new File(authorized_keys).exists() == true && ShellUtils.rm(authorized_keys) == false) {
			return falseWithError(authorized_keys);
		}

		// host_rsa
		publishProgress("" + step++, "" + steps, "Host RSA key");
		if (new File(host_rsa).exists() == true && ShellUtils.rm(host_rsa) == false) {
			return falseWithError(host_rsa);
		}

		// host_dss
		publishProgress("" + step++, "" + steps, "Host DSS key");
		if (new File(host_dss).exists() == true && ShellUtils.rm(host_dss) == false) {
			return falseWithError(host_dss);
		}

		// lock
		publishProgress("" + step++, "" + steps, "Lock file");
		if (new File(lock).exists() == true && ShellUtils.rm(lock) == false) {
			return falseWithError(lock);
		}

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