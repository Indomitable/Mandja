package com.vmladenov.cook.ui.shoppinglist;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import com.vmladenov.cook.R;

public class ShoppingListTabHostActivity extends TabActivity implements OnTabChangeListener {
    public String NewShoppingListTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewShoppingListTitle = "";
        setContentView(R.layout.shopping_list_tabhost);
        setTitle(R.string.shoppingLists);
        setTabLayout();
    }

    private void setTabLayout() {
        TabHost tabHost = getTabHost();

        Intent intent = new Intent();
        intent.setClass(this, NewShoppingListActivity.class);

        setupTag(tabHost, intent, "newShoppingList", R.string.newShoppingList);

        intent = new Intent();
        intent.setClass(this, OldShoppingListActivity.class);
        setupTag(tabHost, intent, "oldShoppingLists", R.string.oldShoppingLists);

        tabHost.setCurrentTab(1);
        tabHost.setOnTabChangedListener(this);
    }

    private void setupTag(TabHost tabHost, Intent intent, String tag, int title) {
        View tabView = createTabView(tabHost.getContext(), title);
        TabHost.TabSpec spec = tabHost.newTabSpec(tag).setIndicator(tabView).setContent(intent);
        tabHost.addTab(spec);
    }

    private View createTabView(Context context, int text) {
        View view = ((LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.custom_tab_view, null);
        TextView tv = (TextView) view.findViewById(R.id.tabTitle);
        tv.setText(text);
        return view;
    }

    public void switchTab() {
        getTabHost().setCurrentTab(1);
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId == "newShoppingList") //
        {
            NewShoppingListTitle = "";
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.new_shopping_list_title, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(view);
            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.setCancelable(false);
            dialog.setOnKeyListener(new OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            final EditText edtTitle = (EditText) view.findViewById(R.id.edtTitle);
            Button button = (Button) view.findViewById(R.id.bTitleOk);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewShoppingListTitle = edtTitle.getText().toString();
                    if (NewShoppingListTitle.length() == 0) return;
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
