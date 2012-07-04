package com.vmladenov.cook.ui.useful;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vmladenov.cook.R;
import com.vmladenov.cook.core.loaders.ProductsLoader;
import com.vmladenov.cook.domain.PreviewListItem;
import com.vmladenov.cook.ui.PagedListActivity;

public class ProductsActivity extends PagedListActivity<ProductsLoader> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle(R.string.products);
	}

	@Override
	protected void onListItemClick(ListView parent, View v, int position, long id) {
		ListAdapter adapter = getListAdapter();
		PreviewListItem item = (PreviewListItem) adapter.getItem(position);
		Intent intent = new Intent(this, UsefulViewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("id", item.getId());
		bundle.putInt("category", 2);
		intent.putExtra("USEFUL", bundle);
		startActivity(intent);
	}

	@Override
	protected ProductsLoader CreateLoader() {
		return new ProductsLoader();
	}

}