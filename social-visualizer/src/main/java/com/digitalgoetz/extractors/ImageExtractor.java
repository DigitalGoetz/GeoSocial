package com.digitalgoetz.extractors;

import java.awt.Image;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class ImageExtractor {

	Logger log = Logger.getLogger(getClass());

	public boolean urlContainsImage(String urlString) {
		boolean containsImage = false;

		try {
			Image image = null;
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {

				// Replace with Jersey Client to perform simple request of
				// resource and determine response media type
				// if image and all that jazz

				if (image != null) {
					containsImage = true;
				}
			}

		} catch (Exception e) {

			log.error("Obtaining object from " + urlString, e);
		}
		return containsImage;
	}

}
