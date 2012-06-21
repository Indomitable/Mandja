package com.vmladenov.cook.core.loaders;

import android.content.Context;
import com.vmladenov.cook.core.Helpers;

public class DictionaryLoader extends IdLoader {
    public DictionaryLoader(Context context) {
        super.setRepository(Helpers.getDataHelper().DictionaryRepository);
    }
}
