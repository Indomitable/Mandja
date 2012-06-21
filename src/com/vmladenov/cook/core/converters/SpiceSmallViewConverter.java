package com.vmladenov.cook.core.converters;

import android.database.Cursor;
import com.vmladenov.cook.core.GlobalStrings;
import com.vmladenov.cook.core.html.SmallPreview;

public class SpiceSmallViewConverter implements ICursorConverter<SmallPreview> {

    @Override
    public SmallPreview Convert(Cursor cursor) {
        String imageUrl = cursor.getString(3);

        SmallPreview preview = new SmallPreview();

        preview.id = cursor.getLong(0);
        preview.title = cursor.getString(4); // Title
        preview.bigViewUrl = cursor.getString(2); // URL

        if (imageUrl != null) {
            String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            String smallImageUrl = GlobalStrings.SpiceImagesUrlPrefix
                    + GlobalStrings.SmallImage100UrlPrefix + imageName;
            preview.imageUrl = smallImageUrl;
        }
        return preview;
    }

}
