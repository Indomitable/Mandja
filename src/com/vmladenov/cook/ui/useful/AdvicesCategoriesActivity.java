package com.vmladenov.cook.ui.useful;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.objects.SimpleView;
import com.vmladenov.cook.ui.SimpleLinkListActivity;

import java.util.ArrayList;

public class AdvicesCategoriesActivity extends SimpleLinkListActivity<SimpleView> {
    @Override
    protected ArrayList<SimpleView> getData() {
        return Helpers.getDataHelper().AdvicesRepository.getCategories();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SimpleView category = (SimpleView) this.getListAdapter().getItem(position);
        Intent intent = new Intent(this, AdvicesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("category_id", category.id);
        intent.putExtra("CATEGORY", bundle);
        startActivity(intent);
    }
}
