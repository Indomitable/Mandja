package com.vmladenov.cook.core.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vmladenov.cook.core.converters.ICursorConverter;

public class SQLHelper {
	public static <T> ArrayList<T> ExecuteSql(String sql, SQLiteDatabase db, ICursorConverter<T> convert) {
		ArrayList<T> list = new ArrayList<T>();
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				T item = convert.Convert(cursor);
				list.add(item);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed())
			cursor.close();
		return list;
	}

	public static <T> ArrayList<T> ExecuteSql(String sql, SQLiteDatabase db, ICursorConverter<T> convert, String... args) {
		ArrayList<T> list = new ArrayList<T>();
		Cursor cursor = db.rawQuery(sql, args);
		if (cursor.moveToFirst()) {
			do {
				T item = convert.Convert(cursor);
				list.add(item);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed())
			cursor.close();
		return list;
	}

	public static <T> T ExecuteSingleSql(String sql, SQLiteDatabase db, ICursorConverter<T> convert) {
		T item = null;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			item = convert.Convert(cursor);
		}
		if (cursor != null && !cursor.isClosed())
			cursor.close();
		return item;
	}

	public static <T> T ExecuteSingleSql(String sql, SQLiteDatabase db, ICursorConverter<T> convert, String... args) {
		T item = null;
		Cursor cursor = db.rawQuery(sql, args);
		if (cursor.moveToFirst()) {
			item = convert.Convert(cursor);
		}
		if (cursor != null && !cursor.isClosed())
			cursor.close();
		return item;
	}

	public static Integer ExecuteScalarIntSql(String sql, SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(sql, null);
		try {
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return null;
	}

	public static Integer ExecuteScalarIntSql(String sql, SQLiteDatabase db, String... args) {
		Cursor cursor = db.rawQuery(sql, args);
		try {
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return null;
	}

	public static String ExecuteScalarStringSql(String sql, SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(sql, null);
		try {
			if (cursor.moveToFirst()) {
				return cursor.getString(0);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return null;
	}

	public static void ExecuteNonQuery(String sql, SQLiteDatabase db) {
		db.execSQL(sql);
	}

	public static void ExecuteNonQuery(String sql, SQLiteDatabase db, Object... args) {
		db.execSQL(sql, args);
	}
}
