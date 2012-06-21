package com.vmladenov.cook.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.loaders.LatestRecipesLoader;
import com.vmladenov.cook.ui.SmallPreviewListActivity;

public class LatestRecipesActivity extends SmallPreviewListActivity<LatestRecipesLoader> {

    @Override
    protected void onListItemClick(ListView parent, View v, int position, long id) {
        Intent intent = new Intent(LatestRecipesActivity.this, ViewRecipeActivity.class);
        ListAdapter adapter = getListAdapter();
        SmallPreview item = (SmallPreview) adapter.getItem(position);
        Bundle bundle = new Bundle();
        if (item.id > 0) {
            bundle.putLong("id", item.id);
            intent.putExtra("RECIPE_ID", bundle);
        } else {
            bundle.putString("url", item.bigViewUrl);
            intent.putExtra("RECIPE_URL", bundle);
        }
        startActivity(intent);
    }

    @Override
    protected LatestRecipesLoader CreateLoader() {
        return new LatestRecipesLoader();
    }
}