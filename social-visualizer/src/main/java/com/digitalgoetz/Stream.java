package com.digitalgoetz;

import com.digitalgoetz.concurrent.ConcurrentTweetList;
import com.digitalgoetz.social.Tweet;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class Stream {

	private TwitterStream twitterStream;
	ConcurrentTweetList tweets;

	public Stream(String queryString, ConcurrentTweetList tweets) {
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

		ConcurrentTweetList tweets;
		String queryString;

		public StreamListener(String queryString, ConcurrentTweetList tweets) {
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
					tweets.add(new Tweet(status));
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