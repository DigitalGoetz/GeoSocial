package com.digitalgoetz.extractors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jersey.repackaged.com.google.common.base.Joiner;
import twitter4j.Status;

public class ImageExtractor implements Extractor {

	final Client client = ClientBuilder.newClient();

	Logger log = Logger.getLogger(getClass());

	private final String[] imageAffixes = { ".jpg", ".jpeg", ".png", ".bmp", ".gif" };

	boolean endWithImageAffixes(String string) {
		for (final String affix : imageAffixes) {
			if (string.toLowerCase().endsWith(affix)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void extract(Status status, Map<String, String> meta) {
		final String url = meta.get("url");

		final Document doc = getDocument(url);

		if (url != null) {
			final List<String> urls = getImageUrls(status.getId(), doc);
			if (!urls.isEmpty()) {
				log.debug("Image link found");
				meta.put("images", Joiner.on(",").join(urls));
			} else {
				meta.put("images", "");
			}
		}

	}

	Document getDocument(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (final IOException e) {
			return null;
		}
	}

	List<String> getImageUrls(long id, Document document) {
		final List<String> urls = new ArrayList<>();

		if (document == null) {
			return urls;
		}

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
