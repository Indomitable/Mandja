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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.vmladenov.cook.core.BooleanValue;
import com.vmladenov.cook.core.CopyDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

public class DataHelper {
	private static final String DATABASE_NAME = "cook.db";
	private static final int DBVersion = 4;

	private String dbPath;
	private String userDbPath;

    public void initUserDb(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase userDb = helper.getWritableDatabase();
        this.userDbPath = userDb.getPath();
        userDb.close();
    }

    public boolean checkDb(Context context){
        this.dbPath = getDbPath(context);
        if (dbPath.equals(""))
            return false;
        return checkDbVersion(dbPath);
    }

    private String getDbPath(Context context) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String dbPath = context.getExternalFilesDir(null) + "/" + DATABASE_NAME;
            File file = new File(dbPath);
            if (!file.exists())
                return "";
            return dbPath;
        } else {
            String filePath = context.getFilesDir() + "/" + DATABASE_NAME;
            File file = new File(filePath);
            if (!file.exists())
                return "";
            return file.getAbsolutePath();
        }
    }

	public String getDbPathForCopy(Context context) {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return context.getExternalFilesDir(null) + "/" + DATABASE_NAME;
		} else {
			String filePath = context.getFilesDir() + "/" + DATABASE_NAME;
			File file = new File(filePath);
			return file.getAbsolutePath();
		}
	}

	private Boolean checkDbVersion(String dbPath) {
		try {
			String sql = "SELECT VERSION FROM DB_VERSION";
			Integer version = SQLHelper.ExecuteScalarIntSql(sql, dbPath);
			return version == DBVersion;
		} catch (Exception e) {
			return false;
		}
	}

	public RecipesRepository getRecipesRepository() {
		return new RecipesRepository(dbPath);
	}

	public SpicesRepository getSpicesRepository() {
		return new SpicesRepository(dbPath);
	}

	public ProductsRepository getProductsRepository() {
		return new ProductsRepository(dbPath);
	}

	public AdvicesRepository getAdvicesRepository() {
		return new AdvicesRepository(dbPath);
	}

	public ShoppingListsRepository getShoppingListsRepository() {
		return new ShoppingListsRepository(userDbPath);
	}

	public String getDbPath() {
		return dbPath;
	}
}
