package com.vmladenov.cook.core.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.vmladenov.cook.core.converters.ICursorConverter;

import java.util.ArrayList;

public class SQLHelper {
    public static <T> ArrayList<T> ExecuteSql(String sql, SQLiteDatabase db,
                                              ICursorConverter<T> convert) {
        ArrayList<T> list = new ArrayList<T>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                T item = convert.Convert(cursor);
                list.add(item);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
        return list;
    }

    public static <T> T ExecuteSingleSql(String sql, SQLiteDatabase db, ICursorConverter<T> convert) {
        T item = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            item = convert.Convert(cursor);
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
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
            if (cursor != null && !cursor.isClosed()) cursor.close();
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
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return null;
    }

    public static long ExecuteScalarLongSql(String sql, SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getLong(0);
            }
        } catch (Exception e) {
            return -1;
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return -1;
    }

    public static void ExecuteNonQuery(String sql, SQLiteDatabase db) {
        db.execSQL(sql);
    }
}
