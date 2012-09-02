/*
 * Andreas Stutz <https://github.com/astuetz/android-viewpagertabs-example>
 */
package me.shkschneider.dropbearserver;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.astuetz.viewpagertabs.ViewPagerTabProvider;

import me.shkschneider.dropbearserver.Pages.AboutPage;
import me.shkschneider.dropbearserver.Pages.ServerPage;
import me.shkschneider.dropbearserver.Pages.SettingsPage;
import me.shkschneider.dropbearserver.util.L;

public class MainAdapter extends PagerAdapter implements ViewPagerTabProvider {

    private static final int SETTINGS_INDEX = 0;
    private static final int SERVER_INDEX = 1;
    private static final int ABOUT_INDEX = 2;
    public static final int DEFAULT_PAGE = SERVER_INDEX;

    private Context mContext;
    private SettingsPage mSettingsPage;
    private ServerPage mServerPage;
    private AboutPage mAboutPage;

    private String[] mTitles = {
	    "SETTINGS",
	    "SERVER",
	    "ABOUT"
    };

    public MainAdapter(Context context) {
	mContext = context;
	mSettingsPage = new SettingsPage(mContext);
	mServerPage = new ServerPage(mContext);
	mAboutPage = new AboutPage(mContext);
    }

    public void updateSettings() {
	mSettingsPage.updateAll();
    }

    public void updateServer() {
	mServerPage.updateAll();
    }

    public void updateAbout() {
	mAboutPage.updateAll();
    }

    public void updatePublicKeys() {
	mSettingsPage.updatePublicKeys();
	mServerPage.updateAll();
    }

    @Override
    public Object instantiateItem(View container, int position) {
	View v = null;
	switch (position) {
	case SETTINGS_INDEX:
	    //mSettingsPage.update();
	    v = mSettingsPage.getView();
	    break;
	case SERVER_INDEX:
	    //mServerPage.update();
	    v = mServerPage.getView();
	    break;
	case ABOUT_INDEX:
	    //mAboutPage.update();
	    v = mAboutPage.getView();
	    break;
	default:
	    L.e("default");
	    break;
	}
	((ViewPager) container).addView(v, 0);
	return v;
    }

    @Override
    public int getCount() {
	return mTitles.length;
    }

    @Override
    public void destroyItem(View container, int position, Object view) {
	((ViewPager) container).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
	return view == ((View) object);
    }

    @Override
    public void finishUpdate(View container) {
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
	return null;
    }

    @Override
    public void startUpdate(View container) {
    }

    public String getTitle(int position) {
	return mTitles[position];
    }
}