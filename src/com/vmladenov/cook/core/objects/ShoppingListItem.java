package com.vmladenov.cook.core.objects;

public class ShoppingListItem {
    public String Title;
    public Integer Order;
    public Boolean IsChecked;
    public long ID;

    @Override
    public String toString() {
        return this.Title;
    }
}
