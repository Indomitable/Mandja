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
		final View view = inflater.inflate(
				android.R.layout.simple_dropdown_item_1line, parent, false);
		return view;
	}

	public void close() {
		if (db.isOpen())
			db.close();
	}

}
