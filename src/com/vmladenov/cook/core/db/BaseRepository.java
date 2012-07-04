package com.vmladenov.cook.core.db;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vmladenov.cook.core.converters.PreviewListItemConverter;
import com.vmladenov.cook.domain.BigViewItem;
import com.vmladenov.cook.domain.PreviewListItem;

public abstract class BaseRepository {
    
	private static final String PREVIEWS_PAGE_SQL = "SELECT ID, THUMBNAIL_URL, TITLE FROM %s %s LIMIT %d,10";
	
	private static final String BIG_VIEW_SQL = "SELECT ID, IMAGE_URL, TITLE, DESCRIPTION FROM %s WHERE ID = %d";
	
	protected final SQLiteDatabase db;
	
	public BaseRepository(SQLiteDatabase db) {
		this.db = db;
	}
	
	protected abstract String getTableName();
	
	public Integer getMaxId()
	{
		String sql = "SELECT MAX(ID) FROM " + getTableName();
		return SQLHelper.ExecuteScalarIntSql(sql, db);
	}
	
    public ArrayList<PreviewListItem> getPreviews(long start, int sorting)
    {
        String order = "";
        switch (sorting) {
            case 1:
                order = " ORDER BY TITLE ";
                break;
            default:
                order = "";
                break;
        }
        String sql = String.format(PREVIEWS_PAGE_SQL, getTableName(), order, start);
        return SQLHelper.ExecuteSql(sql, db, new PreviewListItemConverter());
    }
    
    public BigViewItem getBigViewItem(int id) {
        BigViewItem view = new BigViewItem();
        String sql = String.format(BIG_VIEW_SQL, getTableName(), id);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            view.setId(id);
            view.setImageUrl(cursor.getString(1));
            view.setTitle(cursor.getString(2));
            view.setDescription(cursor.getString(3));
        }
        if (cursor != null && !cursor.isClosed()) cursor.close();
        return view;
    }
}
