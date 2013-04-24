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
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.IOnImageDownload;
import com.vmladenov.cook.core.objects.MainActivityState;
import com.vmladenov.cook.domain.Recipe;
import com.vmladenov.cook.ui.recipes.AllRecipesActivity;
import com.vmladenov.cook.ui.recipes.FavoriteRecipesActivity;
import com.vmladenov.cook.ui.recipes.RecipeCategoriesActivity;
import com.vmladenov.cook.ui.recipes.ViewRecipeActivity;
import com.vmladenov.cook.ui.useful.AdvicesActivity;
import com.vmladenov.cook.ui.useful.ProductsActivity;
import com.vmladenov.cook.ui.useful.SpicesActivity;

public final class MainActivity extends Activity
{
	private Boolean loadingRecipeOfTheDay;
	Recipe recipeOfTheDay = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        Helpers.getDataHelper().initUserDb(MainActivity.this);
		if (!Helpers.getDataHelper().checkDb(MainActivity.this)){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, CopyDatabaseActivity.class);
            startActivity(intent);
            return;
        }

		MainActivityState state = (MainActivityState) getLastNonConfigurationInstance();
		if (state == null || state.recipe == null)
			getRecipeOfTheDay();
		else
			showRecipeOfTheDay(state.recipe);
	}

	private void getRecipeOfTheDay()
	{
		loadingRecipeOfTheDay = true;
		Thread initializeThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				recipeOfTheDay = Helpers.getDataHelper().getRecipesRepository().getRecipeOfTheDay();
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						showRecipeOfTheDay(recipeOfTheDay);
					}
				});
			}
		});
		initializeThread.start();
	}

	private void showRecipeOfTheDay(Recipe recipe)
	{
		if (recipe == null)
			return;

		final TextView txtRecipeContent = (TextView) findViewById(R.id.txtRecipeContent);
        StringBuilder builder = new StringBuilder();
        builder.append(recipe.getTitle());
        if (!recipe.getPrepareTime().isEmpty()) {
            builder.append("\n");
            builder.append(getText(R.string.prepare_time) + " " + recipe.getPrepareTime());
        }
        if (!recipe.getCookTime().isEmpty()) {
            builder.append("\n");
            builder.append(getText(R.string.cook_time) + " " + recipe.getCookTime());
        }
        if (!recipe.getPortions().isEmpty()) {
            builder.append("\n");
            builder.append(getText(R.string.portions) + " " + recipe.getPortions());
        }
        builder.append("\n");
        builder.append(getText(R.string.products) + ":");
        builder.append("\n");
        builder.append(recipe.getProducts());
		txtRecipeContent.setText(builder.toString());
		SharedPreferences preferences = this.getSharedPreferences("MandjaSettings", Context.MODE_PRIVATE);
		boolean downloadBigImages = preferences.getBoolean("DownloadBigImages", true);
		if (downloadBigImages) {
			Helpers.setImageFromUrlAsync(new IOnImageDownload()
			{
				@Override
				public void ReceiveImage(BitmapDrawable draw)
				{
                    ImageView img = (ImageView)findViewById(R.id.imgRecipeOfTheDay);
                    if (img == null) {
                        txtRecipeContent.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null);
                    } else {
                        img.setImageDrawable(draw);
                    }
				}
			}, this, recipe.getImageUrl());
		}
		loadingRecipeOfTheDay = false;
	}

	public void onLatestRecipeClick(View v)
	{
		if (loadingRecipeOfTheDay || recipeOfTheDay == null)
			return;
		Bundle bundle = new Bundle();
		bundle.putInt("id", recipeOfTheDay.getId());

		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ViewRecipeActivity.class);
		intent.putExtra("RECIPE_ID", bundle);

		startActivity(intent);
	}

	public void onAllRecipesClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, AllRecipesActivity.class);
		startActivity(intent);
	}

	public void onCategoryRecipesClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, RecipeCategoriesActivity.class);
		startActivity(intent);
	}

	public void onShowFavoriteRecipes(View v)
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, FavoriteRecipesActivity.class);
		startActivity(intent);
	}

	public void onAdvicesClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, AdvicesActivity.class);
		startActivity(intent);
	}

	public void onSpicesClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SpicesActivity.class);
		startActivity(intent);
	}

	public void onProductsClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ProductsActivity.class);
		startActivity(intent);
	}

	public void onToolsClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ToolsActivity.class);
		startActivity(intent);
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		MainActivityState state = new MainActivityState();
		state.recipe = recipeOfTheDay;
		return state;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.miAbout:
				showAbout();
				return true;
			case R.id.miSettings:
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SettingsActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onMenuItemSelected(featureId, item);
		}
	}

	private void showAbout()
	{
		final AlertDialog alertDialog;

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.about, (ViewGroup) findViewById(R.id.layout_root));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(layout);

		builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		alertDialog = builder.create();
		alertDialog.setView(layout);

		TextView txtTitleVersion = (TextView) layout.findViewById(R.id.txtTitleVersion);
		try
		{
			PackageInfo packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			txtTitleVersion.setText(String.format(getString(R.string.about_header), packageInfo.versionName));
		} catch (Exception e)
		{
		}

		TextView txtEmail = (TextView) layout.findViewById(R.id.txtAboutEmail);
		txtEmail.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				/* Create the Intent */
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

				/* Fill it with Data */
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "ventsislav.mladenov@gmail.com" });
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

				/* Send it off to the Activity-Chooser */
				startActivity(emailIntent);
				// startActivity(Intent.createChooser(emailIntent,
				// "Send mail..."));
			}
		});

		TextView txtBrowse = (TextView) layout.findViewById(R.id.txtSite);
		txtBrowse.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent viewSiteIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.gotvetesmen.com/"));
				startActivity(viewSiteIntent);
			}
		});

		Button button = (Button) layout.findViewById(R.id.bOk);
		button.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				alertDialog.dismiss();
			}
		});
		alertDialog.show();

	}
}
