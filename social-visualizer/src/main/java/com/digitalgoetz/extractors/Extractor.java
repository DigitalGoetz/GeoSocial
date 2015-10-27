package com.digitalgoetz.extractors;

import java.util.Map;

import twitter4j.Status;

public interface Extractor {

	public enum Priority {
		INDEPENDENT(1), FIRST_LEVEL_DEPENDENCY(2), SECOND_LEVEL_DEPENDENCY(3);

		int rankValue;

		private Priority(int rankValue) {
			this.rankValue = rankValue;
		}

		public int getRankValue() {
			return this.rankValue;
		}
	}

	public void extract(Status status, Map<String, String> meta);

	public String getName();

	public Priority getPriority();
}
