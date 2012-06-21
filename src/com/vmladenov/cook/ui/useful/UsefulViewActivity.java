package com.vmladenov.cook.ui.useful;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.vmladenov.cook.R;
import com.vmladenov.cook.core.Helpers;
import com.vmladenov.cook.core.objects.UsefulView;

public class UsefulViewActivity extends Activity {
    private long id;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usefulview);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("USEFUL");
        id = bundle.getLong("id");
        category = bundle.getInt("category");
        UsefulView view = null;
        switch (category) {
            case 0: // Advice
                view = Helpers.getDataHelper().AdvicesRepository.getAdviceView(id);
                break;
            case 1: // Spice
                view = Helpers.getDataHelper().SpicesRepository.getSpiceView(id);
                break;
            case 2: // Product
                view = Helpers.getDataHelper().ProductsRepository.getProductView(id);
                break;
            case 3: // Dictionary
                view = Helpers.getDataHelper().DictionaryRepository.getDictionaryView(id);
                break;
            default:
                break;
        }
        if (view != null) ShowUseful(view);
    }

    private void ShowUseful(UsefulView view) {
        TextView title = (TextView) findViewById(R.id.txtTitle);
        TextView description = (TextView) findViewById(R.id.txtDescription);
        final ImageView image = (ImageView) findViewById(R.id.imgPicture);

        title.setText(view.getTitle());
        description.setText(view.getDescription());
        Helpers.setImageFromUrlAsync(image, view.getImageUrl());
        // image.setImageBitmap(view.getBigImage());
    }

}
