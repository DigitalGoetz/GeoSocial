package com.digitalgoetz.extractors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import jersey.repackaged.com.google.common.base.Joiner;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import twitter4j.Status;

public class ImageExtractor implements Extractor {

	final Client client = ClientBuilder.newClient();

	Logger log = Logger.getLogger(getClass());

	private final String[] imageAffixes = { ".jpg", ".jpeg", ".png", ".bmp", ".gif" };

	private boolean endWithImageAffixes(String string) {
		for (final String affix : imageAffixes) {
			if (string.endsWith(affix)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void extract(Status status, Map<String, String> meta) {
		final String url = meta.get("url");

		if (url != null) {
			final List<String> urls = getImageUrls(status.getId(), url);
			if (!urls.isEmpty()) {
				log.debug("Image link found");
				meta.put("images", Joiner.on(",").join(urls));
			} else {
				meta.put("images", "");
			}
		}

	}

	private List<String> getImageUrls(long id, String urlString) {
		final List<String> urls = new ArrayList<>();

		try {
			final Document document = Jsoup.connect(urlString).get();
			final Elements anchorElements = document.getElementsByTag("a");
			final Elements imageElements = document.getElementsByTag("img");

			for (final Element element : imageElements) {
				final String imageUrl = element.attr("src");
				if (!imageUrl.isEmpty()) {
					if (endWithImageAffixes(imageUrl)) {
						urls.add(imageUrl);
					}
				}

			}

			for (final Element element : anchorElements) {
				final String anchorHref = element.attr("href");
				if (!anchorHref.isEmpty()) {
					if (endWithImageAffixes(anchorHref)) {
						urls.add(anchorHref);
					}
				}
			}

		} catch (final Exception e) {
			log.error("ERROR Obtaining object from " + urlString, e);
		}

		return urls;
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public Priority getPriority() {
		return Priority.FIRST_LEVEL_DEPENDENCY;
	}

}
