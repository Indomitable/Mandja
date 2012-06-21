package com.vmladenov.cook.core.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.converters.RecipeSmallViewConverter;
import com.vmladenov.cook.core.converters.RecipeViewConverter;
import com.vmladenov.cook.core.converters.SimpleViewConverter;
import com.vmladenov.cook.core.html.Recipe;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.objects.SimpleView;

import java.util.ArrayList;
import java.util.Random;

public class RecipesRepository implements ISmallPreviewListRepository {
    private final SQLiteDatabase db;

    public RecipesRepository(SQLiteDatabase db) {
        this.db = db;
        insertStatement = db.compileStatement(INSERT_RECIPE_SQL);
        maxRecipeIdStatement = db.compileStatement(MAX_RECIPE_ID);
        maxSiteRecipeIdStatement = db.compileStatement(MAX_SITE_RECIPE_ID);
    }

    SQLiteStatement insertStatement;
    SQLiteStatement maxRecipeIdStatement;
    SQLiteStatement maxSiteRecipeIdStatement;

    private static final String INSERT_RECIPE_SQL = "insert into "
            + GlobalStrings.RECIPES_TABLE_NAME
            + " (ID, RECIPE_ID, URL, IMAGE_URL, TITLE, PRODUCTS, DESCRIPTION, NOTES, DATE) "
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String MAX_RECIPE_ID = "SELECT MAX(ID) FROM "
            + GlobalStrings.RECIPES_TABLE_NAME;

    private static final String MAX_SITE_RECIPE_ID = "SELECT MAX(RECIPE_ID) FROM "
            + GlobalStrings.RECIPES_TABLE_NAME;

    private static final String RECIPES_SELECT_CLAUSE = "SELECT r.ID, r.RECIPE_ID, r.URL, r.IMAGE_URL, r.TITLE, r.PRODUCTS, r.NOTES, r.DESCRIPTION, r.DATE "
            + " FROM RECIPES r ";

    private static final String RECIPES_PAGE_SQL = RECIPES_SELECT_CLAUSE + " %s LIMIT %d,10";

    private static final String RECIPES_SEARCH_SQL = RECIPES_SELECT_CLAUSE
            + " WHERE TITLE like '%s' or PRODUCTS like '%s' ";

    private static final String RECIPES_CATEGORIES_SQL = "SELECT ID, TITLE FROM RECIPE_CATEGORIES";
    private static final String RECIPES_SUBCATEGORIES_SQL = "SELECT ID, TITLE FROM RECIPE_SUBCATEGORIES WHERE CATEGORY_ID = %d";
    private static final String RECIPES_IN_SUB_CATEGORY_SQL = "SELECT r.ID, r.RECIPE_ID, r.URL, r.IMAGE_URL, r.TITLE, r.PRODUCTS, r.NOTES, r.DESCRIPTION, r.DATE "
            + " FROM RECIPES r, RECIPE_CATEGORY rc "
            + " WHERE r.ID = rc.RECIPE_ID AND "
            + " rc.SUBCATEGORY_ID = %d";

    private static final String RECIPE_SELECT_ID_SQL = ""
            + "SELECT r.ID, r.RECIPE_ID, r.URL, r.IMAGE_URL, r.TITLE, r.PRODUCTS, "
            + "		  r.NOTES, r.DESCRIPTION, r.DATE, r.IS_FAVORITE, r.USER_NOTES FROM RECIPES r "
            + "WHERE id = %d ";

    private static final String TEN_RECIPES_CATEGORIES_SQL = "SELECT ID, TITLE FROM TEN_RECIPES_CATEGORIES";
    private static final String RECIPES_IN_TEN_CATEGORY_SQL = "SELECT r.ID, r.RECIPE_ID, r.URL, r.IMAGE_URL, r.TITLE, r.PRODUCTS, r.NOTES, r.DESCRIPTION, r.DATE "
            + " FROM RECIPES r, RECIPES_IN_TEN_RECIPES_CATEGORY rc"
            + " WHERE r.ID = rc.RECIPE_ID AND rc.TEN_RECIPE_CATEGORY = %d";

    private static final String RECIPES_IN_FAVORITES_SQL = "SELECT r.ID, r.RECIPE_ID, r.URL, r.IMAGE_URL, r.TITLE, r.PRODUCTS, r.NOTES, r.DESCRIPTION, r.DATE "
            + " FROM RECIPES r WHERE IS_FAVORITE = 1";

    private static final String SET_FAVORITE_SQL = "UPDATE RECIPES SET IS_FAVORITE = 1 WHERE ID = %d";
    private static final String UNSET_FAVORITE_SQL = "UPDATE RECIPES SET IS_FAVORITE = 0 WHERE ID = %d";

    private static final String UPDATE_RECIPE_NOTE_SQL = "UPDATE RECIPES SET USER_NOTES = '%s' WHERE ID = %d";

    private static final String GET_RECIPE_OF_THE_DAY_SQL = "SELECT RECIPE_ID FROM RECIPE_OF_THE_DAY WHERE DAY = date('now')";

    private static final String SET_RECIPE_OF_THE_DAY_SQL = "INSERT INTO RECIPE_OF_THE_DAY (DAY, RECIPE_ID) VALUES(date('now'), %d)";

    private static final String RECIPES_BY_PRODUCTS_SQL = "SELECT r.ID, r.RECIPE_ID, r.URL, r.IMAGE_URL, r.TITLE, r.PRODUCTS, r.NOTES, r.DESCRIPTION, r.DATE "
            + " FROM RECIPES R WHERE R.PRODUCTS like '%s'";

    private static final String IMAGE_URL_FROM_SITE_ID = "SELECT r.IMAGE_URL FROM RECIPES r WHERE RECIPE_ID = %d ";
    private static final String LOCAL_ID_FROM_SITE_ID = "SELECT r.ID FROM RECIPES r WHERE RECIPE_ID = %d ";

    public void saveRecipe(Recipe recipe, long id) {
        insertStatement.clearBindings();
        insertStatement.bindLong(1, id);
        insertStatement.bindLong(2, recipe.getId());

        insertStatement.bindString(3, recipe.getUrl() == null ? "" : recipe.getUrl());
        insertStatement.bindString(4, recipe.getImageUrl() == null ? "" : recipe.getImageUrl());
        insertStatement.bindString(5, recipe.getTitle() == null ? "" : recipe.getTitle());
        insertStatement.bindString(6, recipe.getProducts() == null ? "" : recipe.getProducts());
        insertStatement.bindString(7, recipe.getNotes() == null ? "" : recipe.getNotes());
        insertStatement.bindString(8,
                recipe.getDescription() == null ? "" : recipe.getDescription());
        insertStatement.bindString(9, recipe.getDate() == null ? "" : recipe.getDate());
        insertStatement.executeInsert();
    }

    public long getMaxId() {
        return maxRecipeIdStatement.simpleQueryForLong();
    }

    public long getMaxRecipeId() {
        return maxSiteRecipeIdStatement.simpleQueryForLong();
    }

    private ArrayList<SmallPreview> getRecipePreviews(String sql) {
        return SQLHelper.ExecuteSql(sql, db, new RecipeSmallViewConverter());
    }

    public ArrayList<SmallPreview> getPreviews(long start, int sorting) {
        String order = "";
        switch (sorting) {
            case 1:
                order = " ORDER BY r.TITLE ";
                break;
            default:
                order = "";
                break;
        }
        String sql = String.format(RECIPES_PAGE_SQL, order, start);
        return getRecipePreviews(sql);
    }

    public ArrayList<SmallPreview> searchRecipes(String query) {
        String like_query = "%" + query + "%";
        String sql = String.format(RECIPES_SEARCH_SQL, like_query, like_query);
        return getRecipePreviews(sql);
    }

    public Recipe getRecipe(long id) {
        String sql = String.format(RECIPE_SELECT_ID_SQL, id);
        return SQLHelper.ExecuteSingleSql(sql, db, new RecipeViewConverter());
    }

    public ArrayList<SimpleView> getRecipeCategories() {
        return SQLHelper.ExecuteSql(RECIPES_CATEGORIES_SQL, db, new SimpleViewConverter());
    }

    public ArrayList<SimpleView> getRecipeSubCategories(long categoryId) {
        String sql = String.format(RECIPES_SUBCATEGORIES_SQL, categoryId);
        return SQLHelper.ExecuteSql(sql, db, new SimpleViewConverter());
    }

    public ArrayList<SmallPreview> getRecipesInSubCategory(long subCategoryId) {
        String sql = String.format(RECIPES_IN_SUB_CATEGORY_SQL, subCategoryId);
        return SQLHelper.ExecuteSql(sql, db, new RecipeSmallViewConverter());
    }

    public ArrayList<SimpleView> getRecipeTenCategories() {
        return SQLHelper.ExecuteSql(TEN_RECIPES_CATEGORIES_SQL, db, new SimpleViewConverter());
    }

    public ArrayList<SmallPreview> getRecipesInTenCategory(long categoryId) {
        String sql = String.format(RECIPES_IN_TEN_CATEGORY_SQL, categoryId);
        return SQLHelper.ExecuteSql(sql, db, new RecipeSmallViewConverter());
    }

    public ArrayList<SmallPreview> getRecipesInFavorites() {
        return SQLHelper.ExecuteSql(RECIPES_IN_FAVORITES_SQL, db, new RecipeSmallViewConverter());
    }

    // public Boolean isFavorite(long recipeId)
    // {
    // String sql = String.format(FAVORITE_SQL, recipeId);
    // Integer result = SQLHelper.ExecuteScalarIntSql(sql, db);
    // return result != null && result > 0;
    // }

    public void addFavorite(long recipeId) {
        String sql = String.format(SET_FAVORITE_SQL, recipeId);
        SQLHelper.ExecuteNonQuery(sql, db);
    }

    public void removeFavorite(long recipeId) {
        String sql = String.format(UNSET_FAVORITE_SQL, recipeId);
        SQLHelper.ExecuteNonQuery(sql, db);
    }

    // public String getRecipeNote(long recipeId)
    // {
    // String sql = String.format(RECIPE_NOTE_SQL, recipeId);
    // String result = SQLHelper.ExecuteScalarStringSql(sql, db);
    // return result == null ? "" : result;
    // }

    public void setRecipeNote(long recipeId, String note) {
        // String sql = String.format(DELETE_RECIPE_NOTE_SQL, recipeId);
        // SQLHelper.ExecuteNonQuery(sql, db);
        String sql = String.format(UPDATE_RECIPE_NOTE_SQL, note, recipeId);
        SQLHelper.ExecuteNonQuery(sql, db);
    }

    public Recipe getRecipeOfTheDay() {
        Integer integer = SQLHelper.ExecuteScalarIntSql(GET_RECIPE_OF_THE_DAY_SQL, db);
        long id = 0;
        if (integer == null) {
            Random random = new Random();
            int maxId = (int) this.getMaxId();
            id = random.nextInt(maxId);
            String sql = String.format(SET_RECIPE_OF_THE_DAY_SQL, id);
            SQLHelper.ExecuteNonQuery(sql, db);
        } else
            id = integer;
        return this.getRecipe(id);
    }

    public void close() {
        insertStatement.close();
        maxRecipeIdStatement.close();
        maxSiteRecipeIdStatement.close();
    }

    public ArrayList<SmallPreview> getRecipesByProducts(ArrayList<String> products) {
        StringBuilder sqlBuilder = new StringBuilder();
        for (String product : products) {
            String search_value = "%" + product + "%";
            String sql = String.format(RECIPES_BY_PRODUCTS_SQL, search_value);
            sqlBuilder.append(sql);
            sqlBuilder.append(" INTERSECT ");
        }

        String sql = sqlBuilder.substring(0, sqlBuilder.length() - 11);
        return SQLHelper.ExecuteSql(sql, db, new RecipeSmallViewConverter());
    }

    public String getImageUrlFromRecipeId(long id) {
        String sql = String.format(IMAGE_URL_FROM_SITE_ID, id);
        return SQLHelper.ExecuteScalarStringSql(sql, db);
    }

    public long getLocalIdFromRecipeId(long id) {
        String sql = String.format(LOCAL_ID_FROM_SITE_ID, id);
        return SQLHelper.ExecuteScalarLongSql(sql, db);
    }
}
