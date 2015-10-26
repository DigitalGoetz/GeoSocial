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

import com.digitalgoetz.StreamCleaner;
import com.digitalgoetz.StreamRunner;
import com.digitalgoetz.concurrent.ConcurrentTweetList;
import com.digitalgoetz.service.commons.BasicService;

public class StreamService extends BasicService {

	static Logger log = Logger.getLogger(StreamService.class);

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		final boolean runServerWithStream = false;

		final ConcurrentTweetList tweets = ConcurrentTweetList.getInstance();

		final StreamRunner captureThread = new StreamRunner(null, tweets);
		final StreamCleaner scrubThread = new StreamCleaner(tweets);

		// Start twitter stream capture and schedule scrubbers
		final ExecutorService streamCapture = Executors.newSingleThreadExecutor();
		final ScheduledExecutorService scrubber = Executors.newSingleThreadScheduledExecutor();

		if (runServerWithStream) {
			streamCapture.submit(captureThread);
			scrubber.scheduleWithFixedDelay(scrubThread, 5, 15, TimeUnit.SECONDS);
		}

		final StreamService service = new StreamService();
		final HttpServer server = service.buildServer();

		try {
			server.start();
			Thread.currentThread().join();
		} catch (final Exception ioe) {
			System.err.println(ioe);
		} finally {
			server.stop();
		}

	}

	public StreamService() {
		final String host = "localhost";
		final int port = 4222;
		serviceUri = uriBuilder.resolveTemplate("host", host).resolveTemplate("port", port).build();
	}

	public HttpServer buildServer() {
		final HttpServer httpServer = new HttpServer();
		final NetworkListener listener = new NetworkListener("twitstream", serviceUri.getHost(), serviceUri.getPort());
		httpServer.addListener(listener);

		final ServerConfiguration config = httpServer.getServerConfiguration();
		final CLStaticHttpHandler fileContainer = new CLStaticHttpHandler(StreamService.class.getClassLoader());
		config.addHttpHandler(fileContainer, "/");

		final ResourceConfig rc = new ResourceConfig().packages("com.digitalgoetz");
		final GrizzlyHttpContainer restContainer = GrizzlyUtil.getContainer(rc);
		config.addHttpHandler(restContainer, "/rest");
		return httpServer;
	}
}
