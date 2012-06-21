package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.html.SmallPreview;

import java.util.ArrayList;

public class FavoriteRecipesLoader extends ISmallPreviewLoader {
    @Override
    public Boolean hasMore() {
        return false;
    }

    @Override
    public ArrayList<SmallPreview> getNextData() {
        return Helpers.getDataHelper().RecipesRepository.getRecipesInFavorites();
    }
}
