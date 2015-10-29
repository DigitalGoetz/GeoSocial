package com.digitalgoetz.twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.digitalgoetz.extractors.Extractor;
import com.digitalgoetz.extractors.ExtractorComparator;

import twitter4j.Status;

public class TweetBuilder {

	ExtractorComparator comparator;

	public TweetBuilder() {
		comparator = new ExtractorComparator();
	}

	public Tweet buildTweet(Status source, Extractor... extractors) {
		final List<Extractor> extractorList = new ArrayList<>();
		for (final Extractor extractor : extractors) {
			extractorList.add(extractor);
		}

		Collections.sort(extractorList, Collections.reverseOrder(comparator));

		final Tweet tweet = new Tweet();
		tweet.setId(Long.toString(source.getId()));
		tweet.setObtained(new Date());

		final Map<String, String> meta = tweet.getMeta();

		meta.put("text", source.getText());

		if (source.getGeoLocation() != null) {
			meta.put("latitude", Double.toString(source.getGeoLocation().getLatitude()));
			meta.put("longitude", Double.toString(source.getGeoLocation().getLongitude()));
		}

		for (final Extractor extractor : extractorList) {
			extractor.extract(source, meta);
		}

		return tweet;
	}

}
