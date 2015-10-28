package com.digitalgoetz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.digitalgoetz.twitter.Tweet;
import com.digitalgoetz.twitter.TweetCollection;
import com.digitalgoetz.twitter.TweetList;

@Path("api")
public class RestHandler {

	Logger log = Logger.getLogger(getClass());

	TweetCollection tweets = TweetCollection.getInstance();

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

		final List<Tweet> list = tweets.getList();
		final List<Tweet> filteredList = new ArrayList<>();

		for (final Tweet tweet : list) {
			final Map<String, String> meta = tweet.getMeta();
			if (meta.containsKey("latitude") && meta.containsKey("longitude")) {
				filteredList.add(tweet);
			}
		}

		log.debug("Found: " + filteredList.size() + " tweets with geo + images");

		final TweetList jsonTweets = new TweetList(filteredList);
		return Response.ok(jsonTweets).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
}
