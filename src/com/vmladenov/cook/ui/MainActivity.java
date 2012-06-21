package com.vmladenov.cook.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.html.Recipe;
import com.vmladenov.cook.core.objects.MainActivityState;
import com.vmladenov.cook.ui.recipes.*;
import com.vmladenov.cook.ui.useful.AdvicesCategoriesActivity;
import com.vmladenov.cook.ui.useful.DictionaryActivity;
import com.vmladenov.cook.ui.useful.ProductsActivity;
import com.vmladenov.cook.ui.useful.SpicesActivity;

public final class MainActivity extends Activity {
    private Boolean loadingRecipeOfTheDay;
    Recipe recipeOfTheDay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MainActivityState state = (MainActivityState) getLastNonConfigurationInstance();
        if (state == null || state.recipe == null)
            getRecipeOfTheDay();
        else
            showRecipeOfTheDay(state.recipe);
    }

    private void getRecipeOfTheDay() {
        loadingRecipeOfTheDay = true;
        Thread initializeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                recipeOfTheDay = Helpers.getDataHelper().RecipesRepository.getRecipeOfTheDay();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showRecipeOfTheDay(recipeOfTheDay);
                    }
                });
            }
        });
        initializeThread.start();
    }

    private void showRecipeOfTheDay(Recipe recipe) {
        if (recipe == null) return;

        TextView txtRecipeContent = (TextView) findViewById(R.id.txtRecipeContent);
        ImageView imgRecipePicture = (ImageView) findViewById(R.id.imgRecipePicture);

        txtRecipeContent.setText(recipe.getTitle());
        Helpers.setImageFromUrlAsync(imgRecipePicture, recipe.getImageUrl());
        loadingRecipeOfTheDay = false;
    }

    public void onLatestRecipeClick(View v) {
        if (loadingRecipeOfTheDay || recipeOfTheDay == null) return;
        Bundle bundle = new Bundle();
        bundle.putLong("id", recipeOfTheDay.getId());

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ViewRecipeActivity.class);
        intent.putExtra("RECIPE_ID", bundle);

        startActivity(intent);
    }

    public void onLatestRecipesClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LatestRecipesActivity.class);
        startActivity(intent);
    }

    public void onAllRecipesClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AllRecipesActivity.class);
        startActivity(intent);
    }

    public void onCategoryRecipesClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, RecipeCategoriesActivity.class);
        startActivity(intent);
    }

    public void onAdvicesClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AdvicesCategoriesActivity.class);
        startActivity(intent);
    }

    public void onSpicesClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SpicesActivity.class);
        startActivity(intent);
    }

    public void onProductsClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ProductsActivity.class);
        startActivity(intent);
    }

    public void onDictionaryClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, DictionaryActivity.class);
        startActivity(intent);
    }

    public void onShowTenRecipes(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, TenRecipesCategoriesActivity.class);
        startActivity(intent);
    }

    public void onShowFavoriteRecipes(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, FavoriteRecipesActivity.class);
        startActivity(intent);
    }

    public void onToolsClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ToolsActivity.class);
        startActivity(intent);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        MainActivityState state = new MainActivityState();
        state.recipe = recipeOfTheDay;
        return state;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
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

    private void showAbout() {
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
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0);
            txtTitleVersion.setText(String.format(getString(R.string.about_header),
                    packageInfo.versionName));
        } catch (Exception e) {
        }

        TextView txtEmail = (TextView) layout.findViewById(R.id.txtAboutEmail);
        txtEmail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /* Create the Intent */
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{"ventsislav.mladenov@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

                /* Send it off to the Activity-Chooser */
                startActivity(emailIntent);
                // startActivity(Intent.createChooser(emailIntent,
                // "Send mail..."));
            }
        });

        TextView txtBrowse = (TextView) layout.findViewById(R.id.txtSite);
        txtBrowse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent viewSiteIntent = new Intent("android.intent.action.VIEW", Uri
                        .parse("http://www.gotvetesmen.com/"));
                startActivity(viewSiteIntent);
            }
        });

        Button button = (Button) layout.findViewById(R.id.bOk);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }
}
