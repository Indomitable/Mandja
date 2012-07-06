package com.vmladenov.cook.core.loaders;

import android.content.Context;

import com.vmladenov.cook.core.Helpers;

public class AdvicesLoader extends IdLoader {
	public AdvicesLoader(Context context) {
		super.setRepository(Helpers.getDataHelper().getAdvicesRepository());
	}
}
