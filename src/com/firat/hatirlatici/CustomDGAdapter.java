package com.firat.hatirlatici;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDGAdapter extends ArrayAdapter {
	private final Context context;
	private final String[] id, title, desc, date;
	private final int resource;
	DatabaseHandler db;
	View rowView;

	public CustomDGAdapter(Context context, int resource, String[] id,
			String[] title, String[] desc, String[] date) {
		super(context, resource, id);
		this.context = context;
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.date = date;
		this.resource = resource;
		db = new DatabaseHandler(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		rowView = inflater.inflate(R.layout.listview_etkinlik, parent, false);
		ImageView btnAyarEtkinlik = (ImageView) rowView
				.findViewById(R.id.btnAyarEtkinlik);
		TextView txTitle = (TextView) rowView.findViewById(R.id.txTitle);
		TextView txDesc = (TextView) rowView.findViewById(R.id.txDesc);
		TextView txDate = (TextView) rowView.findViewById(R.id.txDate);
		ImageView dateimage = (ImageView) rowView.findViewById(R.id.ImageView01);
		TextView txSaat = (TextView) rowView.findViewById(R.id.txSaat);
		ImageView saatimage = (ImageView) rowView.findViewById(R.id.ImageView01);
		txSaat.setVisibility(View.INVISIBLE);
		saatimage.setVisibility(View.INVISIBLE);
		// ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		txTitle.setText(title[position]);
		txDesc.setText(desc[position]);
		txDate.setText(date[position]);

		return rowView;
	}
}
