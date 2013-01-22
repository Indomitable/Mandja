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

package com.vmladenov.cook.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.loaders.RecipesByIdLoader;
import com.vmladenov.cook.domain.PreviewListItem;
import com.vmladenov.cook.ui.PagedListActivity;

public final class AllRecipesActivity extends PagedListActivity<RecipesByIdLoader> {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle(R.string.allRecipesLong);
	}

	@Override
	public boolean onSearchRequested() {
		startSearch(null, false, null, false);
		return true;
	}

	@Override
	protected void onListItemClick(ListView parent, View v, int position, long id) {
		ListAdapter adapter = getListAdapter();
		PreviewListItem item = (PreviewListItem) adapter.getItem(position);
		Intent intent = new Intent(this, ViewRecipeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("id", item.getId());
		intent.putExtra("RECIPE_ID", bundle);
		startActivity(intent);
	}

	@Override
	protected RecipesByIdLoader CreateLoader() {
		return new RecipesByIdLoader();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.recipes_list_menu, menu);
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
		case R.id.miSearch:
			startSearch(null, false, null, false);
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}
}
