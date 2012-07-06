package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.core.Helpers;

public class RecipesByIdLoader extends IdLoader {
	public RecipesByIdLoader() {
		super.setRepository(Helpers.getDataHelper().getRecipesRepository());
	}
}
