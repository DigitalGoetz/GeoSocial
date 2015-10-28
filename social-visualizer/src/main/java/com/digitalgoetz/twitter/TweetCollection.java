package com.digitalgoetz.twitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class TweetCollection {

	private static Logger log = Logger.getLogger(TweetCollection.class);

	List<Tweet> list;
	private static TweetCollection instance = null;

	public static TweetCollection getInstance() {
		if (instance == null) {
			instance = new TweetCollection();
		}
		return instance;
	}

	private TweetCollection() {
		list = new ArrayList<Tweet>();
	}

	public synchronized int size() {
		return list.size();
	}

	public synchronized void add(Tweet element) {
		list.add(element);
	}

	public synchronized void removeIndex(int index) {
		list.remove(index);
	}

	public synchronized void clearList(Date now, long windowInMilliseconds) {
		int startsize = list.size();
		long currentTime = now.getTime();

		List<Tweet> newList = new ArrayList<>();

		for (Tweet message : list) {
			long messageTime = message.getDateObtained().getTime();
			if ((currentTime - messageTime) <= windowInMilliseconds) {
				newList.add(message);
			}
		}

		list = newList;
		int endsize = list.size();

		log.debug("Removed " + (startsize - endsize) + " tweets");

	}

	public synchronized List<Tweet> getList() {
		List<Tweet> output = new ArrayList<>();
		for (Tweet message : list) {
			output.add(message);
		}
		return output;
	}
}
