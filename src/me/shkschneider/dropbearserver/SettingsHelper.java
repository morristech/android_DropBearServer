/*
 * Bill Mote <http://stackoverflow.com/questions/5734721/android-shared-preferences>
 */
package me.shkschneider.dropbearserver;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import me.shkschneider.dropbearserver.util.L;

public class SettingsHelper {

    private static final String PREF_NOTIFICATION = "notification";
    private static final String PREF_KEEP_SCREEN_ON = "keepScreenOn";
    private static final String PREF_ONLY_OVER_WIFI = "onlyOverWifi";
    private static final String PREF_DISALLOW_ROOT_LOGINS = "disallowRootLogins";
    private static final String PREF_DISABLE_PASSWORD_LOGINS = "disablePasswordLogins";
    private static final String PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT = "disablePasswordLoginsForRoot";
    private static final String PREF_BANNER = "banner";
    private static final String PREF_LISTENING_PORT = "listeningPort";
    private static final String PREF_CREDENTIALS_LOGIN = "credentialsLogin";
    private static final String PREF_CREDENTIALS_PASSWD = "credentialsPasswd";

    public static final Boolean ASSUME_ROOT_ACCESS_DEFAULT = false;
    public static final Boolean NOTIFICATION_DEFAULT = false;
    public static final Boolean KEEP_SCREEN_ON_DEFAULT = false;
    public static final Boolean ONLY_OVER_WIFI_DEFAULT = false;
    public static final Boolean DISALLOW_ROOT_LOGINS_DEFAULT = false;
    public static final Boolean DISABLE_PASSWORD_LOGINS_DEFAULT = false;
    public static final Boolean DISABLE_PASSWORD_LOGINS_FOR_ROOT_DEFAULT = false;
    public static final Boolean BANNER_DEFAULT = true;
    public static final Integer LISTENING_PORT_DEFAULT = 22;
    public static final Boolean CREDENTIALS_LOGIN_DEFAULT = true;
    public static final String CREDENTIALS_PASSWD_DEFAULT = "42";

    private static SettingsHelper mSettingsHelper = null;
    private static SharedPreferences mSharedPreferences = null;

    public static SettingsHelper getInstance(Context context) {
	if (mSettingsHelper == null) {
	    mSettingsHelper = new SettingsHelper(context);
	}
	return mSettingsHelper;
    }

    private SettingsHelper(Context context) {
	if (context != null) {
	    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	else {
	    L.e("Context is null");
	}
    }

    // notification
    public Boolean getNotification() {
	return mSharedPreferences.getBoolean(PREF_NOTIFICATION, NOTIFICATION_DEFAULT);
    }

    public void setNotification(Boolean b) {
	L.d("setNotification(" + b + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putBoolean(PREF_NOTIFICATION, b);
	editor.commit();
    }

    // keepScreenOn
    public Boolean getKeepScreenOn() {
	return mSharedPreferences.getBoolean(PREF_KEEP_SCREEN_ON, KEEP_SCREEN_ON_DEFAULT);
    }

    public void setKeepScreenOn(Boolean b) {
	L.d("setKeepScreenOn(" + b + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putBoolean(PREF_KEEP_SCREEN_ON, b);
	editor.commit();
    }

    // onlyOverWifi
    public Boolean getOnlyOverWifi() {
	return mSharedPreferences.getBoolean(PREF_ONLY_OVER_WIFI, ONLY_OVER_WIFI_DEFAULT);
    }

    public void setOnlyOverWifi(Boolean b) {
	L.d("setOnlyOverWifi(" + b + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putBoolean(PREF_ONLY_OVER_WIFI, b);
	editor.commit();
    }

    // disallowRootLogins
    public Boolean getDisallowRootLogins() {
	return mSharedPreferences.getBoolean(PREF_DISALLOW_ROOT_LOGINS, DISABLE_PASSWORD_LOGINS_DEFAULT);
    }

    public void setDisallowRootLogins(Boolean b) {
	L.d("setDisallowRootLogins(" + b + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putBoolean(PREF_DISALLOW_ROOT_LOGINS, b);
	editor.commit();
    }

    // disablePasswordLogins
    public Boolean getDisablePasswordLogins() {
	return mSharedPreferences.getBoolean(PREF_DISABLE_PASSWORD_LOGINS, DISABLE_PASSWORD_LOGINS_DEFAULT);
    }

    public void setDisablePasswordLogins(Boolean b) {
	L.d("setDisablePasswordLogins(" + b + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putBoolean(PREF_DISABLE_PASSWORD_LOGINS, b);
	editor.commit();
    }

    // disablePasswordLoginsForRoot
    public Boolean getDisablePasswordLoginsForRoot() {
	return mSharedPreferences.getBoolean(PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT, DISABLE_PASSWORD_LOGINS_FOR_ROOT_DEFAULT);
    }

    public void setDisablePasswordLoginsForRoot(Boolean b) {
	L.d("setDisablePasswordLoginsForRoot(" + b + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putBoolean(PREF_DISABLE_PASSWORD_LOGINS_FOR_ROOT, b);
	editor.commit();
    }

    // banner
    public Boolean getBanner() {
	return mSharedPreferences.getBoolean(PREF_BANNER, BANNER_DEFAULT);
    }

    public void setBanner(Boolean b) {
	L.d("setBanner(" + b + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putBoolean(PREF_BANNER, b);
	editor.commit();
    }

    // listeningPort
    public Integer getListeningPort() {
	return mSharedPreferences.getInt(PREF_LISTENING_PORT, LISTENING_PORT_DEFAULT);
    }

    public void setListeningPort(Integer i) {
	L.d("setListeningPort(" + i + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putInt(PREF_LISTENING_PORT, i);
	editor.commit();
    }

    // credentialsLogin
    public Boolean getCredentialsLogin() {
	return mSharedPreferences.getBoolean(PREF_CREDENTIALS_LOGIN, CREDENTIALS_LOGIN_DEFAULT);
    }

    public void setCredentialsLogin(Boolean b) {
	L.d("setCredentialsLogin(" + b + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putBoolean(PREF_CREDENTIALS_LOGIN, b);
	editor.commit();
    }

    // credentialsPasswd
    public String getCredentialsPasswd() {
	return mSharedPreferences.getString(PREF_CREDENTIALS_PASSWD, CREDENTIALS_PASSWD_DEFAULT);
    }

    public void setCredentialsPasswd(String s) {
	L.d("setCredentialsPasswd(" + s + ")");
	Editor editor = mSharedPreferences.edit();
	editor.putString(PREF_CREDENTIALS_PASSWD, s);
	editor.commit();
    }

}
