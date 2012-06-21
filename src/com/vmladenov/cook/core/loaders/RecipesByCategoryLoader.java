package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.html.SmallPreview;

import java.util.ArrayList;

public class RecipesByCategoryLoader extends ISmallPreviewLoader {
    private final long categoryId;
    private final int type;
    private final ArrayList<String> products;

    public RecipesByCategoryLoader(long categoryId, int type, ArrayList<String> products) {
        this.categoryId = categoryId;
        this.type = type;
        this.products = products;
    }

    @Override
    public Boolean hasMore() {
        return false;
    }

    @Override
    public ArrayList<SmallPreview> getNextData() {
        switch (type) {
            case 1:
                return Helpers.getDataHelper().RecipesRepository
                        .getRecipesInSubCategory(categoryId);
            case 2:
                return Helpers.getDataHelper().RecipesRepository
                        .getRecipesInTenCategory(categoryId);
            case 3:
                return Helpers.getDataHelper().RecipesRepository.getRecipesByProducts(products);
            default:
                return null;
        }

    }
}
