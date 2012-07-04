package com.vmladenov.cook.core.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.vmladenov.cook.core.loaders.ISmallPreviewLoader;
import com.vmladenov.cook.domain.PreviewListItem;

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
                    for (int i = 0; i < size; i++) {
                        ContinuesSmallPreviewListAdapter.this.add(loadedData.get(i));
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
