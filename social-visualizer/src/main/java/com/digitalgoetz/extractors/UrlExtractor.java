package com.digitalgoetz.extractors;

import org.apache.commons.validator.routines.UrlValidator;

public class UrlExtractor {

	private final static String[] schemes = { "http", "https" };
	private final static UrlValidator validator = new UrlValidator(schemes);

	public boolean isValid(String url) {
		if (url == null) {
			return false;
		}
		return validator.isValid(url);
	}

	private String extractUrl(String text) {
		String url;

		int indexOf = text.indexOf("http");

		if (indexOf < 0) {
			url = null;
		} else {

			String httpStart = text.substring(indexOf);

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

	public String validateUrl(String urlCandidate) {
		if (isValid(urlCandidate)) {
			return urlCandidate;
		}
		return null;
	}

	public static void main(String[] args) {
		UrlExtractor ue = new UrlExtractor();

		// http://commons.apache.org/proper/commons-validator/apidocs/org/apache/commons/validator/routines/UrlValidator.html

		String url1 = "http://something with tail";
		String url2 = "something https://blah and stuff";
		String url3 = "never mind the gap http://yarg";

		System.out.println(ue.extractUrl(url1));
		System.out.println(ue.extractUrl(url2));
		System.out.println(ue.extractUrl(url3));
	}

}
