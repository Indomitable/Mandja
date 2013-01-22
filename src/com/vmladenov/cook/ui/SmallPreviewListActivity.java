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

package com.vmladenov.cook.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.adapters.SmallPreviewListAdapter;
import com.vmladenov.cook.core.loaders.ISmallPreviewLoader;
import com.vmladenov.cook.domain.PreviewListItem;

import java.util.ArrayList;

public abstract class SmallPreviewListActivity<TLoader extends ISmallPreviewLoader> extends
        ListActivity {
    ProgressDialog progressDialog = null;
    protected TLoader _loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphabet_recipes);
        progressDialog = null;
        _loader = CreateLoader();
        loadData();
    }

    protected void loadData() {
        progressDialog = ProgressDialog.show(this, getString(R.string.loading),
                getString(R.string.loading), false);
        Thread readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<PreviewListItem> loadedData = _loader.getNextData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showData(loadedData);
                        if (progressDialog != null) progressDialog.dismiss();
                    }
                });
            }
        });
        readThread.start();
    }

    protected abstract TLoader CreateLoader();

    private void showData(ArrayList<PreviewListItem> loadedData) {
        SmallPreviewListAdapter adapter = new SmallPreviewListAdapter(this, loadedData);
        setListAdapter(adapter);
    }
}
