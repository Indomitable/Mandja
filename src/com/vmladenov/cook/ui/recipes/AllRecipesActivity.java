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
