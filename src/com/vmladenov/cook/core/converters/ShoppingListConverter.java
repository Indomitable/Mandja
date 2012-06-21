package com.vmladenov.cook.core.converters;

import android.database.Cursor;
import com.vmladenov.cook.core.objects.ShoppingList;

public class ShoppingListConverter implements ICursorConverter<ShoppingList> {

    @Override
    public ShoppingList Convert(Cursor cursor) {
        ShoppingList list = new ShoppingList();
        list.ID = cursor.getLong(0);
        list.Title = cursor.getString(1);
        list.DateCreated = cursor.getString(2);
        return list;
    }

}
