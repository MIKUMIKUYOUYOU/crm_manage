package net.wanho.util;

import javax.servlet.http.Cookie;

public class ServletUtil {
	public static Cookie getCookieByName(Cookie[] cookies, String name) {
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (name.equals(cookies[i].getName())) {
					return cookies[i];
				}
			}
		}
		return null;
	}
}
