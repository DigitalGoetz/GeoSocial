package com.digitalgoetz.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyUtil;
import org.glassfish.jersey.server.ResourceConfig;

import com.altamira.irad.aede.services.BasicService;
import com.digitalgoetz.StreamCleaner;
import com.digitalgoetz.StreamRunner;
import com.digitalgoetz.concurrent.ConcurrentTweetList;

public class StreamService extends BasicService {

	static Logger log = Logger.getLogger(StreamService.class);

	public StreamService() {
		String host = "localhost";
		int port = 4222;
		serviceUri = uriBuilder.resolveTemplate("host", host).resolveTemplate("port", port).build();
	}

	public HttpServer buildServer() {
		HttpServer httpServer = new HttpServer();
		NetworkListener listener = new NetworkListener("twitstream", serviceUri.getHost(), serviceUri.getPort());
		httpServer.addListener(listener);

		ServerConfiguration config = httpServer.getServerConfiguration();
		CLStaticHttpHandler fileContainer = new CLStaticHttpHandler(StreamService.class.getClassLoader());
		config.addHttpHandler(fileContainer, "/web");

		final ResourceConfig rc = new ResourceConfig().packages("com.digitalgoetz");
		GrizzlyHttpContainer restContainer = GrizzlyUtil.getContainer(rc);
		config.addHttpHandler(restContainer, "/rest");
		return httpServer;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		ConcurrentTweetList tweets = ConcurrentTweetList.getInstance();

		StreamRunner captureThread = new StreamRunner(null, tweets);
		StreamCleaner scrubThread = new StreamCleaner(tweets);

		// Start twitter stream capture and schedule scrubbers
		ExecutorService streamCapture = Executors.newSingleThreadExecutor();
		streamCapture.submit(captureThread);

		ScheduledExecutorService scrubber = Executors.newSingleThreadScheduledExecutor();
		scrubber.schedule(scrubThread, 30, TimeUnit.SECONDS);

		StreamService service = new StreamService();
		HttpServer server = service.buildServer();

		try {
			server.start();
			Thread.currentThread().join();
		} catch (Exception ioe) {
			System.err.println(ioe);
		} finally {
			server.stop();
		}

	}
}
