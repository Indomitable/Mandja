/*
 * Copyright (C) 2011 Ventsislav Mladenov <ventsislav dot mladenov at gmail dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
