package com.vmladenov.cook.core.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.vmladenov.cook.core.BooleanValue;
import com.vmladenov.cook.core.Helpers;

public class DataHelper {
	private static final String DATABASE_NAME = "cook.db";
	private static final int DBVersion = 2;

	private SQLiteDatabase db;
	private SQLiteDatabase userDb;

	public void checkDb(Context context)
	{
		DatabaseHelper helper = new DatabaseHelper(context);
		userDb = helper.getWritableDatabase();

		BooleanValue hasBeenCreated = new BooleanValue(false);
		String dbPath = getDbPath(context, false, hasBeenCreated);
		db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		if (!hasBeenCreated.getValue() && !checkDbVersion(db)) {
			dbPath = getDbPath(context, true, hasBeenCreated);
			db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		}
	}

	private String getDbPath(Context context, Boolean forceRecreate, BooleanValue hasBeenCreated) {
		String state = Environment.getExternalStorageState();
		// If there is a storage card copy to it , if there is not copy to
		// database directory
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			String dbPath = context.getExternalFilesDir(null) + "/" + DATABASE_NAME;
			File file = new File(dbPath);
			if (forceRecreate || !file.exists()) {
				try {
					createDatabase(context, new FileOutputStream(dbPath));
					hasBeenCreated = new BooleanValue(true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			return context.getExternalFilesDir(null) + "/" + DATABASE_NAME;
		} else {
			String filePath = context.getFilesDir() + "/" + DATABASE_NAME;
			File file = new File(filePath);
			if (forceRecreate || !file.exists()) {
				try {
					createDatabase(context, context.openFileOutput(DATABASE_NAME, Context.MODE_PRIVATE));
					hasBeenCreated = new BooleanValue(true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			return file.getAbsolutePath();
		}
	}

	private Boolean checkDbVersion(SQLiteDatabase db) {
		try {
			String sql = "SELECT VERSION FROM DB_VERSION";
			Integer version = SQLHelper.ExecuteScalarIntSql(sql, db);
			return version == DBVersion;
		} catch (Exception e) {
			return false;
		}
	}

	private void createDatabase(Context context, OutputStream out) {
		try {
			Helpers.copyDatabase(context, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RecipesRepository getRecipesRepository() {
		return new RecipesRepository(db);
	}

	public SpicesRepository getSpicesRepository() {
		return new SpicesRepository(db);
	}

	public ProductsRepository getProductsRepository() {
		return new ProductsRepository(db);
	}

	public AdvicesRepository getAdvicesRepository() {
		return new AdvicesRepository(db);
	}

	public DictionaryRepository getDictionaryRepository() {
		return new DictionaryRepository(db);
	}

	public ShoppingListsRepository getShoppingListsRepository() {
		return new ShoppingListsRepository(userDb);
	}

	public void close()
	{
		if (userDb.isOpen())
			userDb.close();

		if (db.isOpen())
			db.close();
	}
}
