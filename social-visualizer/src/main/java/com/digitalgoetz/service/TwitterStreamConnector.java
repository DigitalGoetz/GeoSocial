package com.digitalgoetz.service;

import org.apache.log4j.Logger;

import com.digitalgoetz.twitter.TweetCollection;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TwitterStreamConnector {

	Logger log = Logger.getLogger(getClass());

	private final TwitterStream twitterStream;
	TweetCollection tweets;

	public TwitterStreamConnector(String queryString, TweetCollection tweets) {
		twitterStream = TwitterStreamFactory.getSingleton();
		twitterStream.addListener(new TwitterStreamListener(queryString, tweets));
	}

	public synchronized void start() {
		if (twitterStream != null) {
			twitterStream.sample();
		}
	}

	public synchronized void stop() {
		if (twitterStream != null) {
			twitterStream.shutdown();
		}
	}
}