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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.adapters.ContinuesSmallPreviewListAdapter;
import com.vmladenov.cook.core.loaders.ISmallPreviewLoader;
import com.vmladenov.cook.domain.PreviewListItem;

import java.util.ArrayList;

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
