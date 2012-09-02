/*
 * Johan Nilsson <https://github.com/johannilsson/android-actionbar>
 */
package me.shkschneider.dropbearserver.explorer;

import android.content.Context;
import android.view.View;

import com.markupartist.android.widget.ActionBar.Action;

class CancelAction implements Action {

    private Context mContext;

    public CancelAction(Context context) {
	mContext = context;
    }

    public int getDrawable() {
	return android.R.drawable.ic_menu_close_clear_cancel;
    }

    public void performAction(View view) {
	((ExplorerActivity) mContext).finish();
    }

}
