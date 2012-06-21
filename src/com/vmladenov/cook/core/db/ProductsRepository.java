package com.vmladenov.cook.core.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.converters.ProductSmallViewConverter;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.objects.UsefulView;

import java.util.ArrayList;

public class ProductsRepository implements ISmallPreviewListRepository {
    private final SQLiteDatabase db;

    SQLiteStatement maxIdStatement;

    private static final String MAX_ID_SQL = "SELECT MAX(ID) FROM "
            + GlobalStrings.PRODUCTS_TABLE_NAME;

    private static final String PRODUCTS_SELECT_CLAUSE = "SELECT ID, PRODUCT_ID, URL, "
            + "IMAGE_URL, TITLE, DESCRIPTION " + " FROM " + GlobalStrings.PRODUCTS_TABLE_NAME;

    private static final String PRODUCTS_PAGE_SQL = PRODUCTS_SELECT_CLAUSE + " %s LIMIT %d,10";

    private static final String PRODUCT_SQL = PRODUCTS_SELECT_CLAUSE + " WHERE ID = %d";

    private static final String PRODUCT_SPICE_SEARCH_SQL = "select rowid _id,  title from ( "
            + "select rowid, title from PRODUCTS where title like '%s' " + " union all "
            + "select rowid, title from SPICES where title like '%s')";

    public ProductsRepository(SQLiteDatabase db) {
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

        String sql = String.format(PRODUCTS_PAGE_SQL, order, start);
        return SQLHelper.ExecuteSql(sql, db, new ProductSmallViewConverter());
    }

    public void close() {
        maxIdStatement.close();
    }

    public UsefulView getProductView(long id) {
        UsefulView view = new UsefulView();
        String sql = String.format(PRODUCT_SQL, id);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            view.setId(id);
            view.setTitle(cursor.getString(4));
            view.setDescription(cursor.getString(5));
            view.setImageUrl(cursor.getString(3));
            // try
            // {
            // String imgUrl = cursor.getString(3);
            // if (imgUrl != null)
            // {
            // String imageName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1);
            // view.setBigImage(Helpers.getSmallProductsImage(context,
            // imageName));
            // }
            // }
            // catch (IOException e)
            // {
            // }

            // String imgUrl = cursor.getString(3);
            // if (imgUrl != null)
            // {
            // view.setBigImage(Helpers.getImage(imgUrl));
            // }
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();

        return view;
    }

    public Cursor getProductsAndSpices(CharSequence constraint) {
        String search = constraint + "%";
        String sql = String.format(PRODUCT_SPICE_SEARCH_SQL, search, search);
        return db.rawQuery(sql, null);
    }
}
