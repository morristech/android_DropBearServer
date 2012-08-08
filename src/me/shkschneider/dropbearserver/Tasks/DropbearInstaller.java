package me.shkschneider.dropbearserver.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.Utils.ServerUtils;
import me.shkschneider.dropbearserver.Utils.ShellUtils;
import me.shkschneider.dropbearserver.Utils.Utils;

public class DropbearInstaller extends AsyncTask<Void, String, Boolean> {

	private static final String TAG = "DropBearServer";

	private Context mContext = null;
	private ProgressDialog mProgressDialog = null;

	private DropbearInstallerCallback<Boolean> mCallback;

	public DropbearInstaller(Context context, DropbearInstallerCallback<Boolean> callback) {
		mContext = context;
		mCallback = callback;
		if (mContext != null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setTitle("Installing Dropbear");
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setMax(100);
			mProgressDialog.setIcon(0);
		}
	}

	@Override
	protected void onPreExecute() {
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
		Log.d(TAG, "DropBearInstall: ERROR: " + error);
		//Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_LONG).show();
		return false;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Log.i(TAG, "DropbearInstaller: doInBackground()");

		int step = 0;
		int steps = 13;

		String dropbear = ServerUtils.getLocalDir(mContext) + "/dropbear";
		String dropbearkey = ServerUtils.getLocalDir(mContext) + "/dropbearkey";
		String scp = ServerUtils.getLocalDir(mContext) + "/scp";
		String banner = ServerUtils.getLocalDir(mContext) + "/banner";
		String host_rsa = ServerUtils.getLocalDir(mContext) + "/host_rsa";
		String host_dss = ServerUtils.getLocalDir(mContext) + "/host_dss";
		String authorized_keys = ServerUtils.getLocalDir(mContext) + "/authorized_keys";
		String lock = ServerUtils.getLocalDir(mContext) + "/lock";

		// dropbear
		publishProgress("" + step++, "" + steps, "Dropbear binary");
		if (Utils.copyRawFile(mContext, R.raw.dropbear, dropbear) == false) {
			return falseWithError(dropbear);
		}
		publishProgress("" + step++, "" + steps, "Dropbear binary");
		if (ShellUtils.chmod(dropbear, "755") == false) {
			return falseWithError(dropbear);
		}

		// dropbearkey
		publishProgress("" + step++, "" + steps, "Dropbearkey binary");
		if (Utils.copyRawFile(mContext, R.raw.dropbearkey, dropbearkey) == false) {
			return falseWithError(dropbearkey);
		}
		publishProgress("" + step++, "" + steps, "Dropbearkey binary");
		if (ShellUtils.chmod(dropbearkey, "755") == false) {
			return falseWithError(dropbearkey);
		}

		// scp
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (Utils.copyRawFile(mContext, R.raw.scp, scp) == false) {
			return falseWithError(scp);
		}
		publishProgress("" + step++, "" + steps, "SCP binary");
		if (ShellUtils.chmod(scp, "755") == false) {
			return falseWithError(scp);
		}

		// banner
		publishProgress("" + step++, "" + steps, "Banner");
		if (Utils.copyRawFile(mContext, R.raw.banner, banner) == false) {
			return falseWithError(banner);
		}

		// authorized_keys
		publishProgress("" + step++, "" + steps, "Authorized keys");
		if (ServerUtils.createIfNeeded(authorized_keys) == false) {
			return falseWithError(authorized_keys);
		}

		// host_rsa
		publishProgress("" + step++, "" + steps, "Host RSA key");
		if (ServerUtils.generateRsaPrivateKey(host_rsa) == false) {
			return falseWithError(host_rsa);
		}
		publishProgress("" + step++, "" + steps, "Host RSA key");
		if (ShellUtils.chown(host_rsa, "0:0") == false) {
			return falseWithError(host_rsa);
		}

		// host_dss
		publishProgress("" + step++, "" + steps, "Host DSS key");
		if (ServerUtils.generateDssPrivateKey(host_dss) == false) {
			return falseWithError(host_dss);
		}
		publishProgress("" + step++, "" + steps, "Host DSS key");
		if (ShellUtils.chown(host_dss, "0:0") == false) {
			return falseWithError(host_dss);
		}

		// lock
		publishProgress("" + step++, "" + steps, "Lock file");
		if (ShellUtils.echoToFile("0", lock) == false) {
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
			mCallback.onDropbearInstallerComplete(result);
		}
	}
}