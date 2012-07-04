package com.vmladenov.cook.core.converters;

import android.database.Cursor;

import com.vmladenov.cook.domain.Recipe;

public class RecipeViewConverter implements ICursorConverter<Recipe> {
	@Override
	public Recipe Convert(Cursor cursor) {
		Recipe recipe = new Recipe();
		recipe.setId(cursor.getInt(0)); // ID
		recipe.setThumbnailUrl(cursor.getString(1));
		recipe.setImageUrl(cursor.getString(2));
		recipe.setTitle(cursor.getString(3));
		recipe.setProducts(cursor.getString(4));
		recipe.setDescription(cursor.getString(5));
		recipe.setIsFavorite(cursor.getInt(6) > 0);
		recipe.setUserNotes(cursor.getString(7));
		return recipe;
	}
}
