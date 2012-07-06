package com.vmladenov.cook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vmladenov.cook.R;
import com.vmladenov.cook.ui.recipes.SearchByProductActivity;
import com.vmladenov.cook.ui.shoppinglist.ShoppingListsActivity;

public class ToolsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.tools);
		this.setTitle(R.string.tools);
	}

	public void onSearchClick(View sender) {
		Intent intent = new Intent();
		intent.setClass(ToolsActivity.this, SearchByProductActivity.class);
		startActivity(intent);
	}

	public void onShoppingListClick(View sender) {
		Intent intent = new Intent();
		intent.setClass(ToolsActivity.this, ShoppingListsActivity.class);
		startActivity(intent);
	}

	public void onTimerClick(View sender) {
		Intent intent = new Intent();
		intent.setClass(ToolsActivity.this, TimerActivity.class);
		startActivity(intent);
	}
}
