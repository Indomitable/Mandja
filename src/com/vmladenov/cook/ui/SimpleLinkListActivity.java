package com.vmladenov.cook.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import com.vmladenov.cook.R;

import java.util.ArrayList;

public abstract class SimpleLinkListActivity<T> extends ListActivity {
    ProgressDialog progressDialog = null;
    ArrayList<T> data = null;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.simple_link_list);
        progressDialog = null;

        data = null;

        data = (ArrayList<T>) getLastNonConfigurationInstance();
        if (data != null) {
            ShowData();
        } else {
            LoadData();
        }
    }

    Handler loadHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (progressDialog != null) progressDialog.dismiss();
        }

        ;
    };

    protected void LoadData() {
        progressDialog = ProgressDialog.show(this, getString(R.string.loading),
                getString(R.string.loading), false);

        Thread readThread = new Thread(new Runnable() {

            @Override
            public void run() {
                data = getData();
                loadHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        ShowData();
                    }
                });
                loadHandler.sendEmptyMessage(0);
            }
        });
        readThread.start();
    }

    private void ShowData() {
        if (data == null) return;
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(this, android.R.layout.simple_list_item_1,
                data);
        setListAdapter(adapter);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return data;
    }

    protected abstract ArrayList<T> getData();
}
