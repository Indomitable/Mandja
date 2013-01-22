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

import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.converters.PreviewListItemConverter;
import com.vmladenov.cook.domain.PreviewListItem;

import java.util.ArrayList;

public class SpicesRepository extends BaseRepository {

	private static final String SPICES_SELECT_CLAUSE = "SELECT ID, SPICE_ID, URL, "
			+ "IMAGE_URL, TITLE, LATIN_TITLE, DESCRIPTION "
			+ " FROM "
			+ GlobalStrings.SPICES_TABLE_NAME;

	private static final String SPICES_SEARCH_SQL = "PRAGMA case_sensitive_like = false; "
			+ SPICES_SELECT_CLAUSE + " WHERE TITLE like '%s'";

	public SpicesRepository(String dbPath) {
		super(dbPath);
	}

	@Override
	protected String getTableName() {
		return GlobalStrings.SPICES_TABLE_NAME;
	}

    public ArrayList<PreviewListItem> searchSpices(String query) {
		String value = "%" + query + "%";
		String sql = String.format(SPICES_SEARCH_SQL, value);
		return SQLHelper
				.ExecuteSql(sql, dbPath, new PreviewListItemConverter());
	}

}
