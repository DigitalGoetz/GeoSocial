package com.digitalgoetz.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import com.digitalgoetz.extractors.Extractor;
import com.digitalgoetz.extractors.ExtractorComparator;

import twitter4j.Status;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tweet extends SocialMessage {

	Logger log = Logger.getLogger(getClass());

	String id;
	Date obtained;

	public Tweet(Status source, Extractor... extractors) {
		final List<Extractor> extractorList = new ArrayList<>();
		for (final Extractor extractor : extractors) {
			extractorList.add(extractor);
		}

		id = Long.toString(source.getId());
		obtained = new Date();
		meta = new HashMap<>();

		meta.put("text", source.getText());

		if (source.getGeoLocation() != null) {
			meta.put("latitude", Double.toString(source.getGeoLocation().getLatitude()));
			meta.put("longitude", Double.toString(source.getGeoLocation().getLongitude()));
		}

		Collections.sort(extractorList, new ExtractorComparator());

		log.debug(id + " EXTRACTION PROCESS");
		for (final Extractor extractor : extractorList) {
			log.debug(id + ": Extracting via " + extractor.getName() + " with priority " + extractor.getPriority());
			extractor.extract(source, meta);
		}
	}

	@Override
	public Date getDateObtained() {
		return obtained;
	}

	public String getId() {
		return id;
	}

	@Override
	public Network getNetwork() {
		return Network.TWITTER;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		String str = "";

		str += "{ 'id' : '" + id + "' }";

		return str;
	}

}
