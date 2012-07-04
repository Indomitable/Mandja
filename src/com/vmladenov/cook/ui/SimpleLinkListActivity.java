package com.vmladenov.cook.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.vmladenov.cook.R;

public abstract class SimpleLinkListActivity<T> extends ListActivity {
	static ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.simple_link_list);
		progressDialog = null;
		LoadData();
	}

	static Handler loadHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (progressDialog != null)
				progressDialog.dismiss();
		};
	};

	protected void LoadData() {
		progressDialog = ProgressDialog.show(this, getString(R.string.loading),
				getString(R.string.loading), false);

		Thread readThread = new Thread(new Runnable() {

			@Override
			public void run() {
				final ArrayList<T> data = getData();
				loadHandler.post(new Runnable() {

					@Override
					public void run() {
						ShowData(data);
					}
				});
				loadHandler.sendEmptyMessage(0);
			}
		});
		readThread.start();
	}

	private void ShowData(ArrayList<T> data) {
		if (data == null)
			return;
		ArrayAdapter<T> adapter = new ArrayAdapter<T>(this, android.R.layout.simple_list_item_1,
				data);
		setListAdapter(adapter);
	}

	protected abstract ArrayList<T> getData();
}
