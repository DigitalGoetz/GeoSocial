package com.digitalgoetz.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.persistence.oxm.annotations.XmlVariableNode;

public class TweetMapAdapter extends XmlAdapter<TweetMapAdapter.AdaptedMap, Map<String, String>> {

	public static class AdaptedEntry {

		@XmlTransient
		public String key;

		@XmlValue
		public String value;
	}

	public static class AdaptedMap {

		@XmlVariableNode("key")
		List<AdaptedEntry> entries = new ArrayList<>();

	}

	@Override
	public AdaptedMap marshal(Map<String, String> map) throws Exception {
		final AdaptedMap adaptedMap = new AdaptedMap();
		for (final Entry<String, String> entry : map.entrySet()) {
			final AdaptedEntry adaptedEntry = new AdaptedEntry();
			adaptedEntry.key = entry.getKey();
			adaptedEntry.value = entry.getValue();
			adaptedMap.entries.add(adaptedEntry);
		}
		return adaptedMap;
	}

	@Override
	public Map<String, String> unmarshal(AdaptedMap adaptedMap) throws Exception {
		final List<AdaptedEntry> adaptedEntries = adaptedMap.entries;
		final Map<String, String> map = new HashMap<>(adaptedEntries.size());
		for (final AdaptedEntry entry : adaptedEntries) {
			map.put(entry.key, entry.value);
		}
		return map;
	}
}
