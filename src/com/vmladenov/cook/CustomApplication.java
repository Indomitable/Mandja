package com.vmladenov.cook;

import android.app.Application;

import com.vmladenov.cook.core.db.DataHelper;
import com.vmladenov.cook.ui.CustomCountDown;

public class CustomApplication extends Application {
	private static CustomApplication _instance = null;

	public CustomApplication() {
		_instance = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		_instance.dataHelper = new DataHelper();
	}

	public int CurrentTheme;
	private DataHelper dataHelper;
	public CustomCountDown customCountDown;

	public static synchronized CustomApplication getInstance() {
		if (_instance == null) {
			_instance = new CustomApplication();
			_instance.CurrentTheme = android.R.style.Theme_Black;
		}
		return _instance;
	}

	public DataHelper getDataHelper() {
		return dataHelper;
	}

	@Override
	public void onTerminate() {
		_instance.dataHelper.close();
		super.onTerminate();
	}

}
