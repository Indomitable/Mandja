package com.vmladenov.cook.core.html;

import android.graphics.Bitmap;

public final class SmallPreview {
    public long id;
    public String title;
    public String imageUrl;
    public String bigViewUrl;
    public Bitmap cachedImage;
    public String description;
    public Boolean isFavorite;

    @Override
    public String toString() {
        return title;
    }
}
