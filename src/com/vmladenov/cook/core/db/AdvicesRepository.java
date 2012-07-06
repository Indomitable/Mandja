package com.vmladenov.cook.core.db;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;

import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.converters.SimpleViewConverter;
import com.vmladenov.cook.core.objects.SimpleView;

public class AdvicesRepository extends BaseRepository {

	private static final String ADVICE_CATEGORIES_SQL = "SELECT ID, TITLE FROM ADVICE_CATEGORIES";

	public AdvicesRepository(SQLiteDatabase db) {
		super(db);
	}

	@Override
	protected String getTableName() {
		return GlobalStrings.ADVICE_TABLE_NAME;
	}

	public ArrayList<SimpleView> getCategories() {
		return SQLHelper.ExecuteSql(ADVICE_CATEGORIES_SQL, db, new SimpleViewConverter());
	}

}
