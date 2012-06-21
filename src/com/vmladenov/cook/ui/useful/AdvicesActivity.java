package com.vmladenov.cook.ui.useful;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.html.SmallPreview;
import com.vmladenov.cook.core.loaders.AdvicesLoader;
import com.vmladenov.cook.ui.PagedListActivity;

public class AdvicesActivity extends PagedListActivity<AdvicesLoader> {

    private long categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("CATEGORY");
        categoryId = bundle.getLong("category_id");

        super.onCreate(savedInstanceState);
        this.setTitle(R.string.advices);
    }

    @Override
    protected AdvicesLoader CreateLoader() {
        return new AdvicesLoader(this, categoryId);
    }

    @Override
    protected void onListItemClick(ListView parent, View v, int position, long id) {
        ListAdapter adapter = getListAdapter();
        SmallPreview item = (SmallPreview) adapter.getItem(position);
        Intent intent = new Intent(this, UsefulViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("id", item.id);
        bundle.putInt("category", 0);
        intent.putExtra("USEFUL", bundle);
        startActivity(intent);
    }
}
