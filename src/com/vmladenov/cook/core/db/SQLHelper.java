package com.vmladenov.cook.core.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vmladenov.cook.core.converters.ICursorConverter;

public class SQLHelper {
	public static <T> ArrayList<T> ExecuteSql(String sql, String dbPath,
			ICursorConverter<T> convert) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READONLY);
		ArrayList<T> list = new ArrayList<T>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				do {
					T item = convert.Convert(cursor);
					list.add(item);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			return list;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			if (db.isOpen())
				db.close();
		}
		return list;
	}

	public static <T> ArrayList<T> ExecuteSql(String sql, String dbPath,
			ICursorConverter<T> convert, String... args) {

		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READONLY);
		ArrayList<T> list = new ArrayList<T>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, args);
			if (cursor.moveToFirst()) {
				do {
					T item = convert.Convert(cursor);
					list.add(item);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			return list;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			if (db.isOpen())
				db.close();
		}
		return list;
	}

	public static <T> T ExecuteSingleSql(String sql, String dbPath,
			ICursorConverter<T> convert) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READONLY);
		T item = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				item = convert.Convert(cursor);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			if (db.isOpen())
				db.close();
		}
		return item;
	}

	public static <T> T ExecuteSingleSql(String sql, String dbPath,
			ICursorConverter<T> convert, String... args) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READONLY);
		T item = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, args);
			if (cursor.moveToFirst()) {
				item = convert.Convert(cursor);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			if (db.isOpen())
				db.close();
		}
		return item;
	}

	public static Integer ExecuteScalarIntSql(String sql, String dbPath) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			if (db.isOpen())
				db.close();
		}
		return null;
	}

	public static Integer ExecuteScalarIntSql(String sql, String dbPath,
			String... args) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, args);
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			if (db.isOpen())
				db.close();
		}
		return null;
	}

	public static String ExecuteScalarStringSql(String sql, String dbPath) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				return cursor.getString(0);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
			if (db.isOpen())
				db.close();
		}
		return null;
	}

	public static void ExecuteNonQuery(String sql, String dbPath) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		try {
			db.execSQL(sql);
		} finally {
			if (db.isOpen())
				db.close();
		}
	}

	public static void ExecuteNonQuery(String sql, String dbPath,
			Object... args) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		try {
			db.execSQL(sql, args);
		} finally {
			if (db.isOpen())
				db.close();
		}
	}

	public static long ExecuteInsert(String tableName, String dbPath,
			ContentValues values) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		try {
			return db.insert(tableName, null, values);
		} finally {
			if (db.isOpen())
				db.close();
		}
	}
}
