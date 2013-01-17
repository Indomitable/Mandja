package com.vmladenov.cook.core.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.vmladenov.cook.core.BooleanValue;
import com.vmladenov.cook.core.CopyDatabase;

public class DataHelper {
	private static final String DATABASE_NAME = "cook.db";
	private static final int DBVersion = 2;

	private String dbPath;
	private String userDbPath;

	public void checkDb(Context context) {
		DatabaseHelper helper = new DatabaseHelper(context);
		SQLiteDatabase userDb = helper.getWritableDatabase();
		BooleanValue hasBeenCreated = new BooleanValue(false);
		String dbPath = getDbPath(context, false, hasBeenCreated);
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		if (!hasBeenCreated.getValue() && !checkDbVersion(dbPath)) {
			dbPath = getDbPath(context, true, hasBeenCreated);
			db = SQLiteDatabase.openDatabase(dbPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		}
		db.close();
		this.dbPath = dbPath;
		this.userDbPath = userDb.getPath();
		userDb.close();
	}

	private String getDbPath(Context context, Boolean forceRecreate,
			BooleanValue hasBeenCreated) {
		String state = Environment.getExternalStorageState();
		// If there is a storage card copy to it , if there is not copy to
		// database directory
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			String dbPath = context.getExternalFilesDir(null) + "/"
					+ DATABASE_NAME;
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
					createDatabase(context, context.openFileOutput(
							DATABASE_NAME, Context.MODE_PRIVATE));
					hasBeenCreated = new BooleanValue(true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
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

	private void createDatabase(Context context, OutputStream out) {
		try {
			// ProgressDialog progressDialog = ProgressDialog.show(context,
			// context.getString(com.vmladenov.cook.R.string.loading),
			// context.getString(com.vmladenov.cook.R.string.initialize));
			CopyDatabase copy = new CopyDatabase(context);
			copy.execute(out);
			copy.get();
			// progressDialog.dismiss();
			// Helpers.copyDatabase(context, out);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
