package com.vmladenov.cook.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.loaders.RecipesByIdLoader;
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
        SmallPreview item = (SmallPreview) adapter.getItem(position);
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", item.id);
        intent.putExtra("RECIPE_ID", bundle);
        startActivity(intent);
    }

    @Override
    protected RecipesByIdLoader CreateLoader() {
        return new RecipesByIdLoader();
    }
}
