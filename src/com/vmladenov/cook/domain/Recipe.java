package com.vmladenov.cook.domain;

public final class Recipe extends BigViewItem {
	private String products;

	private Boolean isFavorite;

	private String userNotes;

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public Boolean getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public String getUserNotes() {
		return userNotes;
	}

	public void setUserNotes(String userNotes) {
		this.userNotes = userNotes;
	}

}
