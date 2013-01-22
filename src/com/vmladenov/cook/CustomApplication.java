/*
 * Copyright (C) 2011 Ventsislav Mladenov <ventsislav dot mladenov at gmail dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
}
