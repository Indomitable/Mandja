package com.vmladenov.cook.core.converters;

import android.database.Cursor;
import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.html.SmallPreview;

public class RecipeSmallViewConverter implements ICursorConverter<SmallPreview> {
    public RecipeSmallViewConverter() {
    }

    @Override
    public SmallPreview Convert(Cursor cursor) {
        SmallPreview preview = new SmallPreview();

        preview.id = cursor.getLong(0);
        preview.title = cursor.getString(4); // Title
        preview.description = cursor.getString(8); // Date
        preview.bigViewUrl = cursor.getString(2); // URL
        String imageUrl = cursor.getString(3);
        if (imageUrl != null) {
            String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            String smallImageUrl = GlobalStrings.RecipeImagesUrlPrefix
                    + GlobalStrings.SmallImage60UrlPrefix + imageName;
            preview.imageUrl = smallImageUrl;
        }

        return preview;
    }

}
