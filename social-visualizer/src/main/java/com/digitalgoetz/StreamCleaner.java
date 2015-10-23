package com.digitalgoetz;

import java.util.Date;

import com.digitalgoetz.concurrent.ConcurrentTweetList;

public class StreamCleaner implements Runnable {

	ConcurrentTweetList tweets;

	public StreamCleaner(ConcurrentTweetList tweets) {
		this.tweets = tweets;
	}

	@Override
	public void run() {
		tweets.clearList(new Date(), 1);
	}

}
