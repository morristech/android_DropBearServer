package me.shkschneider.dropbearserver.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import me.shkschneider.dropbearserver.service.ServerActionService;
import me.shkschneider.dropbearserver.util.L;
import me.shkschneider.dropbearserver.util.ServerUtils;
import me.shkschneider.dropbearserver.util.ShellUtils;

public class ServerStopper extends AsyncTask<Void, String, Boolean> {

    private Context mContext = null;
    private ProgressDialog mProgressDialog = null;
    private boolean mStartInBackground = false;

    private ServerStopperCallback<Boolean> mCallback;

    public ServerStopper(Context context, ServerStopperCallback<Boolean> callback) {
	this(context, callback, false);
    }

    public ServerStopper(Context context, ServerStopperCallback<Boolean> callback, boolean startInBackground) {
	mContext = context;
	mCallback = callback;
	mStartInBackground = startInBackground;
	if (mContext != null && !mStartInBackground) {
	    mProgressDialog = new ProgressDialog(mContext);
	    mProgressDialog.setTitle("Stopping server");
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
	if (mProgressDialog != null && !mStartInBackground) {
	    mProgressDialog.show();
	}
    }

    private Boolean falseWithError(String error) {
	L.d(error);
	//Toast.makeText(mContext, "Error: " + error, Toast.LENGTH_LONG).show();
	return false;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
	String pidFile = ServerUtils.getLocalDir(mContext) + "/pid";
	ShellUtils.rm(pidFile);

	String lockFile = ServerUtils.getLocalDir(mContext) + "/lock";
	if (ShellUtils.echoToFile("0", lockFile) == false)
	    return falseWithError("echoToFile(0, " + lockFile + ")");

	L.i("Killing processes");
	if (ShellUtils.killall("dropbear") == false)
	    return falseWithError("killall(dropbear)");

	return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
	if (mProgressDialog != null && !mStartInBackground) {
	    mProgressDialog.dismiss();
	}
	if (mCallback != null) {
	    mCallback.onServerStopperComplete(result);
	} else {
	    Intent intent = new Intent(ServerActionService.ACTION_SERVER_STOPPED);
	    intent.putExtra(ServerActionService.EXTRA_IS_SUCCESS, result);
	    mContext.sendBroadcast(intent);
	}
    }
}