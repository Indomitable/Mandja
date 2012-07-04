package com.vmladenov.cook.core.loaders;

import java.util.ArrayList;

import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.domain.PreviewListItem;

public class FavoriteRecipesLoader extends ISmallPreviewLoader {
    @Override
    public Boolean hasMore() {
        return false;
    }

    @Override
    public ArrayList<PreviewListItem> getNextData() {
        return Helpers.getDataHelper().RecipesRepository.getRecipesInFavorites();
    }
}
