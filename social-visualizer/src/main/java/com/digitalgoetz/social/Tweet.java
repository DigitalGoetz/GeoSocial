package com.digitalgoetz.social;

import java.util.Date;

import twitter4j.Status;

public class Tweet extends SocialMessage {

	Status source;

	public Tweet(Status source) {
		this.source = source;
	}

	@Override
	public Date getDate() {
		return source.getCreatedAt();
	}

	@Override
	public Network getNetwork() {
		return Network.TWITTER;
	}

}
