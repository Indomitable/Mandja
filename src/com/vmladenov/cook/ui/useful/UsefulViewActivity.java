package com.vmladenov.cook.ui.useful;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.IOnImageDownload;
import com.vmladenov.cook.domain.BigViewItem;

public class UsefulViewActivity extends Activity {
	private int id;
	private int category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usefulview);

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("USEFUL");
		id = bundle.getInt("id");
		category = bundle.getInt("category");
		BigViewItem view = null;
		switch (category) {
		case 0: // Advice
			view = Helpers.getDataHelper().AdvicesRepository.getBigViewItem(id);
			break;
		case 1: // Spice
			view = Helpers.getDataHelper().SpicesRepository.getBigViewItem(id);
			break;
		case 2: // Product
			view = Helpers.getDataHelper().ProductsRepository.getBigViewItem(id);
			break;
		case 3: // Dictionary
			view = Helpers.getDataHelper().DictionaryRepository.getBigViewItem(id);
			break;
		default:
			break;
		}
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
				public void ReceiveImage(Drawable draw) {
					description.setCompoundDrawablesWithIntrinsicBounds(null, draw, null, null);
				}
			}, this, view.getImageUrl());
		}
	}

}
