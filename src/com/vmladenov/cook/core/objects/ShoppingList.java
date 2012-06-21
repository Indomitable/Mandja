package com.vmladenov.cook.core.objects;

import java.util.ArrayList;

public class ShoppingList {
    public String Title;
    public String DateCreated;
    public ArrayList<ShoppingListItem> Items;
    public long ID;

    public ShoppingList() {
        Items = new ArrayList<ShoppingListItem>();
    }

    @Override
    public String toString() {
        return Title;
    }
}
