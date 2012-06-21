package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.rss.RSSFeed;
import com.vmladenov.cook.core.rss.RSSItem;
import com.vmladenov.cook.core.rss.RSSReader;

import java.util.ArrayList;

public class LatestRecipesLoader extends ISmallPreviewLoader {

    @Override
    public Boolean hasMore() {
        return false;
    }

    @Override
    public ArrayList<SmallPreview> getNextData() {
        RSSReader reader = new RSSReader();
        RSSFeed feed = reader.getFeed(GlobalStrings.LatestRecipesFeed);
        ArrayList<SmallPreview> list = new ArrayList<SmallPreview>();
        if (feed == null) return list;
        for (int i = 0; i < feed.size(); i++) {
            RSSItem item = feed.get(i);
            SmallPreview preview = new SmallPreview();
            preview.id = Helpers.getDataHelper().RecipesRepository.getLocalIdFromRecipeId(item
                    .getId());
            preview.imageUrl = Helpers.getDataHelper().RecipesRepository
                    .getImageUrlFromRecipeId(item.getId());
            preview.bigViewUrl = item.getLink();
            preview.title = item.getTitle();
            preview.description = "Добавена на "
                    + String.format("%d-%d-%d", item.getPubDate().getYear() + 1900, item
                    .getPubDate().getMonth() + 1, item.getPubDate().getDate());
            list.add(preview);
        }
        return list;
    }
}
