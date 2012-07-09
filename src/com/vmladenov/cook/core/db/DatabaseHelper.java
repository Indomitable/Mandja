package com.vmladenov.cook.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "cook_personal.db";

	private static String getDbName(Context context) {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return context.getExternalFilesDir(null) + "/" + DATABASE_NAME;
		}
		return context.getFilesDir() + "/" + DATABASE_NAME;
	}

	public DatabaseHelper(Context context) {
		super(context, getDbName(context), null, 1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS MY_RECIPES");
		db.execSQL("DROP TABLE IF EXISTS SHOPPING_LISTS");
		db.execSQL("DROP TABLE IF EXISTS SHOPPING_LIST_ITEMS");
		db.execSQL("DROP TABLE IF EXISTS MY_FAVORITE_RECIPES");
		db.execSQL("DROP TABLE IF EXISTS MY_RECIPE_NOTES");
		onCreate(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE SHOPPING_LISTS ( ID INTEGER PRIMARY KEY, TITLE TEXT, CREATION_DATE DATETIME); ");
		db.execSQL("CREATE TABLE SHOPPING_LIST_ITEMS ( ID INTEGER PRIMARY KEY, LIST_ID INTEGER, TITLE TEXT, ORDER_NUMBER INTEGER, IS_CHECKED INTEGER);");
	}
}
