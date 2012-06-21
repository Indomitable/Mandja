package com.vmladenov.cook.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.loaders.RecipesByCategoryLoader;
import com.vmladenov.cook.ui.SmallPreviewListActivity;

import java.util.ArrayList;

public class RecipesListActivity extends SmallPreviewListActivity<RecipesByCategoryLoader> {
    private long categoryId;
    private int type;
    private ArrayList<String> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("CATEGORY");
        products = bundle.getStringArrayList("products");
        categoryId = bundle.getLong("category_id");
        type = bundle.getInt("type");
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
        SmallPreview item = (SmallPreview) adapter.getItem(position);
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", item.id);
        intent.putExtra("RECIPE_ID", bundle);
        startActivity(intent);
    }
}
