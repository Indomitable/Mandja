package com.vmladenov.cook.ui.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.objects.ShoppingList;
import com.vmladenov.cook.ui.SimpleLinkListActivity;

import java.util.ArrayList;

public class OldShoppingListActivity extends SimpleLinkListActivity<ShoppingList> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                ShoppingList list = (ShoppingList) getListAdapter().getItem(info.position);
                Helpers.getDataHelper().ShoppingListsRepository.deleteList(list.ID);
                LoadData();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected ArrayList<ShoppingList> getData() {
        return Helpers.getDataHelper().ShoppingListsRepository.getShoppingLists();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        super.LoadData();
    }
}
