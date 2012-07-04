package com.vmladenov.cook.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vmladenov.cook.R;
import com.vmladenov.cook.core.loaders.FavoriteRecipesLoader;
import com.vmladenov.cook.domain.PreviewListItem;
import com.vmladenov.cook.ui.SmallPreviewListActivity;

public class FavoriteRecipesActivity extends SmallPreviewListActivity<FavoriteRecipesLoader>
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle(R.string.favorites);
	}

	@Override
	protected FavoriteRecipesLoader CreateLoader()
	{
		return new FavoriteRecipesLoader();
	}

	@Override
	protected void onListItemClick(ListView parent, View v, int position, long id)
	{
		ListAdapter adapter = getListAdapter();
		PreviewListItem item = (PreviewListItem) adapter.getItem(position);
		Intent intent = new Intent(this, ViewRecipeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("id", item.getId());
		intent.putExtra("RECIPE_ID", bundle);
		startActivity(intent);
	}
}
