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

package com.vmladenov.cook.ui.recipes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.IOnImageDownload;
import com.vmladenov.cook.core.OnSlideListener;
import com.vmladenov.cook.core.SlideGestureDetector;
import com.vmladenov.cook.core.db.RecipesRepository;
import com.vmladenov.cook.domain.Recipe;

public final class ViewRecipeActivity extends Activity implements OnSlideListener {
	Recipe recipe = null;
	private SlideGestureDetector detector;
	private ViewFlipper flipper;
	private RecipesRepository repository;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipeview);
		this.repository = Helpers.getDataHelper().getRecipesRepository();
		this.detector = new SlideGestureDetector(this, this);
		this.flipper = (ViewFlipper) findViewById(R.id.flipper);
		View.OnTouchListener onTouchListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return detector.onTouchEvent(event);
			}
		};
		ScrollView scroller = (ScrollView) findViewById(R.id.scrollView1);
		scroller.setOnTouchListener(onTouchListener);
		changeControlsVisibility(View.INVISIBLE);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("RECIPE_ID");
		int id = bundle.getInt("id");
		loadRecipe(id);
	}

	static Handler loadRecipeHandler = new Handler();

	private void loadRecipe(final int id) {
		Thread readRecipeThread = new Thread(new Runnable() {
			@Override
			public void run() {
				recipe = repository.getRecipe(id);
				loadRecipeHandler.post(new Runnable() {

					@Override
					public void run() {
						showRecipe();
					}
				});
			}
		});
		readRecipeThread.start();
	}

	private void changeControlsVisibility(int visibility) {
		TextView title = (TextView) findViewById(R.id.txtRecipeTitle);
		TextView products = (TextView) findViewById(R.id.txtRecipeProducts);
		TextView description = (TextView) findViewById(R.id.txtRecipeDescription);
		ImageView image = (ImageView) findViewById(R.id.imgRecipePicture);

		title.setVisibility(visibility);
		products.setVisibility(visibility);
		description.setVisibility(visibility);
		image.setVisibility(visibility);
	}

	private void showRecipe() {
		if (recipe == null)
			return;

		TextView title = (TextView) findViewById(R.id.txtRecipeTitle);
		TextView products = (TextView) findViewById(R.id.txtRecipeProducts);
		TextView description = (TextView) findViewById(R.id.txtRecipeDescription);
        TextView prepareTime = (TextView) findViewById(R.id.txtPrepareTime);
        TextView cookTime = (TextView) findViewById(R.id.txtCookTime);
        TextView portions = (TextView) findViewById(R.id.txtPortions);
		final ImageView image = (ImageView) findViewById(R.id.imgRecipePicture);

		title.setText(recipe.getTitle());
		products.setText(recipe.getProducts());
		description.setText(recipe.getDescription());

        if (!recipe.getPrepareTime().isEmpty()){
            prepareTime.setText(getString(R.string.prepare_time) + " " + recipe.getPrepareTime());
            prepareTime.setVisibility(View.VISIBLE);
        }

        if (!recipe.getCookTime().isEmpty()){
            cookTime.setText(getString(R.string.cook_time) + " " + recipe.getCookTime());
            cookTime.setVisibility(View.VISIBLE);
        }

        if (!recipe.getPortions().isEmpty()){
            portions.setText(getString(R.string.portions) + " " + recipe.getPortions());
            portions.setVisibility(View.VISIBLE);
        }

		SharedPreferences preferences = this.getSharedPreferences("MandjaSettings", Context.MODE_PRIVATE);
		boolean downloadBigImages = preferences.getBoolean("DownloadBigImages", true);
		if (downloadBigImages) {
			Helpers.setImageFromUrlAsync(new IOnImageDownload() {
				@Override
				public void ReceiveImage(BitmapDrawable draw) {
					image.setImageDrawable(draw);
				}
			}, this, recipe.getImageUrl());
		}
		changeControlsVisibility(View.VISIBLE);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return recipe;
	}

	public void onFavoriteClick() {
		if (recipe != null && recipe.getId() > 0) {
			recipe.setIsFavorite(!recipe.getIsFavorite());
			if (recipe.getIsFavorite()) {
				repository.addFavorite(recipe.getId());
			} else {
				repository.removeFavorite(recipe.getId());
			}
		}
	}

	public void onViewNotes() {
		if (recipe != null && recipe.getId() > 0) {
			final AlertDialog alertDialog;

			LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.recipenotes, null);

			final EditText edit = (EditText) layout.findViewById(R.id.txtEditNote);
			edit.setText(recipe.getUserNotes());

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);

			alertDialog = builder.create();

			Button button = (Button) layout.findViewById(R.id.bOk);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					recipe.setUserNotes(edit.getText().toString());
					repository.setRecipeNote(recipe.getId(), recipe.getUserNotes());
					alertDialog.dismiss();
				}
			});
			alertDialog.show();
		}
	}

	// public void onRate()
	// {
	// if (recipe != null && recipe.getId() > 0)
	// {
	// LayoutInflater inflater = (LayoutInflater)
	// this.getSystemService(LAYOUT_INFLATER_SERVICE);
	// View layout = inflater.inflate(R.layout.recipe_rating_dialog, null);
	//
	// final RatingBar ratingBar = (RatingBar)
	// layout.findViewById(R.id.rbRecipeRating);
	// ratingBar.setRating(recipe.getRating());
	//
	// final Dialog dialog = new Dialog(this);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(layout);
	//
	// Button button = (Button) layout.findViewById(R.id.bOk);
	// button.setOnClickListener(new OnClickListener()
	// {
	//
	// @Override
	// public void onClick(View v)
	// {
	// recipe.setRating(ratingBar.getRating());
	// dialog.dismiss();
	// }
	// });
	// dialog.show();
	// }
	// }

	public void onShare() {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, recipe.getTitle());
		shareIntent.putExtra(Intent.EXTRA_TEXT, recipe.getProducts() + "\n" + recipe.getDescription());
		startActivity(Intent.createChooser(shareIntent, this.getString(R.string.share_title)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.recipe_view_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.mi_favorite:
				onFavoriteClick();
				return true;
			case R.id.mi_notes:
				onViewNotes();
				return true;
			case R.id.mi_share:
				onShare();
				return true;
				// case R.id.mi_rate:
				// onRate();
				// return true;
			default:
				return false;
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (recipe == null)
			return false;
		MenuItem miFavorite = menu.findItem(R.id.mi_favorite);
		MenuItem miNotes = menu.findItem(R.id.mi_notes);
		MenuItem miRate = menu.findItem(R.id.mi_rate);
		miRate.setVisible(false);

		Boolean isFavorite = recipe.getIsFavorite() == null ? false : recipe.getIsFavorite();
		Boolean hasNote = !(recipe.getUserNotes() == null || recipe.getUserNotes().isEmpty());
		if (isFavorite) {
			miFavorite.setIcon(R.drawable.remove_favorite);
		} else {
			miFavorite.setIcon(R.drawable.add_favorite);
		}

		if (hasNote) {
			miNotes.setIcon(R.drawable.notes);
		} else {
			miNotes.setIcon(R.drawable.nonotes);
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	final long animationDuration = 150;

	private Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(animationDuration);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(animationDuration);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	private Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(animationDuration);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	private Animation outToRightAnimation() {
		Animation outtoRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(animationDuration);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	@Override
	public void onSlideLeft() {
		flipper.setInAnimation(inFromLeftAnimation());
		flipper.setOutAnimation(outToRightAnimation());
		flipper.showPrevious();
	}

	@Override
	public void onSlideRight() {
		flipper.setInAnimation(inFromRightAnimation());
		flipper.setOutAnimation(outToLeftAnimation());
		flipper.showNext();
	}
}
