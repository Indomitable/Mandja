package com.vmladenov.cook.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.objects.SimpleView;
import com.vmladenov.cook.ui.SimpleLinkListActivity;

import java.util.ArrayList;

public class RecipeSubCategoriesActivity extends SimpleLinkListActivity<SimpleView> {
    private long categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("CATEGORY");
        categoryId = bundle.getLong("category_id");
        this.setTitle(R.string.subCategoriesRecipes);
    }

    @Override
    protected ArrayList<SimpleView> getData() {
        return Helpers.getDataHelper().RecipesRepository.getRecipeSubCategories(categoryId);
    }

    @Override
    protected void onListItemClick(ListView parent, View v, int position, long id) {
        SimpleView category = (SimpleView) this.getListAdapter().getItem(position);
        Intent intent = new Intent(this, RecipesListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("category_id", category.id);
        bundle.putInt("type", 1);
        intent.putExtra("CATEGORY", bundle);
        startActivity(intent);
    }
}
