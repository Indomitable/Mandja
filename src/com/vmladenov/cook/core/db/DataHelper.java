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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
        if (dbPath.isEmpty())
            return false;
        return checkDbVersion(dbPath);
    }

    private  static boolean tryCreateFile(String path) {
        try {
            File file = new File(path + "/dummy");
            if (file.exists())
                file.delete();
            OutputStream out = new FileOutputStream(file);
            out.write(45);
            out.write(54);
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public enum StorageState {
        ExternalStorage,
        InternalStorage,
        None
    }

    private static boolean checkExternalStorage(Context context){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File storage = context.getExternalFilesDir(null);
            if (storage != null && storage.getFreeSpace() > 11 * 1024 * 1024) {
                if (tryCreateFile(storage.getAbsolutePath())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkInternalStorage(Context context) {
        File storage = context.getFilesDir();
        if (storage != null && storage.getFreeSpace() > 11 * 1024 * 1024) {
            if (tryCreateFile(storage.getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }

    public static StorageState getStorageState(Context context) {
        if (!checkExternalStorage(context)) {
            if (!checkInternalStorage(context)) {
                return StorageState.None;
            } else {
                return StorageState.InternalStorage;
            }
        } else {
            return StorageState.ExternalStorage;
        }
    }

    private String getDbPath(Context context) {
        StorageState storageState = getStorageState(context);
        String path = "";
        switch (storageState)
        {
            case ExternalStorage:
                path = context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME;
                break;
            case InternalStorage:
                path = context.getFilesDir().getAbsolutePath() + "/" + DATABASE_NAME;
                break;
        }
        if (path.isEmpty())
            return path;
        File file = new File(path);
        if (file.exists())
            return file.getAbsolutePath();
        else
            return "";
    }

	public String getDbPathForCopy(Context context) {
        StorageState storageState = getStorageState(context);
        switch (storageState)
        {
            case ExternalStorage:
                return context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME;
            case InternalStorage:
                return context.getFilesDir().getAbsolutePath() + "/" + DATABASE_NAME;
            default:
                return "";
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
