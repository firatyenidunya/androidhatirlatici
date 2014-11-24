package com.firat.hatirlatici;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverServis extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("Receiver Çalıştı.");
	    context.startService(new Intent(context, Servis.class));
	}

}
