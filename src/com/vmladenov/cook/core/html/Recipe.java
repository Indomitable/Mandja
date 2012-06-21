package com.vmladenov.cook.core.html;

public final class Recipe {
    private long id;
    private long recipeId;
    private String url;
    private String title;
    private String imageUrl;
    private String products;
    private String description;
    private String notes;
    private String date;
    private Boolean isFavorite;
    private String userNotes;
    private float rating;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getProducts() {
        return products == null ? "" : products;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes == null ? "" : notes;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setUserNotes(String userNotes) {
        this.userNotes = userNotes;
    }

    public String getUserNotes() {
        return userNotes;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }
}
