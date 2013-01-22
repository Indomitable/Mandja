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
import com.vmladenov.cook.core.converters.SimpleViewConverter;
import com.vmladenov.cook.core.objects.SimpleView;

import java.util.ArrayList;

public class AdvicesRepository extends BaseRepository {

	private static final String ADVICE_CATEGORIES_SQL = "SELECT ID, TITLE FROM ADVICE_CATEGORIES";

	public AdvicesRepository(String dbPath) {
		super(dbPath);
	}

	@Override
	protected String getTableName() {
		return GlobalStrings.ADVICE_TABLE_NAME;
	}

	public ArrayList<SimpleView> getCategories() {
		return SQLHelper.ExecuteSql(ADVICE_CATEGORIES_SQL, dbPath,
				new SimpleViewConverter());
	}

}
