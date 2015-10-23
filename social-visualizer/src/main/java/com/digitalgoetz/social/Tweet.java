package com.digitalgoetz.social;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Status;

public class Tweet extends SocialMessage {

	Status source;
	Date obtained;
	List<String> urls;

	public Tweet(Status source) {
		this.source = source;
		this.obtained = new Date();
		urls = new ArrayList<>();
	}

	@Override
	public Date getDateObtained() {
		return obtained;
	}

	@Override
	public Network getNetwork() {
		return Network.TWITTER;
	}

	public void addUrl(String url) {
		urls.add(url);
	}

	public List<String> getUrls() {
		return urls;
	}

}
