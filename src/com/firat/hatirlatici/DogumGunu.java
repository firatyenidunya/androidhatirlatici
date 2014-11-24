package com.firat.hatirlatici;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

public class DogumGunu {
	static DatabaseHandler db;

	public DogumGunu(Context context) {
		db = new DatabaseHandler(context);
	}

	public static String[] DogumGunuGetir(int position, String[] id) {
		String[] dizi = new String[4];
		if (!id[position].equals("")) {
			Cursor cursor = db.getReadableDatabase().query(db.TABLE_DOGUMGUNU,
					new String[] { "id,ad, desc, date" }, "id=?",
					new String[] { id[position] }, null, null, "id desc");
			if (cursor.getCount() > 0) {
				try {
					cursor.moveToFirst();
					dizi[0] = id[position]; // id
					dizi[1] = cursor.getString(1); // title
					dizi[2] = cursor.getString(2); // desc
					dizi[3] = cursor.getString(3); // date

				} catch (CursorIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		}
		return dizi;
	}

	public static int DogumGunuUpdate(String title, String desc, String date, String date2,
			int position, String[] id) {
		int cursor = 0;
		if (!title.equals("") && !date.equals("")) {
			ContentValues values = new ContentValues();
			values.put("ad", title);
			values.put("desc", desc);
			values.put("date", date);
			values.put("date2", date2);
			cursor = db.getWritableDatabase().update(db.TABLE_DOGUMGUNU,
					values, "id=" + id[position], null);
		}
		return cursor;
	}

	public static int DogumGunuDelete(int position, String[] id) {
		int i = 0;
		if (Integer.parseInt(id[position]) > 0) {
			i = db.getWritableDatabase().delete(db.TABLE_DOGUMGUNU,
					"id=" + id[position], null);
		}
		return i;
	}
}
