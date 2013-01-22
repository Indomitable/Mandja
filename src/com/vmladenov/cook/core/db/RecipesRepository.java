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
import com.vmladenov.cook.core.converters.RecipeViewConverter;
import com.vmladenov.cook.core.converters.SimpleViewConverter;
import com.vmladenov.cook.core.objects.SimpleView;
import com.vmladenov.cook.domain.PreviewListItem;
import com.vmladenov.cook.domain.Recipe;

import java.util.ArrayList;
import java.util.Random;

public class RecipesRepository extends BaseRepository {

	public RecipesRepository(String dbPath) {
		super(dbPath);
	}

	private static final String RECIPES_SEARCH_SQL = "SELECT r.ID, r.THUMBNAIL_URL, r.TITLE FROM RECIPES r WHERE TITLE like '%s' or PRODUCTS like '%s' ";

	private static final String RECIPES_CATEGORIES_SQL = "SELECT ID, TITLE FROM CATEGORIES WHERE PARENT_ID = ?";

	private static final String CHECK_FOR_CHILD_CATEGORIES_SQL = "SELECT COUNT(1) FROM CATEGORIES WHERE PARENT_ID = ?";

	private static final String RECIPES_BY_CATEGORY_SQL = "SELECT r.ID, r.THUMBNAIL_URL, r.TITLE FROM RECIPES r, RECIPE_CATEGORY rc where r.ID = rc.RECIPE_ID and rc.CATEGORY_ID = ?";

	private static final String RECIPE_SELECT_ID_SQL = "SELECT r.ID, r.THUMBNAIL_URL, r.IMAGE_URL, r.TITLE, r.PRODUCTS, r.DESCRIPTION"
			+ ", IFNULL(f.RECIPE_ID, 0) IS_FAVORITE, IFNULL(n.NOTE, '') USER_NOTE FROM RECIPES r"
			+ " LEFT OUTER JOIN FAVORITE_RECIPES f on r.ID = f.RECIPE_ID"
			+ " LEFT OUTER JOIN RECIPE_NOTES n on r.ID = n.RECIPE_ID"
			+ " WHERE id = ?";

	private static final String RECIPES_IN_FAVORITES_SQL = "SELECT r.ID, r.THUMBNAIL_URL, r.TITLE FROM RECIPES r, FAVORITE_RECIPES f where r.ID = f.RECIPE_ID";

	private static final String SET_FAVORITE_SQL = "INSERT INTO FAVORITE_RECIPES (RECIPE_ID) VALUES (?)";
	private static final String UNSET_FAVORITE_SQL = "DELETE FROM FAVORITE_RECIPES WHERE RECIPE_ID = ?";

	private static final String DELETE_RECIPE_NOTE_SQL = "DELETE FROM RECIPE_NOTES WHERE RECIPE_ID = ?";
	private static final String INSERT_RECIPE_NOTE_SQL = "INSERT INTO RECIPE_NOTES (RECIPE_ID, NOTE) VALUES (?, ?)";

	private static final String GET_RECIPE_OF_THE_DAY_SQL = "SELECT RECIPE_ID FROM RECIPE_OF_THE_DAY WHERE DAY = date('now')";

	private static final String SET_RECIPE_OF_THE_DAY_SQL = "INSERT INTO RECIPE_OF_THE_DAY (DAY, RECIPE_ID) VALUES(date('now'), %d)";

	private static final String RECIPES_BY_PRODUCTS_SQL = "SELECT r.ID, r.THUMBNAIL_URL, r.TITLE FROM RECIPES R WHERE R.PRODUCTS like '%s'";

	@Override
	protected String getTableName() {
		return GlobalStrings.RECIPES_TABLE_NAME;
	}

	public ArrayList<PreviewListItem> searchRecipes(String query) {
		String like_query = "%" + query + "%";
		String sql = String.format(RECIPES_SEARCH_SQL, like_query, like_query);
		return SQLHelper
				.ExecuteSql(sql, dbPath, new PreviewListItemConverter());
	}

	public Recipe getRecipe(int id) {
		return SQLHelper.ExecuteSingleSql(RECIPE_SELECT_ID_SQL, dbPath,
				new RecipeViewConverter(), Integer.toString(id));
	}

	public ArrayList<SimpleView> getRecipeCategories(int parentId) {
		return SQLHelper.ExecuteSql(RECIPES_CATEGORIES_SQL, dbPath,
				new SimpleViewConverter(), Integer.toString(parentId));
	}

	public Boolean checkForChildCategories(int parentId) {
		return SQLHelper.ExecuteScalarIntSql(CHECK_FOR_CHILD_CATEGORIES_SQL,
				dbPath, Integer.toString(parentId)) > 0;
	}

	public ArrayList<PreviewListItem> getRecipesInFavorites() {
		return SQLHelper.ExecuteSql(RECIPES_IN_FAVORITES_SQL, dbPath,
				new PreviewListItemConverter());
	}

	public ArrayList<PreviewListItem> getRecipesByCategory(int categoryId) {
		return SQLHelper.ExecuteSql(RECIPES_BY_CATEGORY_SQL, dbPath,
				new PreviewListItemConverter(), Integer.toString(categoryId));
	}

	public void addFavorite(int recipeId) {
		SQLHelper.ExecuteNonQuery(SET_FAVORITE_SQL, dbPath, recipeId);
	}

	public void removeFavorite(int recipeId) {
		SQLHelper.ExecuteNonQuery(UNSET_FAVORITE_SQL, dbPath, recipeId);
	}

	public void setRecipeNote(int recipeId, String note) {
		SQLHelper.ExecuteNonQuery(DELETE_RECIPE_NOTE_SQL, dbPath, recipeId);
		SQLHelper.ExecuteNonQuery(INSERT_RECIPE_NOTE_SQL, dbPath, recipeId,
				note);
	}

	public Recipe getRecipeOfTheDay() {
		Integer integer = SQLHelper.ExecuteScalarIntSql(
				GET_RECIPE_OF_THE_DAY_SQL, dbPath);
		int id;
		if (integer == null) {
			Random random = new Random();
			int maxId = (int) this.getMaxId();
			id = random.nextInt(maxId);
			String sql = String.format(SET_RECIPE_OF_THE_DAY_SQL, id);
			SQLHelper.ExecuteNonQuery(sql, dbPath);
		} else
			id = integer;
		return this.getRecipe(id);
	}

	public ArrayList<PreviewListItem> getRecipesByProducts(
			ArrayList<String> products) {
		StringBuilder sqlBuilder = new StringBuilder();
		for (String product : products) {
			String search_value = "%" + product + "%";
			String sql = String.format(RECIPES_BY_PRODUCTS_SQL, search_value);
			sqlBuilder.append(sql);
			sqlBuilder.append(" INTERSECT ");
		}
		String sql = sqlBuilder.substring(0, sqlBuilder.length() - 11);
		return SQLHelper
				.ExecuteSql(sql, dbPath, new PreviewListItemConverter());
	}

}
