package com.vmladenov.cook.core.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vmladenov.cook.core.GlobalStrings;

public class ProductsRepository extends BaseRepository {

	private static final String PRODUCT_SPICE_SEARCH_SQL = "select rowid _id,  title from ( "
			+ "select rowid, title from PRODUCTS where title like '%s' " + " union all "
			+ "select rowid, title from SPICES where title like '%s')";

	public ProductsRepository(SQLiteDatabase db) {
		super(db);
	}

	@Override
	protected String getTableName() {
		return GlobalStrings.PRODUCTS_TABLE_NAME;
	};

	public Cursor getProductsAndSpices(CharSequence constraint) {
		String search = constraint + "%";
		String sql = String.format(PRODUCT_SPICE_SEARCH_SQL, search, search);
		return db.rawQuery(sql, null);
	}
}
