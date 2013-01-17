package com.vmladenov.cook.core.db;

import com.vmladenov.cook.core.GlobalStrings;

public class ProductsRepository extends BaseRepository {

	public ProductsRepository(String dbPath) {
		super(dbPath);
	}

	@Override
	protected String getTableName() {
		return GlobalStrings.PRODUCTS_TABLE_NAME;
	};

}
