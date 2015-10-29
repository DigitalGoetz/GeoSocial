package com.digitalgoetz.service;

import com.digitalgoetz.twitter.TweetCollection;

public class TwitterCollector implements Runnable {

	TwitterStreamConnector stream = null;

	public TwitterCollector(String queryString, TweetCollection tweets) {
		stream = new TwitterStreamConnector(queryString, tweets);
	}

	@Override
	public void run() {
		stream.start();
	}

	public void stop() {
		if (stream != null) {
			stream.stop();
		}
	}

}
