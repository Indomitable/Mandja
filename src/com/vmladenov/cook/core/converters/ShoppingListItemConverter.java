package com.vmladenov.cook.core.converters;

import android.database.Cursor;
import com.vmladenov.cook.core.objects.ShoppingListItem;

public class ShoppingListItemConverter implements ICursorConverter<ShoppingListItem> {

    @Override
    public ShoppingListItem Convert(Cursor cursor) {
        ShoppingListItem item = new ShoppingListItem();
        item.ID = cursor.getLong(0);
        item.Title = cursor.getString(2);
        item.Order = cursor.getInt(3);
        item.IsChecked = cursor.getInt(4) == 1;
        return item;
    }

}
