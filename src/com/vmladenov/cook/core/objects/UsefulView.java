package com.vmladenov.cook.core.objects;

import android.graphics.Bitmap;

public class UsefulView {
    private long id;
    private Bitmap bigImage;
    private String title;
    private String description;
    private String imageUrl;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setBigImage(Bitmap bigImage) {
        this.bigImage = bigImage;
    }

    public Bitmap getBigImage() {
        return bigImage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
