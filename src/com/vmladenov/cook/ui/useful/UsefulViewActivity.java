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

package com.vmladenov.cook.ui.useful;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.IOnImageDownload;
import com.vmladenov.cook.core.db.BaseRepository;
import com.vmladenov.cook.domain.BigViewItem;

public class UsefulViewActivity extends Activity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usefulview);

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("USEFUL");
        int id = bundle.getInt("id");
        int category = bundle.getInt("category");
		BigViewItem view;
		BaseRepository repository;
		switch (category) {
			case 0: // Advice
				repository = Helpers.getDataHelper().getAdvicesRepository();
				break;
			case 1: // Spice
				repository = Helpers.getDataHelper().getSpicesRepository();
				break;
			case 2: // Product
				repository = Helpers.getDataHelper().getProductsRepository();
				break;
			default:
				return;
		}
		view = repository.getBigViewItem(id);
		if (view != null)
			showUseful(view);
	}

	private void showUseful(BigViewItem view) {
		TextView title = (TextView) findViewById(R.id.txtTitle);
		final TextView description = (TextView) findViewById(R.id.txtDescription);

		title.setText(view.getTitle());
		description.setText(view.getDescription());
		SharedPreferences preferences = this.getSharedPreferences("MandjaSettings", Context.MODE_PRIVATE);
		boolean downloadBigImages = preferences.getBoolean("DownloadBigImages", true);
		if (downloadBigImages) {
			Helpers.setImageFromUrlAsync(new IOnImageDownload() {

				@Override
				public void ReceiveImage(BitmapDrawable draw) {
                    Bitmap bitmap = draw.getBitmap();
                    draw.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
					description.setCompoundDrawables(null, draw, null, null);
				}
			}, this, view.getImageUrl());
		}
	}

}
