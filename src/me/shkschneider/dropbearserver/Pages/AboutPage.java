package me.shkschneider.dropbearserver.Pages;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import me.shkschneider.dropbearserver.MainActivity;
import me.shkschneider.dropbearserver.R;
import me.shkschneider.dropbearserver.util.Utils;

public class AboutPage {

    private Context mContext;
    private View mView;

    private TextView mAppVersion = null;

    public AboutPage(Context context) {
	mContext = context;

	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	mView = inflater.inflate(R.layout.about, null);

	mAppVersion = (TextView) mView.findViewById(R.id.appversion);
	mAppVersion.setText("Version " + MainActivity.getAppVersion());

	Button rate = (Button) mView.findViewById(R.id.rate);
	rate.setOnClickListener(new OnClickListener() {

	    public void onClick(View v) {
		try {
		    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getApplicationInfo().packageName)));
		}
		catch (ActivityNotFoundException e) {
		    Utils.marketNotFound(mContext);
		}
	    }
	});

	Button donate = (Button) mView.findViewById(R.id.donate);
	donate.setOnClickListener(new OnClickListener() {

	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://shkschneider.me/donate"));
		mContext.startActivity(intent);
	    }
	});

	Button email = (Button) mView.findViewById(R.id.email);
	email.setOnClickListener(new OnClickListener() {

	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_EMAIL,
			new String[] { "contact@shkschneider.me" });
		intent.putExtra(Intent.EXTRA_SUBJECT, "[DropBearServer v"
			+ MainActivity.getAppVersion() + "]");
		intent.putExtra(Intent.EXTRA_TEXT, "Hi,\n\n...\n\nThanks");
		mContext.startActivity(Intent.createChooser(intent, "Send email..."));
	    }
	});

	Button web = (Button) mView.findViewById(R.id.web);
	web.setOnClickListener(new OnClickListener() {

	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://shkschneider.me"));
		mContext.startActivity(intent);
	    }
	});
    }

    public void updateAll() {
	mAppVersion.setText("Version " + MainActivity.getAppVersion());
    }

    public View getView() {
	updateAll();
	return mView;
    }

}