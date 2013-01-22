/*
 * Copyright (C) 2011 Ventsislav Mladenov <ventsislav dot mladenov at gmail dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.vmladenov.cook.core.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.vmladenov.cook.core.converters.ICursorConverter;

import java.util.ArrayList;

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
