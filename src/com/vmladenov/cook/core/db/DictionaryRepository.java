package com.vmladenov.cook.core.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.converters.DictionarySmallViewConverter;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.objects.UsefulView;

import java.util.ArrayList;

public class DictionaryRepository implements ISmallPreviewListRepository {
    private final SQLiteDatabase db;

    SQLiteStatement maxIdStatement;

    private static final String MAX_ID_SQL = "SELECT MAX(ID) FROM "
            + GlobalStrings.DICTIONARY_TABLE_NAME;

    private static final String DICTIONARY_SELECT_CLAUSE = "SELECT ID, IMAGE_URL, TITLE, DESCRIPTION "
            + " FROM " + GlobalStrings.DICTIONARY_TABLE_NAME;

    private static final String DICTIONARY_PAGE_SQL = DICTIONARY_SELECT_CLAUSE + " %s LIMIT %d,10";

    private static final String DICTIONARY_SQL = DICTIONARY_SELECT_CLAUSE + " WHERE ID = %d";

    public DictionaryRepository(SQLiteDatabase db) {
        this.db = db;
        maxIdStatement = db.compileStatement(MAX_ID_SQL);
    }

    @Override
    public long getMaxId() {
        return maxIdStatement.simpleQueryForLong();
    }

    @Override
    public ArrayList<SmallPreview> getPreviews(long start, int sorting) {
        String order = "";
        switch (sorting) {
            case 1:
                order = " ORDER BY TITLE ";
                break;
            default:
                order = "";
                break;
        }
        String sql = String.format(DICTIONARY_PAGE_SQL, order, start);
        return SQLHelper.ExecuteSql(sql, db, new DictionarySmallViewConverter());
    }

    public void close() {
        maxIdStatement.close();
    }

    public UsefulView getDictionaryView(long id) {
        UsefulView view = new UsefulView();
        String sql = String.format(DICTIONARY_SQL, id);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            view.setId(id);
            view.setTitle(cursor.getString(2));
            view.setDescription(cursor.getString(3));
            view.setImageUrl(cursor.getString(1));
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();

        return view;
    }

}
