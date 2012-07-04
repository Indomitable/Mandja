package com.vmladenov.cook.core.converters;

import android.database.Cursor;

import com.vmladenov.cook.domain.PreviewListItem;

public class PreviewListItemConverter implements ICursorConverter<PreviewListItem> {
    public PreviewListItemConverter() {
    }

    public PreviewListItem Convert(Cursor cursor) {
        PreviewListItem recipe = new PreviewListItem();
        recipe.setId(cursor.getInt(0)); // ID
        recipe.setThumbnailUrl(cursor.getString(1));
        recipe.setTitle(cursor.getString(2));
        return recipe;
    }

}
