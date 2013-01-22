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

package com.vmladenov.cook.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.vmladenov.cook.R;

public class SettingsActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		LoadPreferences();
	}

	private void LoadPreferences() {
		SharedPreferences preferences = getSharedPreferences("MandjaSettings", MODE_PRIVATE);

		CheckBox cbDownloadImages = (CheckBox) findViewById(R.id.cbDownloadImages);
		cbDownloadImages.setChecked(preferences.getBoolean("DownloadImages", true));

		CheckBox cbCacheImages = (CheckBox) findViewById(R.id.cbCacheImages);
		cbCacheImages.setChecked(preferences.getBoolean("CacheImages", true));

		CheckBox cbDownloadOnlyOnWiFi = (CheckBox) findViewById(R.id.cbDownloadOnlyOnWiFi);
		cbDownloadOnlyOnWiFi.setChecked(preferences.getBoolean("DownloadOnlyOnWiFi", false));

		CheckBox cbDownloadThumbnails = (CheckBox) findViewById(R.id.cbDownloadThumbnails);
		cbDownloadThumbnails.setChecked(preferences.getBoolean("DownloadThumbnails", true));

		CheckBox cbDownloadBigImages = (CheckBox) findViewById(R.id.cbDownloadBigImages);
		cbDownloadBigImages.setChecked(preferences.getBoolean("DownloadBigImages", true));

		applyDownloadImages(cbDownloadImages.isChecked());

		cbDownloadImages.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				applyDownloadImages(isChecked);
			}
		});
	}

	private void applyDownloadImages(boolean isChecked)
	{
		CheckBox cbCacheImages = (CheckBox) findViewById(R.id.cbCacheImages);
		CheckBox cbDownloadOnlyOnWiFi = (CheckBox) findViewById(R.id.cbDownloadOnlyOnWiFi);
		CheckBox cbDownloadThumbnails = (CheckBox) findViewById(R.id.cbDownloadThumbnails);
		CheckBox cbDownloadBigImages = (CheckBox) findViewById(R.id.cbDownloadBigImages);

		cbCacheImages.setEnabled(isChecked);
		cbDownloadOnlyOnWiFi.setEnabled(isChecked);
		cbDownloadThumbnails.setEnabled(isChecked);
		cbDownloadBigImages.setEnabled(isChecked);

		if (!isChecked) {
			cbCacheImages.setChecked(false);
			cbDownloadOnlyOnWiFi.setChecked(false);
			cbDownloadThumbnails.setChecked(false);
			cbDownloadBigImages.setChecked(false);
		}
	}

	public void onSavePreferences(View v) {
		SharedPreferences preferences = getSharedPreferences("MandjaSettings", MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();

		CheckBox cbDownloadImages = (CheckBox) findViewById(R.id.cbDownloadImages);
		editor.putBoolean("DownloadImages", cbDownloadImages.isChecked());

		CheckBox cbCacheImages = (CheckBox) findViewById(R.id.cbCacheImages);
		editor.putBoolean("CacheImages", cbCacheImages.isChecked());

		CheckBox cbDownloadOnlyOnWiFi = (CheckBox) findViewById(R.id.cbDownloadOnlyOnWiFi);
		editor.putBoolean("DownloadOnlyOnWiFi", cbDownloadOnlyOnWiFi.isChecked());

		CheckBox cbDownloadThumbnails = (CheckBox) findViewById(R.id.cbDownloadThumbnails);
		editor.putBoolean("DownloadThumbnails", cbDownloadThumbnails.isChecked());

		CheckBox cbDownloadBigImages = (CheckBox) findViewById(R.id.cbDownloadBigImages);
		editor.putBoolean("DownloadBigImages", cbDownloadBigImages.isChecked());

		editor.commit();

		Intent intent = new Intent();
		intent.setClass(SettingsActivity.this, MainActivity.class);
		startActivity(intent);
	}
}