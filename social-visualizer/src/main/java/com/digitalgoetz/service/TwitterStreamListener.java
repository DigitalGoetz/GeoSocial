package com.digitalgoetz.service;

import com.digitalgoetz.extractors.ImageExtractor;
import com.digitalgoetz.extractors.UrlExtractor;
import com.digitalgoetz.twitter.Tweet;
import com.digitalgoetz.twitter.TweetBuilder;
import com.digitalgoetz.twitter.TweetCollection;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TwitterStreamListener implements StatusListener {

	TweetCollection tweets;
	String queryString;

	public TwitterStreamListener(String queryString, TweetCollection tweets) {
		this.tweets = tweets;
		this.queryString = queryString;

	}

	private void createTweet(TweetCollection tweets, Status source) {
		final UrlExtractor urlExtractor = new UrlExtractor();
		final ImageExtractor imageExtractor = new ImageExtractor();

		final Tweet tweet = new TweetBuilder().buildTweet(source, urlExtractor, imageExtractor);
		tweets.add(tweet);
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

	}

	@Override
	public void onException(Exception ex) {

	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {

	}

	@Override
	public void onStallWarning(StallWarning warning) {

	}

	@Override
	public void onStatus(Status status) {
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

	}

}