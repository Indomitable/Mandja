package com.vmladenov.cook.core.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.converters.AdviceSmallViewConverter;
import com.vmladenov.cook.core.converters.SimpleViewConverter;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.objects.SimpleView;
import com.vmladenov.cook.core.objects.UsefulView;

import java.util.ArrayList;

public class AdvicesRepository implements ISmallPreviewListRepository {
    private final SQLiteDatabase db;

    SQLiteStatement maxIdStatement;

    private static final String MAX_ID_SQL = "SELECT COUNT(ID) FROM "
            + GlobalStrings.ADVICE_TABLE_NAME + " WHERE CATEGORY_ID = %d ";

    private static final String ADVICE_SELECT_CLAUSE = "SELECT ID, ADVICE_ID, URL, "
            + "IMAGE_URL, TITLE, DESCRIPTION " + " FROM " + GlobalStrings.ADVICE_TABLE_NAME;

    private static final String ADVICE_PAGE_SQL = ADVICE_SELECT_CLAUSE
            + "  WHERE CATEGORY_ID = %d %s";

    private static final String ADVICE_SQL = ADVICE_SELECT_CLAUSE + "  WHERE ID = %d ";

    private static final String ADVICE_CATEGORIES_SQL = "SELECT ID, TITLE FROM ADVICE_CATEGORIES";
    private long categoryId;

    public AdvicesRepository(SQLiteDatabase db) {
        this.db = db;

    }

    @Override
    public long getMaxId() {
        String sql = String.format(MAX_ID_SQL, categoryId);
        maxIdStatement = db.compileStatement(sql);
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

        String sql = String.format(ADVICE_PAGE_SQL, categoryId, order);
        return SQLHelper.ExecuteSql(sql, db, new AdviceSmallViewConverter());
    }

    public ArrayList<SimpleView> getCategories() {
        return SQLHelper.ExecuteSql(ADVICE_CATEGORIES_SQL, db, new SimpleViewConverter());
    }

    public void close() {
        if (maxIdStatement != null) maxIdStatement.close();
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public UsefulView getAdviceView(long id) {
        UsefulView view = new UsefulView();
        String sql = String.format(ADVICE_SQL, id);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            view.setId(id);
            view.setTitle(cursor.getString(4));
            view.setDescription(cursor.getString(5));
            view.setImageUrl(cursor.getString(3));
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();

        return view;
    }

}
