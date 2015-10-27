package com.digitalgoetz.social;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import twitter4j.Status;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tweet extends SocialMessage {

	String id;
	Date obtained;
	boolean urlIsImage = false;

	public boolean isUrlIsImage() {
		return urlIsImage;
	}

	public void setUrlIsImage(boolean urlIsImage) {
		this.urlIsImage = urlIsImage;
	}

	String url = null;
	String text = null;
	boolean locationDefined = false;
	double latitude;
	double longitude;

	public Tweet(Status source) {
		this.obtained = new Date();
		this.text = source.getText();
		this.id = Long.toString(source.getId());
		if (source.getGeoLocation() != null) {
			locationDefined = true;
			latitude = source.getGeoLocation().getLatitude();
			longitude = source.getGeoLocation().getLongitude();
		}
	}

	@Override
	public Date getDateObtained() {
		return obtained;
	}

	public String getId() {
		return id;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public Network getNetwork() {
		return Network.TWITTER;
	}

	public String getText() {
		return text;
	}

	public String getUrl() {
		if (url == null) {
			url = "";
		}
		return url;
	}

	public boolean isLocationDefined() {
		return locationDefined;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLocationDefined(boolean locationDefined) {
		this.locationDefined = locationDefined;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
