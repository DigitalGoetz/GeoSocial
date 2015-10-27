package com.digitalgoetz.extractors;

import org.apache.commons.validator.routines.UrlValidator;

public class UrlExtractor {

	private final static String[] schemes = { "http", "https" };
	private final static UrlValidator validator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);

	// public static void main(String[] args) {
	// final UrlExtractor ue = new UrlExtractor();
	//
	// final String url1 = "http://something with tail";
	// final String url2 = "something https://blah and stuff";
	// final String url3 = "never mind the gap http://yarg";
	//
	// System.out.println(ue.extractUrl(url1));
	// System.out.println(ue.extractUrl(url2));
	// System.out.println(ue.extractUrl(url3));
	// }

	public String extractUrl(String text) {
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

		return validateUrl(url);

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
