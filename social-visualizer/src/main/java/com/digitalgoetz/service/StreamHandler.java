package com.digitalgoetz.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.digitalgoetz.concurrent.ConcurrentTweetList;

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

}
