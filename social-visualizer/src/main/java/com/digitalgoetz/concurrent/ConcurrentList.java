package com.digitalgoetz.concurrent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.digitalgoetz.social.SocialMessage;

public class ConcurrentList<T extends SocialMessage> {

	List<T> list;

	public ConcurrentList() {
		list = new ArrayList<T>();
	}

	public synchronized void add(T element) {
		list.add(element);
	}

	public synchronized void removeIndex(int index) {
		list.remove(index);
	}

	public synchronized void clearList(Date now, long windowInMinutes) {
		long window = windowInMinutes * SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;

		long currentTime = now.getTime();
		List<T> newList = new ArrayList<T>();

		for (T message : list) {
			long messageTime = message.getDate().getTime();

			if ((currentTime - messageTime) <= window) {
				newList.add(message);
			}

		}

		list = newList;

	}

	public synchronized List<T> getList() {
		List<T> output = new ArrayList<>();
		for (T message : list) {
			output.add(message);
		}
		return output;
	}

	private final int SECONDS_PER_MINUTE = 60;
	private final int MILLISECONDS_PER_SECOND = 1000;
}
