package com.vmladenov.cook.ui;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vmladenov.cook.R;
import com.vmladenov.cook.core.adapters.ContinuesSmallPreviewListAdapter;
import com.vmladenov.cook.core.loaders.ISmallPreviewLoader;
import com.vmladenov.cook.domain.PreviewListItem;

public abstract class PagedListActivity<TLoader extends ISmallPreviewLoader> extends ListActivity {
	ProgressDialog progressDialog = null;
	ContinuesSmallPreviewListAdapter<TLoader> adapter;
	protected TLoader _loader;
	protected Menu mainMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alphabet_recipes);
		progressDialog = null;
		_loader = CreateLoader();
		loadFirstData();
	}

	protected void loadFirstData() {
		progressDialog = ProgressDialog.show(this, getString(R.string.loading),
				getString(R.string.loading), false);
		Thread readThread = new Thread(new Runnable() {
			@Override
			public void run() {
				final ArrayList<PreviewListItem> loadedData = _loader.getNextData();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showFirstData(loadedData);
						if (progressDialog != null)
							progressDialog.dismiss();
					}
				});
			}
		});
		readThread.start();
	}

	protected ContinuesSmallPreviewListAdapter<TLoader> CreateAdaptor(
			ArrayList<PreviewListItem> loadedData)

	{
		return new ContinuesSmallPreviewListAdapter<TLoader>(this, _loader, loadedData);
	}

	protected abstract TLoader CreateLoader();

	private void showFirstData(ArrayList<PreviewListItem> loadedData) {
		adapter = CreateAdaptor(loadedData);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.paged_list_menu, menu);
		mainMenu = menu;
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miAlphabetSorting:
			toggleSorting(1);
			return true;
		case R.id.miNoSorting:
			toggleSorting(2);
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	protected void toggleSorting(int miItem) {
		MenuItem itemAlphabetSorting = mainMenu.findItem(R.id.miAlphabetSorting);
		MenuItem itemNoSorting = mainMenu.findItem(R.id.miNoSorting);
		_loader = CreateLoader();
		switch (miItem) {
		case 1:
			itemAlphabetSorting.setVisible(false);
			itemNoSorting.setVisible(true);
			_loader.sorting = 1;
			break;
		case 2:
			itemAlphabetSorting.setVisible(true);
			itemNoSorting.setVisible(false);
			_loader.sorting = 0;
			break;
		}
		loadFirstData();
	}
}
