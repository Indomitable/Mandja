package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.core.Helpers;

public class ProductsLoader extends IdLoader {
    public ProductsLoader() {
        super.setRepository(Helpers.getDataHelper().ProductsRepository);
    }
}
