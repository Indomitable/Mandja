package com.vmladenov.cook.core.objects;

public class SimpleView {
    public long id;
    public String title;

    @Override
    public String toString() {
        return title;
    }

}
