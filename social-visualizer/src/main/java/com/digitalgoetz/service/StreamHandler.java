package com.digitalgoetz.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.digitalgoetz.concurrent.ConcurrentTweetList;
import com.digitalgoetz.extractors.ImageExtractor;
import com.digitalgoetz.extractors.UrlExtractor;
import com.digitalgoetz.social.Tweet;
import com.google.gson.Gson;

@Path("api")
public class StreamHandler {

	Logger log = Logger.getLogger(getClass());

	ConcurrentTweetList tweets = ConcurrentTweetList.getInstance();
	UrlExtractor urlExtractor = new UrlExtractor();
	ImageExtractor imageExtractor = new ImageExtractor();

	@Path("test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getTest() {
		log.debug("GET: test");
		return "test success: " + tweets.size();
	}

	@Path("tweets")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTweets() {
		log.debug("GET: tweets");

		final List<Tweet> rawList = tweets.getList();
		final List<Tweet> filteredList = new ArrayList<>();

		for (final Tweet tweet : rawList) {
			boolean urlFlag = false;
			boolean geoFlag = false;

			final String url = urlExtractor.extractUrl(tweet.getText());
			if (url != null) {
				urlFlag = true;
				tweet.setUrl(url);

				if (imageExtractor.urlContainsImage(url)) {
					tweet.setUrlIsImage(true);
				}
			}

			if (tweet.isLocationDefined()) {
				geoFlag = true;
			}

			if (urlFlag && geoFlag) {
				filteredList.add(tweet);
			}
		}

		log.debug("Found: " + filteredList.size() + " tweets with geo + images");

		final Gson gson = new Gson();
		gson.toJson(filteredList);
		return Response.ok(gson.toJson(filteredList)).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

}
