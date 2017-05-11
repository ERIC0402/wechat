package org.beangle.wechat.core.util;

import java.util.Hashtable;

public class SessionUtil {
	
	private static Hashtable<String, Object> session = new Hashtable<String, Object>();

	public static void remove(String key) {
		if (contains(key)) {
			session.remove(key);
		}
	}

	public static void put(String key, Object value) {
		session.put(key, value);
	}

	public static Object get(String key) {
		return session.get(key);
	}

	public static boolean contains(String key) {
		return session.containsKey(key);
	}

	public static void clear() {
		session.clear();
	}
}
