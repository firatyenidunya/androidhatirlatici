package com.firat.hatirlatici;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	ImageView ekle, birthday;
	ListView list;
	final Context context = this; //Uygulama ekranını tanımladık.
	private int gun;
	private int ay;
	private int yil;
	private int saat;
	private int dakika;
	private Calendar cal;
	static Dialog dialog;
	DatabaseHandler db = new DatabaseHandler(this); //veritabanını çağırdık.
	String[] id, title, desc, date, time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // title'ı
															// kaldırdık.
		setContentView(R.layout.activity_main); ///res/layout/activity_main.xml layout dosyasını ekrana tanımladık.

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar); // titlebar.xml dosyasını java dosyasına
									// tanıtıyoruz ekle butonuna ulaşabilmek
									// için.
		
//		db.onUpgrade(db.getWritableDatabase(), 0, 1); //eğer veritabanını silmek istersek bu satırı açarız. (Kendim çalışırken veritabanını silmek için kullandım.)
			
		startService(new Intent(MainActivity.this, Servis.class));
		etGuncelle();
		DogumGunu dg = new DogumGunu(context);
		ekle = (ImageView) findViewById(R.id.ekle); // ekleyi tanımlıyoruz
		birthday = (ImageView) findViewById(R.id.birthday);
		birthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, Birthday.class));
			}
		});
		ekle.setOnClickListener(new OnClickListener() { // tıklamayı dinle
			TextView tarih, txtSaat;
			EditText title, not;

			@Override
			public void onClick(View v) {
				dialog = new Dialog(context);
				dialog.getWindow().setBackgroundDrawableResource(
						R.drawable.dialog);
				dialog.setContentView(R.layout.custom);
				dialog.setTitle("Oluştur");

				tarih = (TextView) dialog.findViewById(R.id.txtTarih);
				title = (EditText) dialog.findViewById(R.id.etTitle);
				not = (EditText) dialog.findViewById(R.id.etNot);
				Button dialogButton = (Button) dialog.findViewById(R.id.iptal);
				Button ekle = (Button) dialog.findViewById(R.id.ekle);
				txtSaat = (TextView) dialog.findViewById(R.id.txtSaat);

				DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
				final String togunsDate = dateFormat.format(System
						.currentTimeMillis());

				SimpleDateFormat dateFormat1 = new SimpleDateFormat("KK:mm");
				final String timeOfDay = dateFormat1.format(System
						.currentTimeMillis());
				tarih.setText(togunsDate);

				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				cal = Calendar.getInstance();
				gun = cal.get(Calendar.DAY_OF_MONTH);
				ay = cal.get(Calendar.MONTH);
				yil = cal.get(Calendar.YEAR);
				saat = cal.get(Calendar.HOUR_OF_DAY);
				dakika = cal.get(Calendar.MINUTE);

				txtSaat.setText(timeOfDay);
				txtSaat.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cal = Calendar.getInstance();
						gun = cal.get(Calendar.DAY_OF_MONTH);
						ay = cal.get(Calendar.MONTH);
						yil = cal.get(Calendar.YEAR);
						showDialog(1);
					}
				});
				tarih.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cal = Calendar.getInstance();
						gun = cal.get(Calendar.DAY_OF_MONTH);
						ay = cal.get(Calendar.MONTH);
						yil = cal.get(Calendar.YEAR);
						showDialog(0);
					}
				});

				ekle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!title.getText().toString().equals("")) {
							if (Tarih.convertToDate(
									tarih.getText().toString() + " "
											+ txtSaat.getText().toString(),
									"d/M/yyyy H:m").compareTo(
									Tarih.convertToDate(togunsDate.toString()
											+ " " + timeOfDay.toString(),
											"d/M/yyyy H:m")) > 0) {

								ContentValues contentValues = new ContentValues();
								contentValues.put("title", title.getText()
										.toString());
								contentValues.put("desc", not.getText()
										.toString());
								contentValues.put("date", tarih.getText()
										.toString());
								contentValues.put("time", txtSaat.getText()
										.toString());
								db.insertSql(contentValues, db.TABLE_ETKINLIK);
								dialog.dismiss();
								etGuncelle();
							} else {
								new toast(context,
										"Tarihi veya Saati Kontrol Edin.");
							}

						} else {
							new toast(context, "Başlık Girilmelidir.");
						}
					}
				});

				dialog.show();
			}
		});
	}

	public void etGuncelle() {
		Cursor cursor = db.getReadableDatabase().rawQuery(
				"SELECT * FROM " + db.TABLE_ETKINLIK + " ORDER BY id DESC",
				null); //etkinlikleri tersten aldık.
		if (cursor.getCount() > 0) {
			int i = 0;
			id = new String[cursor.getCount()]; //okunan etkinlik sayısına göre id, title, desc ,date ,time dizilerini tanımlıyoruz.
			title = new String[cursor.getCount()];
			desc = new String[cursor.getCount()];
			date = new String[cursor.getCount()];
			time = new String[cursor.getCount()];
			while (cursor.moveToNext()) {
				id[i] = cursor.getString(0);
				title[i] = cursor.getString(1);
				desc[i] = cursor.getString(2);
				date[i] = cursor.getString(3);
				time[i] = cursor.getString(4);
				i++;
			}
			list = (ListView) findViewById(R.id.list);

			final CustomHatirlaticiAdapter adapter = new CustomHatirlaticiAdapter(
					context, R.layout.listview_etkinlik, id, title, desc, date,
					time, cursor.getCount());
			list.setAdapter(adapter); 
			adapter.notifyDataSetChanged(); //adapter kendini günceller.
		} else {
			id = new String[1];
			title = new String[1];
			desc = new String[1];
			date = new String[1];
			time = new String[1];
			id[0] = "-1";
			title[0] = "Başlık";
			desc[0] = "Not";
			date[0] = "Tarih";
			time[0] = "Saat";
			list = (ListView) findViewById(R.id.list);

			final CustomHatirlaticiAdapter adapter = new CustomHatirlaticiAdapter(
					context, R.layout.listview_etkinlik, id, title, desc, date,
					time, cursor.getCount());
			list.setAdapter(adapter);
		}
		list.setOnItemClickListener(this);
	}

	@Override
	@Deprecated
	public Dialog onCreateDialog(int id) {
		if (id == 0) {

			return new DatePickerDialog(this, datePickerListener, yil, ay,
					gun);

		} else if (id == 1) {
			return new TimePickerDialog(this, timePickerListener, saat,
					dakika, true);
		}
		return dialog;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ayarlar:
			startActivity(new Intent(MainActivity.this, Ayarlar.class));
			// finish();
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long idd) {

		final TextView tarih;
		final EditText not;
		final TextView txtSaat;
		final EditText etTitle;
		ListView list;
		etGuncelle();
		dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);
		dialog.setContentView(R.layout.custom);
		dialog.setTitle("Düzenle");
		String[] dizi;
		Etkinlik etkinlik = new Etkinlik(context);
		dizi = etkinlik.etkinlikGetir(position, id);
		// dizi = etkinlikGetir(position);

		tarih = (TextView) dialog.findViewById(R.id.txtTarih);
		etTitle = (EditText) dialog.findViewById(R.id.etTitle);
		not = (EditText) dialog.findViewById(R.id.etNot);
		Button iptal = (Button) dialog.findViewById(R.id.iptal);
		Button kaydet = (Button) dialog.findViewById(R.id.ekle);
		Button sil = (Button) dialog.findViewById(R.id.sil);
		sil.setVisibility(View.VISIBLE);
		kaydet.setText("Kaydet");
		txtSaat = (TextView) dialog.findViewById(R.id.txtSaat);

		etTitle.setText(dizi[1]);
		tarih.setText(dizi[3]);
		txtSaat.setText(dizi[4]);
		not.setText(dizi[2]);

		tarih.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cal = Calendar.getInstance();
				gun = cal.get(Calendar.DAY_OF_MONTH);
				ay = cal.get(Calendar.MONTH);
				yil = cal.get(Calendar.YEAR);
				saat = cal.get(Calendar.HOUR_OF_DAY);
				dakika = cal.get(Calendar.MINUTE);
				showDialog(0);
			}
		});
		txtSaat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cal = Calendar.getInstance();
				gun = cal.get(Calendar.DAY_OF_MONTH);
				ay = cal.get(Calendar.MONTH);
				yil = cal.get(Calendar.YEAR);
				saat = cal.get(Calendar.HOUR_OF_DAY);
				dakika = cal.get(Calendar.MINUTE);
				showDialog(1);
			}
		});

		iptal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});

		kaydet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Integer.parseInt(id[position]) >0) {
					DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy H:m");
					final String togunsDate = dateFormat.format(System
							.currentTimeMillis());

					SimpleDateFormat dateFormat1 = new SimpleDateFormat("KK:mm");
					final String timeOfDay = dateFormat1.format(System
							.currentTimeMillis());
					if (!etTitle.getText().toString().equals("")) {
						if (Tarih.convertToDate(
								tarih.getText().toString() + " "
										+ txtSaat.getText().toString(),
								"d/M/yyyy H:m")
								.compareTo(
										Tarih.convertToDate(
												togunsDate.toString() + " "
														+ timeOfDay.toString(),
												"d/M/yyyy H:m")) > 0) {
							int i = Etkinlik
									.etkinlikUpdate(etTitle.getText()
											.toString(), not.getText()
											.toString(), tarih.getText()
											.toString(), txtSaat.getText()
											.toString(), position, id);
							if (i > 0) {
								dialog.dismiss();
								etGuncelle();
							}
						} else {
							new toast(context, "Tarihi kontrol edin.");
						}

					} else {
						new toast(context, "Başlık Girilmelidir.");
					}
				}
			}
		});

		sil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = Etkinlik.etkinlikDelete(position, id);
				// int i = etkinlikDelete(position);
				if (i > 0) {
					dialog.dismiss();
					etGuncelle();
				}
			}
		});

		dialog.show();

	}
	
	public static DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedyil, int selecteday,
				int selectedgun) {

			TextView text = (TextView) dialog.findViewById(R.id.txtTarih);
			text.setText(selectedgun + "/" + (selecteday + 1) + "/"
					+ selectedyil);
		}
	};
	public static TimePickerDialog.OnTimeSetListener timePickerListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			TextView saat = (TextView) dialog.findViewById(R.id.txtSaat);
			saat.setText(hourOfDay + ":" + minute);
		}
	};
}
