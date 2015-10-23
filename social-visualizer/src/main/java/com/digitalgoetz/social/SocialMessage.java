package com.digitalgoetz.social;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitalgoetz.Image;

public abstract class SocialMessage {

	Network network;

	Date timestamp;
	List<Image> images;
	Map<String, String> meta;

	public enum Network {
		TWITTER, INSTAGRAM;
	}

	public abstract Network getNetwork();

	public abstract Date getDate();

}
