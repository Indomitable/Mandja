package com.vmladenov.cook.core.db;

import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.html.Recipe;
import com.vmladenov.cook.core.html.RecipeReader;
import com.vmladenov.cook.core.rss.RSSFeed;
import com.vmladenov.cook.core.rss.RSSItem;
import com.vmladenov.cook.core.rss.RSSReader;

public class AutoUpdate {
    public void start() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                doAutoUpdate();
            }
        }).start();
    }

    public void doAutoUpdate() {
        RSSReader reader = new RSSReader();
        RSSFeed feed = reader.getFeed(GlobalStrings.LatestRecipesFeed);
        if (feed == null) return;
        long maxId = 0;
        for (int i = 0; i < feed.size(); i++) {
            RSSItem item = feed.get(i);
            if (item.getId() > maxId) maxId = item.getId();
        }
        RecipesRepository repository = Helpers.getDataHelper().RecipesRepository;
        long maxCachedId = repository.getMaxId();
        long maxRecipeCachedId = repository.getMaxRecipeId();
        if (maxRecipeCachedId >= maxId) // Everything is cached;
            return;

        long id = maxCachedId;
        for (long i = maxRecipeCachedId + 1; i <= maxId; i++) {
            id++;
            String tempUrl = GlobalStrings.RecipeByIdUrl + i;
            RecipeReader recipeReader = new RecipeReader(tempUrl);
            Recipe recipe = recipeReader.getRecipe();
            if (recipe == null || recipe.getId() == 0) continue;
            repository.saveRecipe(recipe, id);
        }
    }
}
