package com.vmladenov.cook.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.adapters.SmallPreviewListAdapter;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.loaders.ISmallPreviewLoader;

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
                final ArrayList<SmallPreview> loadedData = _loader.getNextData();
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

    private void showData(ArrayList<SmallPreview> loadedData) {
        SmallPreviewListAdapter adapter = new SmallPreviewListAdapter(this, loadedData);
        setListAdapter(adapter);
    }
}
