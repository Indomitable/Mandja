package com.vmladenov.cook.core.loaders;

import android.util.Pair;
import com.vmladenov.cook.core.html.SmallPreview;

import java.util.ArrayList;

public abstract class PagedLoader extends ISmallPreviewLoader {
    public PagedLoader() {
        this.setPage(1);
    }

    private Integer _page;

    public void setPage(Integer page) {
        this._page = page;
    }

    public Integer getPage() {
        return _page == null ? 1 : _page;
    }

    public Boolean hasMore() {
        return _page != null;
    }

    public abstract Pair<ArrayList<SmallPreview>, Integer> getPagedData();

    public ArrayList<SmallPreview> getNextData() {
        Pair<ArrayList<SmallPreview>, Integer> data = getPagedData();
        this.setPage(data.second);
        return data.first;
    }
}
