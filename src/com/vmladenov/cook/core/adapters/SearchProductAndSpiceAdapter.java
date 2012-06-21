package com.vmladenov.cook.core.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.db.ProductsRepository;

public class SearchProductAndSpiceAdapter extends CursorAdapter {

    ProductsRepository repository;

    public SearchProductAndSpiceAdapter(Context context) {
        super(context, null, true);
        repository = Helpers.getDataHelper().ProductsRepository;
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (constraint == null) return null;
        return repository.getProductsAndSpices(constraint);
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor.getString(1);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final CharSequence text = convertToString(cursor);
        TextView txtView = (TextView) view;
        txtView.setText(text);
        txtView.setTextColor(Color.BLACK);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent,
                false);
        return view;
    }
}
