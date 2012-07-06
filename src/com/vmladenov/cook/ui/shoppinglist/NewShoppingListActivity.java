package com.vmladenov.cook.ui.shoppinglist;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class NewShoppingListActivity extends ListActivity implements OnKeyListener {
	private AutoCompleteTextView autoComplete;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<String> items;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.new_shopping_list);

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("SHOPPING_LIST_TITLE");
		title = bundle.getString("TITLE");
		setTitle(title);

		autoComplete = (AutoCompleteTextView) findViewById(R.id.txtSearchProduct);
		SearchProductAndSpiceAdapter adapter = new SearchProductAndSpiceAdapter(this);
		autoComplete.setAdapter(adapter);
		autoComplete.setOnKeyListener(this);

		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		items = new ArrayList<String>();
		this.setListAdapter(listAdapter);
		registerForContextMenu(this.findViewById(android.R.id.list));
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
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(R.string.information);
			alertDialogBuilder.setMessage(R.string.enterProducts);
			alertDialogBuilder.setIcon(R.drawable.information_icon);
			alertDialogBuilder.setPositiveButton(R.string.ok, new OnClickListener() {

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

		for (int i = 0; i < items.size(); i++) {
			ShoppingListItem item = new ShoppingListItem();
			item.Title = items.get(i);
			list.Items.add(item);
		}
		ShoppingListsRepository repository = Helpers.getDataHelper().getShoppingListsRepository();
		repository.saveShoppingList(list);
		goToShoppingLists();
	}

	void goToShoppingLists() {
		Intent intent = new Intent();
		intent.setClass(NewShoppingListActivity.this, ShoppingListsActivity.class);
		startActivity(intent);
	}
}
