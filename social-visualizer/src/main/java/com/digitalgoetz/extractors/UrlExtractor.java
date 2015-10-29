package com.digitalgoetz.extractors;

import java.util.Map;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;

import twitter4j.Status;

public class UrlExtractor implements Extractor {

	private final static String[] schemes = { "http", "https" };
	private final static UrlValidator validator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);

	Logger log = Logger.getLogger(getClass());

	@Override
	public void extract(Status status, Map<String, String> meta) {

		final String extractUrl = extractUrl(status.getText());
		if (extractUrl != null) {
			meta.put("url", extractUrl);
		}

	}

	private String extractUrl(String text) {
		final int indexOf = text.indexOf("http");
		String url = null;

		if (indexOf >= 0) {
			final String httpStart = text.substring(indexOf);

			int whitespace;
			boolean isInternal = false;

			for (whitespace = 0; whitespace < httpStart.length(); whitespace++) {
				if (Character.isWhitespace(httpStart.charAt(whitespace))) {
					isInternal = true;
					break;
				}
			}

			if (!isInternal) {
				url = httpStart;
			} else {
				url = httpStart.substring(0, whitespace);
			}
		}

		final String extractedUrl = validateUrl(url);
		log.debug("URL: " + extractedUrl);
		return extractedUrl;

	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public Priority getPriority() {
		return Priority.INDEPENDENT;
	}

	private boolean isValid(String url) {
		if (url == null) {
			return false;
		}

		return validator.isValid(url);
	}

	private String validateUrl(String urlCandidate) {
		if (isValid(urlCandidate)) {
			return urlCandidate;
		}
		return null;

	}

}
