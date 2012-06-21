package com.vmladenov.cook.core.loaders;

import android.content.Context;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.db.AdvicesRepository;

public class AdvicesLoader extends IdLoader {
    public AdvicesLoader(Context context, long categoryId) {
        // DataHelper helper = new DataHelper(context);
        AdvicesRepository repository = Helpers.getDataHelper().AdvicesRepository;
        repository.setCategoryId(categoryId);
        super.setRepository(repository);
    }
}
