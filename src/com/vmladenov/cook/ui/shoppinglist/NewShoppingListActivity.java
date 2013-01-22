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

package com.vmladenov.cook.ui.shoppinglist;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnKeyListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.adapters.SearchProductAndSpiceAdapter;
import com.vmladenov.cook.core.db.ShoppingListsRepository;
import com.vmladenov.cook.core.objects.ShoppingList;
import com.vmladenov.cook.core.objects.ShoppingListItem;

import java.util.ArrayList;

public class NewShoppingListActivity extends ListActivity implements
		OnKeyListener {
	private AutoCompleteTextView autoComplete;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<String> items;
	private String title;
	private SearchProductAndSpiceAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.new_shopping_list);

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("SHOPPING_LIST_TITLE");
		title = bundle.getString("TITLE");
		setTitle(title);

		autoComplete = (AutoCompleteTextView) findViewById(R.id.txtSearchProduct);
		adapter = new SearchProductAndSpiceAdapter(this, Helpers
				.getDataHelper().getDbPath());
		autoComplete.setAdapter(adapter);
		autoComplete.setOnKeyListener(this);

		listAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		items = new ArrayList<String>();
		this.setListAdapter(listAdapter);
		registerForContextMenu(this.findViewById(android.R.id.list));
	}

	@Override
	protected void onDestroy() {
		adapter.close();
		super.onDestroy();
	}

	public void onAddItemSearch(View sender) {
		addItem();
	}

	private void addItem() {
		String item = autoComplete.getText().toString();
		if (item.length() > 0 && !items.contains(item)) {
			items.add(item);
			listAdapter.add(autoComplete.getText().toString());
			listAdapter.notifyDataSetChanged();
		}
		autoComplete.setText("");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_context_delete, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.miDelete:
			deleteItem(info);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void deleteItem(AdapterContextMenuInfo info) {
		int pos = (int) info.id;
		String item = items.get(pos);
		items.remove(pos);
		listAdapter.remove(item);
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == 66) {
			addItem();
			return true;
		}
		return false;
	}

	public void onSaveShoppingList(View sender) {
		if (items.size() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle(R.string.information);
			alertDialogBuilder.setMessage(R.string.enterProducts);
			alertDialogBuilder.setIcon(R.drawable.information_icon);
			alertDialogBuilder.setPositiveButton(R.string.ok,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			AlertDialog dialog = alertDialogBuilder.create();
			dialog.show();
			return;
		}

		ShoppingList list = new ShoppingList();
		list.Title = title;

        for (String item1 : items) {
            ShoppingListItem item = new ShoppingListItem();
            item.Title = item1;
            list.Items.add(item);
        }
		ShoppingListsRepository repository = Helpers.getDataHelper()
				.getShoppingListsRepository();
		repository.saveShoppingList(list);
		goToShoppingLists();
	}

	void goToShoppingLists() {
		Intent intent = new Intent();
		intent.setClass(NewShoppingListActivity.this,
				ShoppingListsActivity.class);
		startActivity(intent);
	}
}
