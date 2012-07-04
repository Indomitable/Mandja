package com.vmladenov.cook.core.converters;

import android.database.Cursor;

import com.vmladenov.cook.core.objects.SimpleView;

public class SimpleViewConverter implements ICursorConverter<SimpleView> {
	@Override
	public SimpleView Convert(Cursor cursor) {
		SimpleView simpleView = new SimpleView();
		simpleView.id = cursor.getInt(0);
		simpleView.title = cursor.getString(1);
		return simpleView;
	}

}
