package com.vmladenov.cook.ui.shoppinglist;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.objects.ShoppingList;
import com.vmladenov.cook.core.objects.ShoppingListItem;

import java.util.List;

public class ViewShoppingListActivity extends ListActivity {
    ProgressDialog progressDialog = null;
    ShoppingList list = null;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = null;
        this.setContentView(R.layout.simple_link_list);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.setTitle(bundle.getString("title"));
        id = bundle.getLong("id");
        this.loadList(id);
        registerForContextMenu(this.findViewById(android.R.id.list));
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
                ShoppingListItem listItem = (ShoppingListItem) getListAdapter().getItem(
                        info.position);
                Helpers.getDataHelper().ShoppingListsRepository.deleteListItem(listItem.ID);
                loadList(id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // public boolean onCreateOptionsMenu(android.view.Menu menu)
    // {
    // MenuInflater inflater = getMenuInflater();
    // inflater.inflate(R.menu.add_new_product_menu, menu);
    // return true;
    // };
    //
    // public boolean onMenuItemSelected(int featureId, MenuItem item)
    // {
    // switch (item.getItemId())
    // {
    // case R.id.miAddNewProduct:
    // showAddNewProductDialog();
    // return true;
    // default:
    // return super.onMenuItemSelected(featureId, item);
    // }
    // };
    //
    // public void showAddNewProductDialog()
    // {
    //
    // }

    Handler loadHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (progressDialog != null) progressDialog.dismiss();
        }

        ;
    };

    private void loadList(final long id) {
        progressDialog = ProgressDialog.show(this, getString(R.string.loading),
                getString(R.string.loading), false);

        Thread readThread = new Thread(new Runnable() {

            @Override
            public void run() {
                list = Helpers.getDataHelper().ShoppingListsRepository.getShoppingList(id);
                loadHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        ShowData();
                    }
                });
                loadHandler.sendEmptyMessage(0);
            }
        });
        readThread.start();
    }

    private void ShowData() {
        if (list == null) return;
        ShoppingListAdapter adapter = new ShoppingListAdapter(this,
                android.R.layout.simple_list_item_checked, list.Items);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        CheckedTextView textview = (CheckedTextView) v;
        textview.setChecked(!textview.isChecked());
        ShoppingListItem item = (ShoppingListItem) getListAdapter().getItem(position);
        item.IsChecked = textview.isChecked();
        Helpers.getDataHelper().ShoppingListsRepository.setCheckedListItem(item.ID, item.IsChecked);
    }

    public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem> {
        public ShoppingListAdapter(Context context, int textViewResourceId,
                                   List<ShoppingListItem> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = super.getView(position, convertView, parent);
            }
            CheckedTextView textview = (CheckedTextView) v;
            ShoppingListItem item = getItem(position);
            textview.setChecked(item.IsChecked);
            return v;
        }
    }
}
