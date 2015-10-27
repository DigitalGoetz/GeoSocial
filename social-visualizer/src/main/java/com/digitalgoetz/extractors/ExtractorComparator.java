package com.digitalgoetz.extractors;

import java.util.Comparator;

public class ExtractorComparator implements Comparator<Extractor> {

	@Override
	public int compare(Extractor o1, Extractor o2) {

		if (o1.getPriority().getRankValue() < o2.getPriority().getRankValue()) {
			return -1;
		} else if (o1.getPriority().getRankValue() > o2.getPriority().getRankValue()) {
			return 1;
		} else {
			return 0;
		}

	}

}
