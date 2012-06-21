package com.vmladenov.cook.core.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.vmladenov.cook.core.Helpers;

import java.io.*;

public class DataHelper {
    private static final String DATABASE_NAME = "cook.db";
    private static final String SETTINGS = "settings";
    private static final String VERSION = "version";
    private SQLiteDatabase db;

    private DatabaseHelper helper;
    private SQLiteDatabase userDb;

    public RecipesRepository RecipesRepository;
    public SpicesRepository SpicesRepository;
    public ProductsRepository ProductsRepository;
    public DictionaryRepository DictionaryRepository;
    public AdvicesRepository AdvicesRepository;
    public ShoppingListsRepository ShoppingListsRepository;

    public DataHelper(Context context) {
        this.helper = new DatabaseHelper(context);
        this.userDb = helper.getWritableDatabase();
        String path = getDbPath(context, false);
        this.db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        RecipesRepository = new RecipesRepository(db);
        SpicesRepository = new SpicesRepository(db);
        ProductsRepository = new ProductsRepository(db);
        DictionaryRepository = new DictionaryRepository(db);
        AdvicesRepository = new AdvicesRepository(db);
        ShoppingListsRepository = new ShoppingListsRepository(userDb);
    }

    private String loadDbPath(Context context) {
        Boolean recreateDb = false;
        try {
            SharedPreferences preferences = context.getSharedPreferences(SETTINGS,
                    Context.MODE_PRIVATE);
            int version = preferences.getInt(VERSION, 0);
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            recreateDb = version == 0 || packageInfo.versionCode > version;
        } catch (Exception e) {
        }
        return getDbPath(context, recreateDb);
    }

    private String getDbPath(Context context, Boolean forceRecreate) {
        String state = Environment.getExternalStorageState();
        // If there is a storage card copy to it , if there is not copy to
        // database directory
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            String dbPath = context.getExternalFilesDir(null) + "/" + DATABASE_NAME;
            File file = new File(dbPath);
            if (!file.exists()) {
                try {
                    createDatabase(context, new FileOutputStream(dbPath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return context.getExternalFilesDir(null) + "/" + DATABASE_NAME;
        } else {
            String filePath = context.getFilesDir() + "/" + DATABASE_NAME;
            File file = new File(filePath);
            if (!file.exists()) {
                try {
                    createDatabase(context,
                            context.openFileOutput(DATABASE_NAME, Context.MODE_PRIVATE));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return file.getAbsolutePath();
        }
    }

    private void createDatabase(Context context, OutputStream out) {
        try {
            Helpers.copyDatabase(context, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.RecipesRepository.close();
        this.SpicesRepository.close();
        this.ProductsRepository.close();
        this.DictionaryRepository.close();
        this.AdvicesRepository.close();
        this.ShoppingListsRepository.close();
        if (db.isOpen()) db.close();
        if (userDb.isOpen()) userDb.close();
        helper.close();
    }

}
