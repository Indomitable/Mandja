package com.vmladenov.cook.core.db;

import com.vmladenov.cook.core.html.SmallPreview;

import java.util.ArrayList;

public interface ISmallPreviewListRepository {
    long getMaxId();

    ArrayList<SmallPreview> getPreviews(long start, int sorting);
}
