package com.digitalgoetz.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.digitalgoetz.extractors.Extractor.Priority;

import twitter4j.Status;

public class TestImageExtractor {

	Logger log = Logger.getLogger(getClass());
	ImageExtractor testExtractor = new ImageExtractor();
	long testId = 12345l;

	private Document getTestDocument() {
		final URL resource = this.getClass().getClassLoader().getResource("test.html");
		Document doc = null;
		try {
			final File file = new File(resource.toURI());
			doc = Jsoup.parse(file, "UTF-8");
		} catch (final IOException e) {
			log.error("Error finding test file", e);
		} catch (final URISyntaxException e) {
			log.error("Error with URI for test file", e);
		}
		return doc;
	}

	@Test
	public void testEndsWithAffixes() {
		log.debug("testEndsWithAffixes");

		final String[] validStrings = { "ASDF.JPG", "asdf.jpg", "ASDF.JPEG", "asdf.jpeg", "ASDF.PNG", "asdf.png",
				"ASDF.BMP", "asdf.bmp", "ASDF.GIF", "asdf.gif" };
		final String[] invalidStrings = { "badstring.txt", "wrongfiletype.dat" };

		for (final String string : validStrings) {
			final boolean correctFileType = testExtractor.endWithImageAffixes(string);
			assertTrue("This should be an appropriate file type", correctFileType);
		}

		for (final String string : invalidStrings) {
			final boolean incorrectFileType = testExtractor.endWithImageAffixes(string);
			assertFalse("This should not be an appropriate file type", incorrectFileType);
		}

	}

	@Test
	@Ignore
	public void testExtract() {

		// FIXME instead of having a local dummy document, spin up a little
		// fileserver
		// and get the URL to that file. Will allow JSoup tests to occur
		final Document testDocument = getTestDocument();

		final Status mockStatus = mock(Status.class);
		when(mockStatus.getId()).thenReturn(testId);

		final ImageExtractor spy = Mockito.spy(testExtractor);
		Mockito.when(spy.getDocument(Mockito.anyString())).thenReturn(testDocument);

		final Map<String, String> meta = new HashMap<>();
		meta.put("url", "someUrl");

		spy.extract(mockStatus, meta);

		final String imageString = meta.get("images");
		assertNotNull(imageString);

		final String[] images = imageString.split(",");
		final List<String> imageList = Arrays.asList(images);

		assertTrue("List should contain all URLs from Test Document", imageList.contains("http://somewhere/image.jpg"));
		assertTrue("List should contain all URLs from Test Document", imageList.contains("http://somewhere/image.jpg"));
		assertTrue("List should contain all URLs from Test Document", imageList.contains("http://somewhere/image.jpg"));
		assertTrue("List should contain all URLs from Test Document", imageList.contains("http://somewhere/image.jpg"));

	}

	@Test
	public void testGetImageUrls() {
		final Document testDoc = getTestDocument();
		if (testDoc == null) {
			fail("Unable to create test document");
		}

		final List<String> imageUrls = testExtractor.getImageUrls(testId, testDoc);

		assertTrue("Could not find the IMG tag url", imageUrls.contains("http://somewhere/image.jpg"));
		assertTrue("Could not find the A tag url", imageUrls.contains("somewhere/relative/pic.png"));

		final List<String> emptyUrls = testExtractor.getImageUrls(testId, null);
		assertEquals("A null document should produce an empty list of url strings", 0, emptyUrls.size());
	}

	@Test
	public void testGetName() {
		log.debug("testGetName");
		assertEquals("com.digitalgoetz.extractors.ImageExtractor", testExtractor.getName());
	}

	@Test
	public void testGetPriority() {
		log.debug("testGetPriority");
		assertEquals(Priority.FIRST_LEVEL_DEPENDENCY, testExtractor.getPriority());
	}

}
