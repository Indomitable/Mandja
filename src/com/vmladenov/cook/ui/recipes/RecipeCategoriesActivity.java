package com.vmladenov.cook.ui.recipes;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.objects.SimpleView;
import com.vmladenov.cook.ui.SimpleLinkListActivity;

public final class RecipeCategoriesActivity extends SimpleLinkListActivity<SimpleView> {

	private int parentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("PARENT");
		if (bundle == null) {
			parentId = 0;
		} else {
			parentId = bundle.getInt("PARENT_ID");
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected ArrayList<SimpleView> getData() {
		return Helpers.getDataHelper().RecipesRepository.getRecipeCategories(parentId);
	}

	@Override
	protected void onListItemClick(ListView parent, View v, int position, long id) {
		SimpleView parentItem = (SimpleView) this.getListAdapter().getItem(position);
		Boolean check = Helpers.getDataHelper().RecipesRepository.checkForChildCategories(parentItem.id);
		if (check) {
			Intent intent = new Intent(this, RecipeCategoriesActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("PARENT_ID", parentItem.id);
			intent.putExtra("PARENT", bundle);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, RecipesListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("category_id", parentItem.id);
			bundle.putShort("type", (short) 1);
			intent.putExtra("CATEGORY", bundle);
			startActivity(intent);
		}
	}
}
