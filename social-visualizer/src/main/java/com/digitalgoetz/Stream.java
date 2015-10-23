package com.digitalgoetz;


import java.util.ArrayList;

import java.util.List;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class Stream {
	List<Status> tweets;

	private TwitterStream twitterStream;

	public Stream(String queryString) {
		tweets = new ArrayList<>();
		twitterStream = TwitterStreamFactory.getSingleton();
		twitterStream.addListener(new StreamListener(queryString, tweets));
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

	private class StreamListener implements StatusListener {

		List<Status> tweets;
		String queryString;

		public StreamListener(String queryString, List<Status> tweets) {
			this.tweets = tweets;
			this.queryString = queryString;

		}

		@Override
		public void onException(Exception ex) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatus(Status status) {
			if (queryString != null) {
				if (status.getText().contains(queryString)) {
					tweets.add(status);
				}
			}
		}

		@Override
		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScrubGeo(long userId, long upToStatusId) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStallWarning(StallWarning warning) {
			// TODO Auto-generated method stub

		}

	}
}