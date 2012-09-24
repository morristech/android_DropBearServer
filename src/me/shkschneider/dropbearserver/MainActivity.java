package me.shkschneider.dropbearserver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.astuetz.viewpagertabs.ViewPagerTabs;
import com.markupartist.android.widget.ActionBar;

import me.shkschneider.dropbearserver.Pages.SettingsPage;
import me.shkschneider.dropbearserver.service.ServerActionService;
import me.shkschneider.dropbearserver.task.Checker;
import me.shkschneider.dropbearserver.task.CheckerCallback;
import me.shkschneider.dropbearserver.util.L;
import me.shkschneider.dropbearserver.util.ServerUtils;

public class MainActivity extends Activity implements CheckerCallback<Boolean> {

	public static Boolean needToCheckDependencies = true;
	public static Boolean needToCheckDropbear = true;

	public static String appVersion = "1.0";
	public static String dropbearVersion = null;

	private ActionBar mActionBar;
	private ViewPager mPager;
	private ViewPagerTabs mPagerTabs;
	private MainAdapter mAdapter;

	private BroadcastReceiver mUpdateUiReceiver = null;

	public static String getAppVersion() {
		if (dropbearVersion != null) {
			return appVersion + "/" + dropbearVersion;
		}
		else {
			return appVersion;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Introduction
		String appName = getResources().getString(R.string.app_name);
		String packageName = getApplication().getPackageName();
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			appVersion = packageInfo.versionName.toString();
		}
		catch (Exception e) {
			L.e(e.getMessage());
		}
		L.i(appName + " v" + appVersion + " (" + packageName + ") Android " + Build.VERSION.RELEASE + " (API-" + Build.VERSION.SDK_INT + ")");

		// Header
		mActionBar = (ActionBar) findViewById(R.id.actionbar);
		mActionBar.setTitle(getResources().getString(R.string.app_name));
		mActionBar.setHomeAction(new HomeAction(this));
		mActionBar.addAction(new CheckAction(this));

		// ViewPagerTabs
		mAdapter = new MainAdapter(this);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPagerTabs = (ViewPagerTabs) findViewById(R.id.tabs);
		mPagerTabs.setViewPager(mPager);

		mUpdateUiReceiver = new UpdateUiReceiver();
		registerReceiver(mUpdateUiReceiver, new IntentFilter(ServerActionService.ACTION_UPDATE_UI));
	}

	@Override
	public void onStart() {
		super.onStart();

		updateSettings();
		updateServer();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (needToCheckDependencies == true) {
			Toast.makeText(this, "You can get rid of those checks in Settings", Toast.LENGTH_LONG).show();
			// Root dependencies
			check();
			needToCheckDependencies = false;
		}

		// keepScreenOn
		if (SettingsHelper.getInstance(getBaseContext()).getKeepScreenOn() == true) {
			this.getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		else {
			this.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	@Override
	protected void onDestroy() {
		if (mUpdateUiReceiver != null) {
			unregisterReceiver(mUpdateUiReceiver);
		}

		super.onDestroy();
	}

	public static Intent createIntent(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return intent;
	}

	public void goToDefaultPage() {
		mPager.setCurrentItem(MainAdapter.DEFAULT_PAGE,  true);
	}

	public void check() {
		// Checker
		Checker checker = new Checker(this, this);
		checker.execute();
	}

	public void onCheckerComplete(Boolean result) {
		updateSettings();
		updateServer();
		updateAbout();
	}

	public void updateSettings() {
		mAdapter.updateSettings();
	}

	public void updateServer() {
		mAdapter.updateServer();
	}

	public void updateAbout() {
		if (dropbearVersion == null) {
			dropbearVersion = ServerUtils.getDropbearVersion();
		}
		mAdapter.updateAbout();
	}

	public void updatePublicKeys() {
		mAdapter.updatePublicKeys();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (SettingsPage.goToHome == true) {
			goToDefaultPage();
		}
		updatePublicKeys();
	}

	class UpdateUiReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateSettings();
			updateServer();
			updateAbout();
		}
	};
}