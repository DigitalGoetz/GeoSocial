package com.digitalgoetz;

import org.apache.log4j.Logger;

import com.digitalgoetz.concurrent.ConcurrentTweetList;
import com.digitalgoetz.extractors.ImageExtractor;
import com.digitalgoetz.extractors.UrlExtractor;
import com.digitalgoetz.social.Tweet;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class Stream {

	private class StreamListener implements StatusListener {

		ConcurrentTweetList tweets;
		String queryString;

		public StreamListener(String queryString, ConcurrentTweetList tweets) {
			this.tweets = tweets;
			this.queryString = queryString;

		}

		@Override
		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onException(Exception ex) {
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

		@Override
		public void onStatus(Status status) {
			log.debug("Tweet Encountered (" + tweets.size() + " found)");

			if (queryString != null) {
				if (status.getText().contains(queryString)) {
					createTweet(tweets, status);
				}
			} else {
				createTweet(tweets, status);
			}
		}

		@Override
		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			// TODO Auto-generated method stub

		}

	}

	Logger log = Logger.getLogger(getClass());

	private final TwitterStream twitterStream;
	ConcurrentTweetList tweets;

	public Stream(String queryString, ConcurrentTweetList tweets) {
		twitterStream = TwitterStreamFactory.getSingleton();
		twitterStream.addListener(new StreamListener(queryString, tweets));
	}

	private void createTweet(ConcurrentTweetList tweets, Status source) {
		final UrlExtractor urlExtractor = new UrlExtractor();
		final ImageExtractor imageExtractor = new ImageExtractor();

		tweets.add(new Tweet(source, urlExtractor, imageExtractor));
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