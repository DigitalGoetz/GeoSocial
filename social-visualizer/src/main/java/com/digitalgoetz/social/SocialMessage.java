package com.digitalgoetz.social;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitalgoetz.Image;

public class SocialMessage implements Message {

	Network network;

	Date timestamp;
	List<Image> images;
	Map<String, String> meta;

	public SocialMessage(Network network) {
		this.network = network;
	}

	public enum Network {
		TWITTER, INSTAGRAM;
	}

	@Override
	public Date getDate() {
		// TODO Auto-generated method stub
		return null;
	}

}
