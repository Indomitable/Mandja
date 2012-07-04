package com.vmladenov.cook.core.loaders;

import java.util.ArrayList;

import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.domain.PreviewListItem;

public class RecipesByCategoryLoader extends ISmallPreviewLoader {
	private final int categoryId;
	private final int type;
	private final ArrayList<String> products;

	public RecipesByCategoryLoader(int categoryId, short type, ArrayList<String> products) {
		this.categoryId = categoryId;
		this.type = type;
		this.products = products;
	}

	@Override
	public Boolean hasMore() {
		return false;
	}

	@Override
	public ArrayList<PreviewListItem> getNextData() {
		switch (type) {
		case 1:
			return Helpers.getDataHelper().RecipesRepository.getRecipesByCategory(categoryId);
		case 2:
			return Helpers.getDataHelper().RecipesRepository.getRecipesByProducts(products);
		default:
			return null;
		}

	}
}
