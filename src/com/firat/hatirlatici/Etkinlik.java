package com.firat.hatirlatici;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

public class Etkinlik {
	static DatabaseHandler db;

	public Etkinlik(Context context) {
		db = new DatabaseHandler(context);
	}

	public static String[] etkinlikGetir(int position, String[] id) {
		String[] dizi = new String[5];
		if (!id[position].equals("")) {
			Cursor cursor = db.getReadableDatabase().query(db.TABLE_ETKINLIK,
					new String[] { "id,title,desc,date,time" }, "id=?",
					new String[] { id[position] }, null, null, "id desc");
			if (cursor.getCount() > 0) {
				try {
					cursor.moveToFirst();
					dizi[0] = id[position]; // id
					dizi[1] = cursor.getString(1); // title
					dizi[2] = cursor.getString(2); // desc
					dizi[3] = cursor.getString(3); // date
					dizi[4] = cursor.getString(4);// time

				} catch (CursorIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		}
		return dizi;
	}

	public static int etkinlikUpdate(String title, String desc, String date, String time, int position, String[] id) {
		int cursor = 0;
		if (!title.equals("") && !date.equals("") && !time.equals("")) {
			ContentValues values = new ContentValues();
			values.put("title", title);
			values.put("desc", desc);
			values.put("date", date);
			values.put("time", time);
			cursor = db.getWritableDatabase().update(db.TABLE_ETKINLIK, values,
					"id=" + id[position], null);
		}
		return cursor;

	}

	public static int etkinlikDelete(int position, String[] id) {
		int i = 0;
		if (Integer.parseInt(id[position]) > 0) {
			i = db.getWritableDatabase().delete(db.TABLE_ETKINLIK,
					"id=" + id[position], null);
		}
		return i;
	}
}
