package com.vmladenov.cook.ui.useful;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.adapters.SmallPreviewListAdapter;
import com.vmladenov.cook.domain.PreviewListItem;

public class UsefulSearchActivity extends ListActivity {
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Bundle bundle = intent.getBundleExtra(SearchManager.APP_DATA);
            category = bundle.getInt("category");
            ProcessQuery(query);
        }
    }

    private void ProcessQuery(String query) {
        ArrayList<PreviewListItem> items = null;
        switch (category) {
            case 1:
                items = Helpers.getDataHelper().SpicesRepository.searchSpices(query);
                break;
            default:
                return;
        }
        SmallPreviewListAdapter adapter = new SmallPreviewListAdapter(this, items);
        setListAdapter(adapter);
    }
}
