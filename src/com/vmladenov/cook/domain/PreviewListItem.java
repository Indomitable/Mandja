package com.vmladenov.cook.domain;

import android.graphics.drawable.BitmapDrawable;

public class PreviewListItem
{
	private int id;
	private String title;
	private String thumbnailUrl;
	private BitmapDrawable cachedImage;

	@Override
	public String toString() {
		return title;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getThumbnailUrl()
	{
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl)
	{
		this.thumbnailUrl = thumbnailUrl;
	}

	public BitmapDrawable getCachedImage()
	{
		return cachedImage;
	}

	public void setCachedImage(BitmapDrawable cachedImage)
	{
		this.cachedImage = cachedImage;
	}
}