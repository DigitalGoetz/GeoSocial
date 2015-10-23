package com.digitalgoetz;

public class Image {

	ImageType type;
	String url;

	public enum ImageType {
		DIRECT_URL, INDIRECT_URL
	}

	public ImageType getType() {
		return type;
	}

	public void setType(ImageType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
