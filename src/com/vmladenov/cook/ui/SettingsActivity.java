package com.vmladenov.cook.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import com.vmladenov.cook.R;

public class SettingsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        LoadPreferences();
    }

    private void LoadPreferences() {
        SharedPreferences preferences = getSharedPreferences("MandjaSettings", MODE_PRIVATE);
        CheckBox checkBox = (CheckBox) findViewById(R.id.cbCacheImages);
        checkBox.setChecked(preferences.getBoolean("CacheImages", true));
    }

    public void onSavePreferences(View v) {
        SharedPreferences preferences = getSharedPreferences("MandjaSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        CheckBox checkBox = (CheckBox) findViewById(R.id.cbCacheImages);
        editor.putBoolean("CacheImages", checkBox.isChecked());
        editor.commit();

        Intent intent = new Intent();
        intent.setClass(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}