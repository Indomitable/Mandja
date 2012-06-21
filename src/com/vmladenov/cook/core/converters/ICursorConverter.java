package com.vmladenov.cook.core.converters;

import android.database.Cursor;

public interface ICursorConverter<T> {
    T Convert(Cursor cursor);
}
