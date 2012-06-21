package com.vmladenov.cook.core.converters;

import android.database.Cursor;
import com.vmladenov.cook.core.html.Recipe;

public class RecipeViewConverter implements ICursorConverter<Recipe> {
    @Override
    public Recipe Convert(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getLong(0)); // ID
        recipe.setRecipeId(cursor.getLong(1));
        recipe.setUrl(cursor.getString(2));
        recipe.setImageUrl(cursor.getString(3));
        recipe.setTitle(cursor.getString(4));
        recipe.setProducts(cursor.getString(5));
        recipe.setNotes(cursor.getString(7)); // Mistake in database
        recipe.setDescription(cursor.getString(6));
        recipe.setDate(cursor.getString(8));
        recipe.setIsFavorite(!cursor.isNull(9) && cursor.getInt(9) != 0);
        recipe.setUserNotes(cursor.getString(10));
        return recipe;
    }

}
