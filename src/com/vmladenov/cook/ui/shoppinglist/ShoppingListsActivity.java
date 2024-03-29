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
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.db.ShoppingListsRepository;
import com.vmladenov.cook.core.objects.ShoppingList;

import java.util.ArrayList;

public final class ShoppingListsActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_lists);
		setTitle(R.string.shoppingLists);
		loadData();
		registerForContextMenu(this.findViewById(android.R.id.list));
	}

	private void loadData()
	{
		ShoppingListsRepository repository = Helpers.getDataHelper().getShoppingListsRepository();
		ArrayList<ShoppingList> data = repository.getShoppingLists();
		if (data == null)
			return;
		ArrayAdapter<ShoppingList> adapter = new ArrayAdapter<ShoppingList>(this, android.R.layout.simple_list_item_1, data);
		setListAdapter(adapter);
	}

	public void onNewShoppingList(View sender) {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.new_shopping_list_title, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(view);
		final AlertDialog dialog = alertDialogBuilder.create();
		final EditText edtTitle = (EditText) view.findViewById(R.id.edtTitle);

		Button buttonOk = (Button) view.findViewById(R.id.bTitleOk);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = edtTitle.getText().toString();
				if (title.length() > 0) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("TITLE", title);
					intent.putExtra("SHOPPING_LIST_TITLE", bundle);
					intent.setClass(ShoppingListsActivity.this, NewShoppingListActivity.class);
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});

		Button buttonCancel = (Button) view.findViewById(R.id.bTitleCancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ShoppingList list = (ShoppingList) this.getListAdapter().getItem(position);
		Intent intent = new Intent(this, ViewShoppingListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong("id", list.ID);
		bundle.putString("title", list.Title);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_context_delete, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.miDelete:
				ShoppingList list = (ShoppingList) getListAdapter().getItem(info.position);
				Helpers.getDataHelper().getShoppingListsRepository().deleteList(list.ID);
				loadData();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

}
