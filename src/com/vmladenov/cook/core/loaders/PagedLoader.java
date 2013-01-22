/*
 * Copyright (C) 2011 Ventsislav Mladenov <ventsislav dot mladenov at gmail dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.vmladenov.cook.core.loaders;

import android.util.Pair;
import com.vmladenov.cook.domain.PreviewListItem;

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

    public abstract Pair<ArrayList<PreviewListItem>, Integer> getPagedData();

    public ArrayList<PreviewListItem> getNextData() {
        Pair<ArrayList<PreviewListItem>, Integer> data = getPagedData();
        this.setPage(data.second);
        return data.first;
    }
}
