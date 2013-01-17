package com.vmladenov.cook.core.db;

import java.util.ArrayList;

import android.database.Cursor;

import com.vmladenov.cook.core.converters.ICursorConverter;
import com.vmladenov.cook.core.converters.PreviewListItemConverter;
import com.vmladenov.cook.domain.BigViewItem;
import com.vmladenov.cook.domain.PreviewListItem;

public abstract class BaseRepository {

	private static final String PREVIEWS_PAGE_SQL = "SELECT ID, THUMBNAIL_URL, TITLE FROM %s %s LIMIT %d,10";

	private static final String BIG_VIEW_SQL = "SELECT ID, IMAGE_URL, TITLE, DESCRIPTION FROM %s WHERE ID = %d";

	protected String dbPath;

	public BaseRepository(String dbPath) {
		this.dbPath = dbPath;
	}

	protected abstract String getTableName();

	public Integer getMaxId() {
		String sql = "SELECT MAX(ID) FROM " + getTableName();
		return SQLHelper.ExecuteScalarIntSql(sql, dbPath);
	}

	public ArrayList<PreviewListItem> getPreviews(long start, int sorting) {
		String order = "";
		switch (sorting) {
		case 1:
			order = " ORDER BY TITLE ";
			break;
		default:
			order = "";
			break;
		}
		String sql = String.format(PREVIEWS_PAGE_SQL, getTableName(), order,
				start);
		return SQLHelper
				.ExecuteSql(sql, dbPath, new PreviewListItemConverter());
	}

	public BigViewItem getBigViewItem(int id) {
		String sql = String.format(BIG_VIEW_SQL, getTableName(), id);
		return SQLHelper.ExecuteSingleSql(sql, dbPath,
				new ICursorConverter<BigViewItem>() {
					@Override
					public BigViewItem Convert(Cursor cursor) {
						BigViewItem view = new BigViewItem();
						view.setId(cursor.getInt(0));
						view.setImageUrl(cursor.getString(1));
						view.setTitle(cursor.getString(2));
						view.setDescription(cursor.getString(3));
						return view;
					}
				});
	}
}
