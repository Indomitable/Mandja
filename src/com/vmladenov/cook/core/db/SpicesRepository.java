package com.vmladenov.cook.core.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.converters.SpiceSmallViewConverter;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.objects.UsefulView;

import java.util.ArrayList;

public class SpicesRepository implements ISmallPreviewListRepository {
    private final SQLiteDatabase db;

    SQLiteStatement maxIdStatement;

    private static final String MAX_ID_SQL = "SELECT MAX(ID) FROM "
            + GlobalStrings.SPICES_TABLE_NAME;

    private static final String SPICES_SELECT_CLAUSE = "SELECT ID, SPICE_ID, URL, "
            + "IMAGE_URL, TITLE, LATIN_TITLE, DESCRIPTION " + " FROM "
            + GlobalStrings.SPICES_TABLE_NAME;

    private static final String SPICES_PAGE_SQL = SPICES_SELECT_CLAUSE + " %s LIMIT %d,10";

    private static final String SPICES_SQL = SPICES_SELECT_CLAUSE + " WHERE ID = %d";

    private static final String SPICES_SEARCH_SQL = "PRAGMA case_sensitive_like = false; "
            + SPICES_SELECT_CLAUSE + " WHERE TITLE like '%s'";

    public SpicesRepository(SQLiteDatabase db) {
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
        String sql = String.format(SPICES_PAGE_SQL, order, start);
        return SQLHelper.ExecuteSql(sql, db, new SpiceSmallViewConverter());
    }

    public void close() {
        maxIdStatement.close();
    }

    public UsefulView getSpiceView(long id) {
        UsefulView view = new UsefulView();
        String sql = String.format(SPICES_SQL, id);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            view.setId(id);
            view.setTitle(cursor.getString(4) + "\n" + cursor.getString(5));
            view.setDescription(cursor.getString(6));
            view.setImageUrl(cursor.getString(3));
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();

        return view;
    }

    public ArrayList<SmallPreview> searchSpices(String query) {
        String value = "%" + query + "%";
        String sql = String.format(SPICES_SEARCH_SQL, value);
        return SQLHelper.ExecuteSql(sql, db, new SpiceSmallViewConverter());
    }

}
