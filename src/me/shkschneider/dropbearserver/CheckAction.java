/*
 * Johan Nilsson <https://github.com/johannilsson/android-actionbar>
 */
package me.shkschneider.dropbearserver;

import android.content.Context;
import android.view.View;

import com.markupartist.android.widget.ActionBar.Action;

class CheckAction implements Action {

	private Context mContext;

	public CheckAction(Context context) {
		mContext = context;
	}

	public int getDrawable() {
		return android.R.drawable.ic_menu_rotate;
	}

	public void performAction(View view) {
		((MainActivity) mContext).check();
	}

}
