package com.vmladenov.cook.core.converters;

import android.database.Cursor;
import com.vmladenov.cook.core.html.SmallPreview;

public class AdviceSmallViewConverter implements ICursorConverter<SmallPreview> {

    @Override
    public SmallPreview Convert(Cursor cursor) {
        SmallPreview preview = new SmallPreview();
        preview.id = cursor.getLong(0);
        preview.title = cursor.getString(4); // Title
        preview.bigViewUrl = cursor.getString(2); // URL
        preview.imageUrl = cursor.getString(3);
        return preview;
    }

}
