package com.vmladenov.cook.domain;

public class BigViewItem extends PreviewListItem 
{
	private String imageUrl;
	
	private String description;

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
