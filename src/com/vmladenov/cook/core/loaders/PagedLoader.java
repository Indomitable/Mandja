package com.vmladenov.cook.core.loaders;

import java.util.ArrayList;

import android.util.Pair;

import com.vmladenov.cook.domain.PreviewListItem;

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

    public abstract Pair<ArrayList<PreviewListItem>, Integer> getPagedData();

    public ArrayList<PreviewListItem> getNextData() {
        Pair<ArrayList<PreviewListItem>, Integer> data = getPagedData();
        this.setPage(data.second);
        return data.first;
    }
}
