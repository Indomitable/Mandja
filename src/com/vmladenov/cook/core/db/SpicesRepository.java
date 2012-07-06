package com.vmladenov.cook.core.db;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;

import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.converters.PreviewListItemConverter;
import com.vmladenov.cook.domain.PreviewListItem;

public class SpicesRepository extends BaseRepository {

	private static final String SPICES_SELECT_CLAUSE = "SELECT ID, SPICE_ID, URL, "
			+ "IMAGE_URL, TITLE, LATIN_TITLE, DESCRIPTION " + " FROM "
			+ GlobalStrings.SPICES_TABLE_NAME;

	private static final String SPICES_SEARCH_SQL = "PRAGMA case_sensitive_like = false; "
			+ SPICES_SELECT_CLAUSE + " WHERE TITLE like '%s'";

	public SpicesRepository(SQLiteDatabase db) {
		super(db);
	}

	@Override
	protected String getTableName() {
		return GlobalStrings.SPICES_TABLE_NAME;
	};

	public ArrayList<PreviewListItem> searchSpices(String query) {
		String value = "%" + query + "%";
		String sql = String.format(SPICES_SEARCH_SQL, value);
		return SQLHelper.ExecuteSql(sql, db, new PreviewListItemConverter());
	}

}
