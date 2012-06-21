package com.vmladenov.cook.ui.recipes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.vmladenov.cook.core.OnSlideListener;
import com.vmladenov.cook.core.SlideGestureDetector;
import com.vmladenov.cook.core.html.Recipe;
import com.vmladenov.cook.core.html.RecipeReader;

public final class ViewRecipeActivity extends Activity implements OnSlideListener
{
	ProgressDialog progressDialog = null;
	Recipe recipe = null;
	private SlideGestureDetector detector;
	private ViewFlipper flipper;
	private boolean loadedById = true;

	// private Boolean hasNote;
	// private Boolean isFavorite;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipeview);
		this.detector = new SlideGestureDetector(this, this);
		this.flipper = (ViewFlipper) findViewById(R.id.flipper);
		View.OnTouchListener onTouchListener = new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return detector.onTouchEvent(event);
			}
		};
		ScrollView scroller = (ScrollView) findViewById(R.id.scrollView1);
		scroller.setOnTouchListener(onTouchListener);
		changeControlsVisibility(View.INVISIBLE);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("RECIPE_ID");
		if (bundle != null)
		{
			loadedById = true;
			long id = bundle.getLong("id");
			loadRecipe(id);
		}
		else
		{
			loadedById = false;
			bundle = intent.getBundleExtra("RECIPE_URL");
			String url = bundle.getString("url");
			loadRecipe(url);
		}
	}

	Handler loadRecipeHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			if (progressDialog != null) progressDialog.dismiss();
		}

		;
	};

	private void loadRecipe(final long id)
	{
		progressDialog = ProgressDialog.show(ViewRecipeActivity.this, getString(R.string.loadingRecipe), getString(R.string.loading), false);

		Thread readRecipeThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				recipe = Helpers.getDataHelper().RecipesRepository.getRecipe(id);
				loadRecipeHandler.post(new Runnable()
				{

					@Override
					public void run()
					{
						showRecipe();
					}
				});
				loadRecipeHandler.sendEmptyMessage(0);
			}
		});
		readRecipeThread.start();
	}

	private void loadRecipe(final String url)
	{
		progressDialog = ProgressDialog.show(ViewRecipeActivity.this, getString(R.string.loadingRecipe), getString(R.string.loading), false);

		Thread readRecipeThread = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				RecipeReader reader = new RecipeReader(url);
				recipe = reader.getRecipe();
				loadRecipeHandler.post(new Runnable()
				{

					@Override
					public void run()
					{
						showRecipe();
					}
				});
				loadRecipeHandler.sendEmptyMessage(0);
			}
		});
		readRecipeThread.start();
	}

	private void changeControlsVisibility(int visibility)
	{
		TextView title = (TextView) findViewById(R.id.txtRecipeTitle);
		TextView products = (TextView) findViewById(R.id.txtRecipeProducts);
		TextView description = (TextView) findViewById(R.id.txtRecipeDescription);
		ImageView image = (ImageView) findViewById(R.id.imgRecipePicture);

		title.setVisibility(visibility);
		products.setVisibility(visibility);
		description.setVisibility(visibility);
		image.setVisibility(visibility);
	}

	private void showRecipe()
	{
		if (recipe == null) return;

		TextView title = (TextView) findViewById(R.id.txtRecipeTitle);
		TextView products = (TextView) findViewById(R.id.txtRecipeProducts);
		TextView description = (TextView) findViewById(R.id.txtRecipeDescription);
		final ImageView image = (ImageView) findViewById(R.id.imgRecipePicture);

		title.setText(recipe.getTitle());
		products.setText(recipe.getProducts());
		String desc = recipe.getDescription() + "\n" + recipe.getNotes();
		description.setText(desc);
		Helpers.setImageFromUrlAsync(image, recipe.getImageUrl());

		changeControlsVisibility(View.VISIBLE);
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return recipe;
	}

	public void onFavoriteClick()
	{
		if (recipe != null && recipe.getId() > 0)
		{
			recipe.setIsFavorite(!recipe.getIsFavorite());
			if (recipe.getIsFavorite())
			{
				Helpers.getDataHelper().RecipesRepository.addFavorite(recipe.getId());
			}
			else
			{
				Helpers.getDataHelper().RecipesRepository.removeFavorite(recipe.getId());
			}
		}
	}

	public void onViewNotes()
	{
		if (recipe != null && recipe.getId() > 0)
		{
			final AlertDialog alertDialog;

			LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.recipenotes, null);

			final EditText edit = (EditText) layout.findViewById(R.id.txtEditNote);
			edit.setText(recipe.getUserNotes());

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);

			alertDialog = builder.create();

			Button button = (Button) layout.findViewById(R.id.bOk);
			button.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					recipe.setUserNotes(edit.getText().toString());
					Helpers.getDataHelper().RecipesRepository.setRecipeNote(recipe.getId(), recipe.getUserNotes());
					alertDialog.dismiss();
				}
			});
			alertDialog.show();
		}
	}

	public void onRate()
	{
		if (recipe != null && recipe.getId() > 0)
		{
			LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.recipe_rating_dialog, null);

			final RatingBar ratingBar = (RatingBar) layout.findViewById(R.id.rbRecipeRating);
			ratingBar.setRating(recipe.getRating());

			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(layout);

			Button button = (Button) layout.findViewById(R.id.bOk);
			button.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					recipe.setRating(ratingBar.getRating());
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}

	public void onShare()
	{
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, recipe.getTitle());
		shareIntent.putExtra(Intent.EXTRA_TEXT, recipe.getUrl());
		startActivity(Intent.createChooser(shareIntent, this.getString(R.string.share_title)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.recipe_view_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.mi_favorite:
				onFavoriteClick();
				return true;
			case R.id.mi_notes:
				onViewNotes();
				return true;
			case R.id.mi_share:
				onShare();
				return true;
			case R.id.mi_rate:
				onRate();
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		if (recipe == null) return false;
		MenuItem miFavorite = menu.findItem(R.id.mi_favorite);
		MenuItem miNotes = menu.findItem(R.id.mi_notes);
		MenuItem miRate = menu.findItem(R.id.mi_rate);
		miFavorite.setVisible(loadedById);
		miNotes.setVisible(loadedById);
		miRate.setVisible(loadedById);

		Boolean isFavorite = recipe.getIsFavorite() == null ? false : recipe.getIsFavorite();
		Boolean hasNote = recipe.getUserNotes() == null || recipe.getUserNotes().length() == 0 ? false : true;
		if (isFavorite)
		{
			miFavorite.setIcon(R.drawable.remove_favorite);
		}
		else
		{
			miFavorite.setIcon(R.drawable.add_favorite);
		}

		if (hasNote)
		{
			miNotes.setIcon(R.drawable.notes);
		}
		else
		{
			miNotes.setIcon(R.drawable.nonotes);
		}
		return true;
	}

	// region Swipe

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return this.detector.onTouchEvent(event);
	}

	final long animationDuration = 150;

	private Animation inFromRightAnimation()
	{
		Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(animationDuration);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	private Animation outToLeftAnimation()
	{
		Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(animationDuration);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	private Animation inFromLeftAnimation()
	{
		Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(animationDuration);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	private Animation outToRightAnimation()
	{
		Animation outtoRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(animationDuration);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	@Override
	public void onSlideLeft()
	{
		flipper.setInAnimation(inFromLeftAnimation());
		flipper.setOutAnimation(outToRightAnimation());
		flipper.showPrevious();
	}

	@Override
	public void onSlideRight()
	{
		flipper.setInAnimation(inFromRightAnimation());
		flipper.setOutAnimation(outToLeftAnimation());
		flipper.showNext();
	}

	// endregion

}
