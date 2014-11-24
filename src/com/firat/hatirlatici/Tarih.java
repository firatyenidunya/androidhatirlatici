package com.firat.hatirlatici;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tarih {
	public static Date convertToDate(String tarih_zaman, String form) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(form);
		ParsePosition pos = new ParsePosition(0);
		Date d = dateFormat.parse(tarih_zaman, pos);
		return d;
	}
}
