package com.digitalgoetz.twitter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.log4j.Logger;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tweet {

	@XmlTransient
	Logger log = Logger.getLogger(getClass());

	String id;

	@XmlTransient
	Date obtained;

	@XmlJavaTypeAdapter(TweetMapAdapter.class)
	Map<String, String> meta;

	public Tweet() {

	}

	public Date getDateObtained() {
		return obtained;
	}

	public String getId() {
		return id;
	}

	public Map<String, String> getMeta() {
		if (meta == null) {
			meta = new HashMap<>();
		}
		return meta;
	}

	public Date getObtained() {
		return obtained;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}

	public void setObtained(Date obtained) {
		this.obtained = obtained;
	}
}
