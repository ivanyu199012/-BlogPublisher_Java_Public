package com.blog.publish.publisher;

import java.io.Serializable;

public class BlogInfo implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = -8812812866344618999L;
	public enum SITE {
		DEVTO, MEDIUM
	};

	public enum PUBLISH_STATUS {
		DRAFT("draft", "false"), UNLISTED("unlisted", null), PUBLIC("public", "true");

		public final String mediumStatus;
		public final String devToStatus;

		private PUBLISH_STATUS(String mediumStatus, String devToStatus) {
			this.mediumStatus = mediumStatus;
			this.devToStatus = devToStatus;
		}
	};

	private String filepath;
	private String title;
	private String canonicalUrl;
	private String series;
	private SITE[] sites;
	private String[] tags;
	private PUBLISH_STATUS publishStatus = PUBLISH_STATUS.DRAFT;
	private String imageUrl;
	private String subtitle;

	public String getFilepath()
	{
		return filepath;
	}
	public void setFilepath( String filepath )
	{
		this.filepath = filepath;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle( String title )
	{
		this.title = title;
	}
	public String getCanonicalUrl()
	{
		return canonicalUrl;
	}
	public void setCanonicalUrl( String canonicalUrl )
	{
		this.canonicalUrl = canonicalUrl;
	}

	public PUBLISH_STATUS getPublishStatus()
	{
		return publishStatus;
	}
	public void setPublishStatus( PUBLISH_STATUS publishStatus )
	{
		this.publishStatus = publishStatus;
	}
	public String getImageUrl()
	{
		return imageUrl;
	}
	public void setImageUrl( String imageUrl )
	{
		this.imageUrl = imageUrl;
	}
	public String getSubtitle()
	{
		return subtitle;
	}
	public void setSubtitle( String subtitle )
	{
		this.subtitle = subtitle;
	}
	public SITE[] getSites()
	{
		return sites;
	}
	public void setSites( SITE[] sites )
	{
		this.sites = sites;
	}
	public String getSeries()
	{
		return series;
	}
	public void setSeries( String series )
	{
		this.series = series;
	}
	public String[] getTags()
	{
		return tags;
	}
	public void setTags( String[] tags )
	{
		this.tags = tags;
	}
}
