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

package com.vmladenov.cook.core.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import com.vmladenov.cook.core.loaders.ISmallPreviewLoader;
import com.vmladenov.cook.domain.PreviewListItem;

import java.util.ArrayList;
import java.util.List;

public class ContinuesSmallPreviewListAdapter<TLoader extends ISmallPreviewLoader> extends
        SmallPreviewListAdapter {
    protected Boolean loadingMore = false;
    protected Handler handler = new Handler();
    private TLoader loader;

    public ContinuesSmallPreviewListAdapter(Context context, TLoader loader,
                                            List<PreviewListItem> objects) {
        super(context, objects);
        this.loader = loader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!loadingMore && position == getCount() - 1) {
            if (this.hasMore()) {
                loadingMore = true;
                addMoreData();
            }
        }
        return super.getView(position, convertView, parent);
    }

    protected void addMoreData() {
        new Thread(null, new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<PreviewListItem> loadedData = loader.getNextData();
                    loadMoreData(loadedData);
                } catch (Exception e) {
                    loadingMore = false;
                }
            }
        }).start();
    }

    protected void loadMoreData(final ArrayList<PreviewListItem> loadedData) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    int size = loadedData.size();
                    for (PreviewListItem aLoadedData : loadedData) {
                        ContinuesSmallPreviewListAdapter.this.add(aLoadedData);
                    }
                    ContinuesSmallPreviewListAdapter.this.notifyDataSetChanged();
                } finally {
                    loadingMore = false;
                }
            }
        });
    }

    public Boolean hasMore() {
        return loader.hasMore();
    }
}
