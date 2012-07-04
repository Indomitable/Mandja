package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.domain.PreviewListItem;
import java.util.ArrayList;

public abstract class ISmallPreviewLoader {
    public abstract Boolean hasMore();

    public abstract ArrayList<PreviewListItem> getNextData();

    public int sorting;
}
