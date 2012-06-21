package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.core.html.SmallPreview;

import java.util.ArrayList;

public abstract class ISmallPreviewLoader {
    public abstract Boolean hasMore();

    public abstract ArrayList<SmallPreview> getNextData();

    public int sorting;
}
