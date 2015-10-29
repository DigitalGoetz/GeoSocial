package com.digitalgoetz.service;

import java.util.Date;

import org.apache.log4j.Logger;

import com.digitalgoetz.twitter.TweetCollection;

public class TwitterCleaner implements Runnable {

	TweetCollection tweets;
	Logger log = Logger.getLogger(getClass());

	public TwitterCleaner(TweetCollection tweets) {
		this.tweets = tweets;
	}

	@Override
	public void run() {
		final Date curr = new Date();
		tweets.clearList(curr, 10 * 60 * 1000);
	}

}
