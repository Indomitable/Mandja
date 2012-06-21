package com.vmladenov.cook.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cook_personal.db";

    private static String getDbName(Context context) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return context.getExternalFilesDir(null) + "/" + DATABASE_NAME;
        }
        return context.getFilesDir() + "/" + DATABASE_NAME;
    }

    public DatabaseHelper(Context context) {
        super(context, getDbName(context), null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String recipesTableSql = "CREATE TABLE MY_RECIPES ( ID INTEGER PRIMARY KEY,"
                + " IMAGE_NAME TEXT, TITLE TEXT, PRODUCTS TEXT, DESCRIPTION TEXT,"
                + " NOTES TEXT ); ";

        String shoppingListsMainTableSql = "CREATE TABLE SHOPPING_LISTS ( ID INTEGER PRIMARY KEY,"
                + " TITLE TEXT, CREATION_DATE DATETIME); ";

        String shoppingListsDetailTableSql = "CREATE TABLE SHOPPING_LIST_ITEMS ( ID INTEGER PRIMARY KEY,"
                + " LIST_ID INTEGER," + " TITLE TEXT, ORDER_NUMBER INTEGER, IS_CHECKED INTEGER); ";

        String recipeFavoritesTableSql = "CREATE TABLE MY_FAVORITE_RECIPES ( RECIPE_ID INTEGER PRIMARY KEY ); ";
        String recipeUserNotesTableSql = "CREATE TABLE MY_RECIPE_NOTES ( RECIPE_ID INTEGER PRIMARY KEY, USER_NOTE TEXT ); ";

        db.execSQL(recipesTableSql);
        db.execSQL(shoppingListsMainTableSql);
        db.execSQL(shoppingListsDetailTableSql);
        db.execSQL(recipeFavoritesTableSql);
        db.execSQL(recipeUserNotesTableSql);
    }
}
