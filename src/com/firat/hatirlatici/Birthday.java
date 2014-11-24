package com.firat.hatirlatici;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class Birthday extends Activity implements OnItemClickListener,
		OnClickListener {
	ImageView ekle, birthday;
	ListView list;
	final Context context = this;
	private int gun;
	private int ay;
	private int yil;
	private Calendar cal;
	static Dialog dialog;
	DatabaseHandler db = new DatabaseHandler(this);
	String[] id, ad, not, date;
	static String date2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // title'ı
		// kaldırdık.
		setContentView(R.layout.activity_birthday);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		TextView titleWindow = (TextView) findViewById(R.id.tvTitleText);
		ImageView ekle = (ImageView) findViewById(R.id.ekle);
		ekle.setOnClickListener(this);

		titleWindow.setText("Doğum Günleri");
		dgGuncelle();
	}

	public void dgGuncelle() {
		Cursor cursor = db.getReadableDatabase().rawQuery(
				"SELECT * FROM " + db.TABLE_DOGUMGUNU + " ORDER BY id DESC",
				null);
		if (cursor.getCount() > 0) {
			int i = 0;
			id = new String[cursor.getCount()];
			ad = new String[cursor.getCount()];
			not = new String[cursor.getCount()];
			date = new String[cursor.getCount()];

			while (cursor.moveToNext()) {
				id[i] = cursor.getString(0);
				ad[i] = cursor.getString(1);
				not[i] = cursor.getString(2);
				date[i] = cursor.getString(3);
				i++;
			}
			list = (ListView) findViewById(R.id.list);

			final CustomDGAdapter adapter = new CustomDGAdapter(context,
					R.layout.listview_etkinlik, id, ad, not, date);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else {
			id = new String[1];
			ad = new String[1];
			not = new String[1];
			date = new String[1];

			id[0] = "-1";
			ad[0] = "İsim";
			not[0] = "Not";
			date[0] = "Tarih";
			list = (ListView) findViewById(R.id.list);

			final CustomDGAdapter adapter = new CustomDGAdapter(context,
					R.layout.listview_etkinlik, id, ad, not, date);
			list.setAdapter(adapter);
		}
		list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long idd) {

		final TextView tarih;
		final EditText not;
		final TextView txtSaat;
		final EditText etTitle;
		ListView list;
		dgGuncelle();
		dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);
		dialog.setContentView(R.layout.custom);
		dialog.setTitle("Düzenle");
		String[] dizi;
		DogumGunu dg = new DogumGunu(context);
		dizi = dg.DogumGunuGetir(position, id);
		// dizi = etkinlikGetir(position);

		tarih = (TextView) dialog.findViewById(R.id.txtTarih);
		TextView baslik = (TextView) dialog.findViewById(R.id.textView1);
		baslik.setText("Ad:   ");
		LinearLayout saat = (LinearLayout) dialog
				.findViewById(R.id.linearLayout3);
		saat.setVisibility(View.INVISIBLE);
		etTitle = (EditText) dialog.findViewById(R.id.etTitle);
		not = (EditText) dialog.findViewById(R.id.etNot);
		Button iptal = (Button) dialog.findViewById(R.id.iptal);
		Button kaydet = (Button) dialog.findViewById(R.id.ekle);
		Button sil = (Button) dialog.findViewById(R.id.sil);
		sil.setVisibility(View.VISIBLE);
		kaydet.setText("Kaydet");

		etTitle.setText(dizi[1]);
		tarih.setText(dizi[3]);
		not.setText(dizi[2]);

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

		iptal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}

		});

		kaydet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Integer.parseInt(id[position]) > 0) {
					DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
					final String togunsDate = dateFormat.format(System
							.currentTimeMillis());

					if (!etTitle.getText().toString().equals("")) {
						
							int i = DogumGunu.DogumGunuUpdate(etTitle.getText()
									.toString(), not.getText().toString(),
									tarih.getText().toString(), date2, position, id);
							if (i > 0) {
								dialog.dismiss();
								dgGuncelle();
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
				int i = DogumGunu.DogumGunuDelete(position, id);
				// int i = etkinlikDelete(position);
				if (i > 0) {
					dialog.dismiss();
					dgGuncelle();
				}
			}
		});

		dialog.show();
	}

	@Override
	@Deprecated
	public Dialog onCreateDialog(int id) {
		if (id == 0) {

			return new DatePickerDialog(this, datePickerListener, yil, ay,
					gun);

		} else if (id == 1) {
			// return new TimePickerDialog(this, pick.timePickerListener, saat,
			// dakika, true);
		}
		return dialog;

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ekle) {
			final TextView tarih;
			final TextView txtSaat;
			final EditText title;
			final EditText not;
			dialog = new Dialog(context);
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);
			dialog.setContentView(R.layout.custom);
			dialog.setTitle("Oluştur");

			TextView baslik = (TextView) dialog.findViewById(R.id.textView1);
			baslik.setText("Ad:   ");
			LinearLayout saatlayout = (LinearLayout) dialog
					.findViewById(R.id.linearLayout3);
			saatlayout.setVisibility(View.INVISIBLE);

			tarih = (TextView) dialog.findViewById(R.id.txtTarih);
			title = (EditText) dialog.findViewById(R.id.etTitle);
			not = (EditText) dialog.findViewById(R.id.etNot);
			Button dialogButton = (Button) dialog.findViewById(R.id.iptal);
			Button ekle = (Button) dialog.findViewById(R.id.ekle);

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

			tarih.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDialog(0);
				}
			});

			ekle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!title.getText().toString().equals("")) {
							ContentValues values = new ContentValues();
							values.put("ad", title.getText().toString());
							values.put("desc", not.getText().toString());
							values.put("date", tarih.getText().toString());
							values.put("date2", date2);
							db.insertSql(values, db.TABLE_DOGUMGUNU);
							dialog.dismiss();
							dgGuncelle();
						

					} else {
						new toast(context, "Başlık Girilmelidir.");
					}
				}
			});

			dialog.show();

		}
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
			startActivity(new Intent(Birthday.this, Ayarlar.class));
			// finish();
			break;

		default:
			break;
		}
		return true;
	}
	
	public static DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedyil, int selecteday,
				int selectedgun) {

			TextView text = (TextView) dialog.findViewById(R.id.txtTarih);
			text.setText(selectedgun + "/" + (selecteday + 1) + "/"
					+ selectedyil);
			date2 = selectedgun+"/"+(selecteday+1);
		}
	};
}
