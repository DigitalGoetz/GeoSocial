package com.digitalgoetz.concurrent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.digitalgoetz.social.Tweet;

public class ConcurrentTweetList {

	List<Tweet> list;
	private static ConcurrentTweetList instance = null;

	public static ConcurrentTweetList getInstance() {
		if (instance == null) {
			instance = new ConcurrentTweetList();
		}
		return instance;
	}

	private ConcurrentTweetList() {
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

		long currentTime = now.getTime();
		List<Tweet> newList = new ArrayList<>();

		for (Tweet message : list) {
			long messageTime = message.getDate().getTime();

			if ((currentTime - messageTime) <= windowInMilliseconds) {
				newList.add(message);
			}

		}

		list = newList;

	}

	public synchronized List<Tweet> getList() {
		List<Tweet> output = new ArrayList<>();
		for (Tweet message : list) {
			output.add(message);
		}
		return output;
	}
}
