package com.hachic.webi.auth.application;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class StateService {

	private final SecureRandom random = new SecureRandom();
	private final Map<String, Long> store = new ConcurrentHashMap<>();
	private static final long Ttl = 5 * 60 * 1000;

	public String issue() {
		byte[] buffer = new byte[16];
		random.nextBytes(buffer);
		String str = Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
		store.put(str, System.currentTimeMillis());

		return str;
	}

	public boolean consume(String str) {
		Long exp = store.remove(str);
		return exp != null && exp >= System.currentTimeMillis();
	}
}
