package com.firat.hatirlatici;

import android.content.Context;
import android.widget.Toast;


public class toast {
	public toast(Context context, String msg) {
		toast(context, msg);
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public void toast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
