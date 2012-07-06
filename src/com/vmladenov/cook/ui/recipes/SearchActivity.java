package com.vmladenov.cook.ui.recipes;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.adapters.SmallPreviewListAdapter;
import com.vmladenov.cook.core.db.RecipesRepository;
import com.vmladenov.cook.domain.PreviewListItem;

public class SearchActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			ProcessQuery(query);
		}
	}

	private void ProcessQuery(String query) {
		RecipesRepository repository = Helpers.getDataHelper().getRecipesRepository();
		SmallPreviewListAdapter adapter = new SmallPreviewListAdapter(this, repository.searchRecipes(query));
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, ViewRecipeActivity.class);
		ListAdapter adapter = getListAdapter();
		PreviewListItem item = (PreviewListItem) adapter.getItem(position);
		Bundle bundle = new Bundle();
		bundle.putInt("id", item.getId());
		intent.putExtra("RECIPE_ID", bundle);
		startActivity(intent);
	}
}
