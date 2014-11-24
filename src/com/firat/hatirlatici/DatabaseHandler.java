package com.firat.hatirlatici;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1; 

	// Database Name
	private static final String DATABASE_NAME = "hatirlatici";

	// Contacts table name
	public static final String TABLE_ETKINLIK = "etkinlik";
	public static final String TABLE_DOGUMGUNU = "dogumgunu";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_ETKINLIK_TABLE = "CREATE TABLE if not exists "
				+ TABLE_ETKINLIK
				+ " (id INTEGER PRIMARY KEY , title TEXT , desc TEXT , date DATE , time TEXT);";
		String CREATE_DOGUMGUNU_TABLE = "CREATE TABLE if not exists "
				+ TABLE_DOGUMGUNU
				+ " (id INTEGER PRIMARY KEY , ad TEXT , desc TEXT , date DATE , date2 TEXT);";
		db.execSQL(CREATE_ETKINLIK_TABLE);
		db.execSQL(CREATE_DOGUMGUNU_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOGUMGUNU);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ETKINLIK);

		// Create tables again
		onCreate(db);
	}

	public void insertSql(ContentValues values , String table) {
		long i = this.getWritableDatabase().insert(table, null, values);		
	}
}
