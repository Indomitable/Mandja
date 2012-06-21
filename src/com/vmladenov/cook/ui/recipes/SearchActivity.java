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
import com.vmladenov.cook.core.html.SmallPreview;

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
        SmallPreviewListAdapter adapter = new SmallPreviewListAdapter(this,
                Helpers.getDataHelper().RecipesRepository.searchRecipes(query));
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        ListAdapter adapter = getListAdapter();
        SmallPreview item = (SmallPreview) adapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putLong("id", item.id);
        intent.putExtra("RECIPE_ID", bundle);
        startActivity(intent);
    }

    // private StringBuilder inputStreamToString(InputStream is) throws
    // IOException {
    // String line = "";
    // StringBuilder total = new StringBuilder();
    //
    // // Wrap a BufferedReader around the InputStream
    // BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    //
    // // Read response until the end
    // while ((line = rd.readLine()) != null) {
    // total.append(line);
    // }
    //
    // // Return full string
    // return total;
    // }
}
