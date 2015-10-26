package com.digitalgoetz;

import java.util.Date;

import org.apache.log4j.Logger;

import com.digitalgoetz.concurrent.ConcurrentTweetList;

public class StreamCleaner implements Runnable {

	ConcurrentTweetList tweets;
	Logger log = Logger.getLogger(getClass());

	public StreamCleaner(ConcurrentTweetList tweets) {
		this.tweets = tweets;
	}

	@Override
	public void run() {
		final Date curr = new Date();
		tweets.clearList(curr, 60 * 1000);
	}

}
