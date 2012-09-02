/*
 * elsdoerfer <http://blog.elsdoerfer.name/2010/04/15/android-check-if-sd-card-storage-is-available/>
 */
package me.shkschneider.dropbearserver.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

public abstract class Utils {

    public static void marketNotFound(Context context) {
	Toast.makeText(context, "Error: Google Play could not be found", Toast.LENGTH_LONG).show();
    }

    public static final Boolean copyRawFile(Context context, int rawId, String path) {
	try {
	    InputStream in = context.getResources().openRawResource(rawId);
	    OutputStream out = new FileOutputStream(new File(path));
	    byte[] buffer = new byte[1024];
	    int read;
	    while ((read = in.read(buffer)) != -1) {
		out.write(buffer, 0, read);
	    }
	    in.close();
	    out.flush();
	    out.close();
	} catch(Exception e) {
	    L.e(e.getMessage());
	    return false;
	}
	return true;
    }

    public static final Boolean remountReadWrite(String path) {
	return RootTools.remount(path, "RW");
    }

    public static final Boolean remountReadOnly(String path) {
	return RootTools.remount(path, "RO");
    }

    public static Boolean isConnectedToWiFi(Context context) {
	ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo netInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

	return netInfo.isConnected();
    }

    public static Boolean isNumeric(String string) {
	if (string.matches("^[0-9]+$")) {
	    return true;
	}
	return false;
    }

    public static Boolean hasStorage(Boolean requireWriteAccess) {
	String state = Environment.getExternalStorageState();

	if (Environment.MEDIA_MOUNTED.equals(state)) {
	    return true;
	}
	else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	    return true;
	}

	return false;
    }

}
