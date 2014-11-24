package com.firat.hatirlatici;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.R.anim;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.Toast;

public class Servis extends Service {
	Timer zamanlayici;
	Handler yardimci; // arka planda iken ön tarafta ekrana ait işlemler yapar.
	long zaman = 59000; // 1000 = 1sn => 59000 = 59sn
	Context context = this;
	DatabaseHandler db = new DatabaseHandler(context);
	SharedPreferences ayarlar;
	static SharedPreferences.Editor editor;

	SimpleDateFormat format = new SimpleDateFormat("d/M"); //doğum günü için tarih foramtı.
	SimpleDateFormat format2 = new SimpleDateFormat("d/M/yyyy"); //etkinlik için tarih formatı belirledik.
	SimpleDateFormat format1 = new SimpleDateFormat("H:m"); // etkinlik için saat formatı belirledik.

	@Override
	public void onCreate() {
		super.onCreate();
		ayarlar = getSharedPreferences("ayarlar", MODE_PRIVATE); //ayarlar veritabanını çağırdık.
		editor = ayarlar.edit(); // ayarları okumak için edit metotunu kullandık.
		zamanlayici = new Timer(); 
		yardimci = new Handler(Looper.getMainLooper());
		zamanlayici.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				new dogumgunutask().execute();
				new etkinlikTask().execute();
			}
		}, 0, zaman);
		
		
	}
	class dogumgunutask extends AsyncTask<Integer, Void, String>{

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stu
			DogumGunuKontrol();
			return null;
		}
		
	}
	class etkinlikTask extends AsyncTask<Integer, Void, String>{
		
		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stu
			EtkinlikKontrol();
			return null;
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void DogumGunuKontrol() {
		long zaman = System.currentTimeMillis(); // anlık zamanı aldık.
		String date = format.format(zaman); //gün/ay şeklinde tarih aldık.
		String saat = format1.format(zaman); //Saat : dakika
		if (saat.equals(ayarlar.getString("bSaat", ""))) {
			Cursor cursor = db.getReadableDatabase().rawQuery(
					"SELECT * FROM " + db.TABLE_DOGUMGUNU + " WHERE date2=?",
					new String[] { date });
			try {
				while (cursor.moveToNext()) {
					String ad = cursor.getString(1);
					String not = cursor.getString(2);
					Bildirim(ad, not, 0);
				}
			} finally {
				cursor.close();
			}
		}
	}

	private void EtkinlikKontrol() {
		long zaman = System.currentTimeMillis();
		String date = format2.format(zaman);
		String saat = format1.format(zaman);
		
		Cursor cursor = db.getReadableDatabase().rawQuery(
				"SELECT * FROM " + db.TABLE_ETKINLIK
						+ " WHERE date=? AND time=?",
				new String[] { date, saat });
		try {
			while (cursor.moveToNext()) {
				String title = cursor.getString(1);
				String desc = cursor.getString(2);
				Bildirim(title, desc, 1);
			}
		} finally {
			cursor.close();
		}

	}

	private void Bildirim(final String ad, final String not, final int kontrol) {
		yardimci.post(new Runnable() {

			@Override
			public void run() {
				if (kontrol == 0) { // doğum günü ise.
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle(ad + " - Doğum günü");
					builder.setIcon(android.R.drawable.ic_menu_my_calendar);
					builder.setMessage(not);
					builder.setPositiveButton("Tamam",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Do something
									dialog.dismiss();
								}
							});

					AlertDialog alert = builder.create(); // dialogu oluştur.
					alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); //alert tipinde dialog oluşturur.
					alert.show(); // dialogu göster.
				} else if (kontrol == 1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle(ad);
					builder.setIcon(android.R.drawable.ic_menu_my_calendar);
					builder.setMessage(not);
					builder.setPositiveButton("Tamam",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Do something
									dialog.dismiss();
								}
							});

					AlertDialog alert = builder.create();
					alert.getWindow().setType(
							WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					alert.show();
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		zamanlayici.cancel();
		super.onDestroy();
		startService(new Intent(Servis.this, Servis.class)); // servis kapandığı zaman servisi tekrar başlatır.
	}

}
