package com.digitalgoetz;

import com.digitalgoetz.concurrent.ConcurrentTweetList;

public class StreamRunner implements Runnable {

	Stream stream = null;

	public StreamRunner(String queryString, ConcurrentTweetList tweets) {
		stream = new Stream(queryString, tweets);
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
