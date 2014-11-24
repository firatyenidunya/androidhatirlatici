package com.firat.hatirlatici;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

public class Ayarlar extends Activity implements OnClickListener {
	Context context = this;
	static Dialog dialog;
	private int saat;
	private int dakika;
	private Calendar cal;
	static TextView saatayar;
	SharedPreferences ayarlar;
	static SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // title'ı
		// kaldırdık.
		setContentView(R.layout.activity_ayarlar);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		TextView titleWindow = (TextView) findViewById(R.id.tvTitleText);

		titleWindow.setText("Ayarlar");
		Button sec = (Button) findViewById(R.id.sec);
		sec.setOnClickListener(this);
		saatayar = (TextView) findViewById(R.id.value);
		ayarlar = getSharedPreferences("ayarlar",MODE_PRIVATE);
		editor = ayarlar.edit();
		saatayar.setText(ayarlar.getString("bSaat",""));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.sec) {
			cal = Calendar.getInstance();
			saat = cal.get(Calendar.HOUR_OF_DAY);
			dakika = cal.get(Calendar.MINUTE);
			dialog = new Dialog(context);
			showDialog(0);

		}
	}

	@Override
	@Deprecated
	public Dialog onCreateDialog(int id) {
		return new TimePickerDialog(this, timePickerListener, saat, dakika,
				true);

	}

	public static TimePickerDialog.OnTimeSetListener timePickerListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			saatayar.setText(hourOfDay + ":" + minute);
			editor.putString("bSaat", hourOfDay+":"+minute);
			editor.commit(); //kaydet
		}

		
	};
}
