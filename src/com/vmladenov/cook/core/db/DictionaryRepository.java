package com.vmladenov.cook.core.db;

import android.database.sqlite.SQLiteDatabase;
import com.vmladenov.cook.core.GlobalStrings;

public class DictionaryRepository extends BaseRepository {

    public DictionaryRepository(SQLiteDatabase db) {
        super(db);
    }
    
    @Override
	protected String getTableName() {
		return GlobalStrings.DICTIONARY_TABLE_NAME;
	}

}
