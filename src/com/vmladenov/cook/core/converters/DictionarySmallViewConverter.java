package com.vmladenov.cook.core.converters;

import android.database.Cursor;
import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.html.SmallPreview;

public class DictionarySmallViewConverter implements ICursorConverter<SmallPreview> {

    @Override
    public SmallPreview Convert(Cursor cursor) {
        SmallPreview preview = new SmallPreview();
        preview.id = cursor.getLong(0);
        preview.title = cursor.getString(2); // Title
        String imageUrl = cursor.getString(1);
        if (imageUrl != null) {
            String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            String smallImageUrl = GlobalStrings.DictionaryImagesUrlPrefix
                    + GlobalStrings.SmallImage100UrlPrefix + imageName;
            preview.imageUrl = smallImageUrl;
        }
        return preview;
    }
}
