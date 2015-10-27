package com.digitalgoetz.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.digitalgoetz.concurrent.ConcurrentTweetList;
import com.digitalgoetz.social.Tweet;

@Path("api")
public class StreamHandler {

	Logger log = Logger.getLogger(getClass());

	ConcurrentTweetList tweets = ConcurrentTweetList.getInstance();

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

		log.debug("Found: " + list.size() + " tweets with geo + images");

		final String json = listToJson(list);
		log.debug("returning: " + json);
		return Response.ok(json).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	private String listToJson(List<Tweet> list) {

		final StringBuilder sb = new StringBuilder();

		sb.append("{ 'tweets' : [");

		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).toString());
			if (i != (list.size() - 1)) {
				sb.append(",");
			}

		}

		sb.append("] }");

		return sb.toString();
	}

}
