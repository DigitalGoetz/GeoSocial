package com.digitalgoetz.service;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyUtil;
import org.glassfish.jersey.server.ResourceConfig;

import com.altamira.irad.aede.services.BasicService;

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
