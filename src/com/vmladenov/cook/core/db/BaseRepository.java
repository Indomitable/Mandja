/*
 * Copyright (C) 2011 Ventsislav Mladenov <ventsislav dot mladenov at gmail dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.vmladenov.cook.core.db;

import android.database.Cursor;
import com.vmladenov.cook.core.converters.ICursorConverter;
import com.vmladenov.cook.core.converters.PreviewListItemConverter;
import com.vmladenov.cook.domain.BigViewItem;
import com.vmladenov.cook.domain.PreviewListItem;

import java.util.ArrayList;

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
		String order;
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
