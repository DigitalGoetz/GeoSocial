package com.digitalgoetz.extractors;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import twitter4j.Status;

public class TestExtractorComparator {

	Extractor high = new Extractor() {
		@Override
		public void extract(Status status, Map<String, String> meta) {
			// do nothing
		}

		@Override
		public String getName() {
			return "Test Extractor High-Priority";
		}

		@Override
		public Priority getPriority() {
			return Priority.INDEPENDENT;
		}
	};

	Extractor mid = new Extractor() {
		@Override
		public void extract(Status status, Map<String, String> meta) {
			// do nothing
		}

		@Override
		public String getName() {
			return "Test Extractor Mid-Priority";
		}

		@Override
		public Priority getPriority() {
			return Priority.FIRST_LEVEL_DEPENDENCY;
		}
	};

	Extractor low = new Extractor() {
		@Override
		public void extract(Status status, Map<String, String> meta) {
			// do nothing
		}

		@Override
		public String getName() {
			return "Test Extractor Low-Priority";
		}

		@Override
		public Priority getPriority() {
			return Priority.SECOND_LEVEL_DEPENDENCY;
		}
	};

	ExtractorComparator testComparator = new ExtractorComparator();

	@Test
	public void testComparisons() {

		final int highTohighCompare = testComparator.compare(high, high);
		assertEquals("A comparison of equal cases should return 0", 0, highTohighCompare);
		final int highToMidCompare = testComparator.compare(high, mid);
		assertEquals("A higher case should return 1 when compared to lower cases", 1, highToMidCompare);
		final int highToLowCompare = testComparator.compare(high, low);
		assertEquals("A higher case should return 1 when compared to lower cases", 1, highToLowCompare);

		final int midToHighCompare = testComparator.compare(mid, high);
		assertEquals("A lower case should return a negative value when compared to a higher case", -1,
				midToHighCompare);
		final int midToMidCompare = testComparator.compare(mid, mid);
		assertEquals("A comparison of equal cases should return 0", 0, midToMidCompare);
		final int midToLowCompare = testComparator.compare(mid, low);
		assertEquals("A higher case should return 1 when compared to lower cases", 1, midToLowCompare);

		final int lowToHighCompare = testComparator.compare(low, high);
		assertEquals("A lower case should return a negative value when compared to a higher case", -1,
				lowToHighCompare);
		final int lowToMidCompare = testComparator.compare(low, mid);
		assertEquals("A lower case should return a negative value when compared to a higher case", -1, lowToMidCompare);
		final int lowToLowCompare = testComparator.compare(low, low);
		assertEquals("A comparison of equal cases should return 0", 0, lowToLowCompare);

	}

	@Test
	public void testSortingWithComparator() {
		final List<Extractor> extractors = new ArrayList<>();

		extractors.add(mid);
		extractors.add(low);
		extractors.add(high);

		Collections.sort(extractors, Collections.reverseOrder(testComparator));

		final Extractor test1 = extractors.get(0);
		assertEquals("First Extractor post-sorting should be highest priority", high.getName(), test1.getName());
		final Extractor test2 = extractors.get(1);
		assertEquals("Second Extractor post-sorting should be middle priority", mid.getName(), test2.getName());
		final Extractor test3 = extractors.get(2);
		assertEquals("Third Extractor post-sorting should be lowest priority", low.getName(), test3.getName());

	}

}
