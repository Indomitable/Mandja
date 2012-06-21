package com.vmladenov.cook.core.loaders;

import android.util.Pair;
import com.vmladenov.cook.core.html.RecipeReader;
import com.vmladenov.cook.core.html.SmallPreview;

import java.util.ArrayList;

public class CategoryPagedRecipeLoader extends PagedLoader {

    private final String _categoryUrl;

    public CategoryPagedRecipeLoader(String categoryUrl) {
        this._categoryUrl = categoryUrl;
        // TODO Auto-generated constructor stub
    }

    @Override
    public Pair<ArrayList<SmallPreview>, Integer> getPagedData() {
        RecipeReader reader = new RecipeReader();
        String baseUrl = _categoryUrl + "index.php?pageID=";
        return reader.getPagedReciples(baseUrl, this.getPage());
    }

}
