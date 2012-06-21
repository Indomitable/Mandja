package com.vmladenov.cook.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.objects.SimpleView;
import com.vmladenov.cook.ui.SimpleLinkListActivity;

import java.util.ArrayList;

public class TenRecipesCategoriesActivity extends SimpleLinkListActivity<SimpleView> {
    @Override
    protected ArrayList<SimpleView> getData() {
        return Helpers.getDataHelper().RecipesRepository.getRecipeTenCategories();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SimpleView category = (SimpleView) this.getListAdapter().getItem(position);
        Intent intent = new Intent(this, RecipesListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("category_id", category.id);
        bundle.putInt("type", 2);
        intent.putExtra("CATEGORY", bundle);
        startActivity(intent);
    }

}
