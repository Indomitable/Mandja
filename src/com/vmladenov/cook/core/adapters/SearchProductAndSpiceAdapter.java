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

package com.vmladenov.cook.core.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class SearchProductAndSpiceAdapter extends CursorAdapter {

	private static final String PRODUCT_SPICE_SEARCH_SQL = "select rowid _id,  title from ( "
			+ "select rowid, title from PRODUCTS where title like '%s' "
			+ " union all "
			+ "select rowid, title from SPICES where title like '%s')";

	SQLiteDatabase db;

	public SearchProductAndSpiceAdapter(Context context, String dbPath) {
		super(context, null, true);
		db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	public Cursor getProductsAndSpices(CharSequence constraint) {
		String search = constraint + "%";
		String sql = String.format(PRODUCT_SPICE_SEARCH_SQL, search, search);
		return db.rawQuery(sql, null);
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (constraint == null)
			return null;
		return getProductsAndSpices(constraint);
	}

	@Override
	public CharSequence convertToString(Cursor cursor) {
		return cursor.getString(1);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final CharSequence text = convertToString(cursor);
		TextView txtView = (TextView) view;
		txtView.setText(text);
		txtView.setTextColor(Color.BLACK);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
	}

	public void close() {
		if (db.isOpen())
			db.close();
	}

}
