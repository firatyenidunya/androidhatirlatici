package com.firat.hatirlatici;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomHatirlaticiAdapter extends ArrayAdapter {
	private final Context context;
	private final String[] id, title, desc, date, time;
	private final int resource, count;
	DatabaseHandler db;
	View rowView;



	public CustomHatirlaticiAdapter(Context context, int resource, String[] id,
			String[] title, String[] desc, String[] date, String[] time,
			int count) {
		super(context, resource, id);
		this.context = context;
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.date = date;
		this.time = time;
		this.resource = resource;
		this.count = count;
		db = new DatabaseHandler(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 

		rowView = inflater.inflate(R.layout.listview_etkinlik, parent, false); // çalışacağı xml dosyasını viewgroup içerisinden seçer.
		
		TextView txTitle = (TextView) rowView.findViewById(R.id.txTitle);  //adı textviewini  seçtik.
		TextView txDesc = (TextView) rowView.findViewById(R.id.txDesc);
		TextView txDate = (TextView) rowView.findViewById(R.id.txDate);
		TextView txSaat = (TextView) rowView.findViewById(R.id.txSaat);
		
		txTitle.setText(title[position]);
		txDesc.setText(desc[position]);
		txDate.setText(date[position]);
		txSaat.setText(time[position]);

		return rowView;
	}
}
