package com.vmladenov.cook.ui.recipes;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vmladenov.cook.R;
import com.vmladenov.cook.core.loaders.RecipesByCategoryLoader;
import com.vmladenov.cook.domain.PreviewListItem;
import com.vmladenov.cook.ui.SmallPreviewListActivity;

public class RecipesListActivity extends SmallPreviewListActivity<RecipesByCategoryLoader> {
	private int categoryId;
	private short type;
	private ArrayList<String> products;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("CATEGORY");
		products = bundle.getStringArrayList("products");
		categoryId = bundle.getInt("category_id");
		type = bundle.getShort("type");
		super.onCreate(savedInstanceState);
		setTitle(R.string.recipes);
	}

	@Override
	protected RecipesByCategoryLoader CreateLoader() {
		return new RecipesByCategoryLoader(categoryId, type, products);
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
}
