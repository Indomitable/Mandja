package com.vmladenov.cook.core.loaders;

import com.vmladenov.cook.core.Helpers;

public class SpicesLoader extends IdLoader {
	public SpicesLoader() {
		super.setRepository(Helpers.getDataHelper().getSpicesRepository());
	}

}
